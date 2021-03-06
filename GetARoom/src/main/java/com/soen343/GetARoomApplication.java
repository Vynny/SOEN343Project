package com.soen343;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import com.soen343.client.ReservationController;
import com.soen343.client.RoomController;
import com.soen343.client.UserController;
import com.soen343.db.QueueNodeEdgeTDG;
import com.soen343.db.ReservationTDG;
import com.soen343.db.RoomTDG;
import com.soen343.db.UserTDG;
import com.soen343.session.ReservationSessionManager;
import com.soen343.auth.GetARoomAuthenticator;
import com.soen343.core.User;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.auth.*;

public class GetARoomApplication extends Application<GetARoomConfiguration> {
	
	final Logger logger = LoggerFactory.getLogger(GetARoomApplication.class);

	public static void main(final String[] args) throws Exception {
		new GetARoomApplication().run(args);
	}

	@Override
	public String getName() {
		return "GetARoom";
	}

	@Override
	public void initialize(final Bootstrap<GetARoomConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
	}

	@Override
	public void run(final GetARoomConfiguration configuration, final Environment environment) throws Exception {	
		//Set root path to /api/*
		((DefaultServerFactory) configuration.getServerFactory()).setJerseyRootPath("/api/*");

		//Database Initialization
		final DBIFactory factory = new DBIFactory();
		environment.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "database");
		
		//TDG Initialization
		final RoomTDG roomTDG = jdbi.onDemand(RoomTDG.class);
		final ReservationTDG reservationTDG = jdbi.onDemand(ReservationTDG.class);
		final UserTDG userTDG = jdbi.onDemand(UserTDG.class);
		final QueueNodeEdgeTDG queueNodeEdgeTDG = jdbi.onDemand(QueueNodeEdgeTDG.class);
		
		//Controller Initialization
		ReservationSessionManager reservationSessionManager = new ReservationSessionManager();
		
		RoomController roomController = new RoomController(roomTDG, reservationSessionManager);
		ReservationController reservationController = new ReservationController(reservationTDG, queueNodeEdgeTDG, reservationSessionManager, Long.parseLong(configuration.getMaxActiveReservations()));
		UserController userController = new UserController(userTDG, configuration.getJwtTokenSecret());
		
		reservationSessionManager.setReservationController(reservationController);
		reservationSessionManager.setRoomController(roomController);
				
		//Controller Registration
		environment.jersey().register(reservationController);
	    environment.jersey().register(roomController);
	    environment.jersey().register(userController);

	    //Authentication
	    final byte[] key = configuration.getJwtTokenSecret();
        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(new HmacKey(key)) // verify the signature with the public key
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build(); // create the JwtConsumer instance
	    
	    environment.jersey().register(new AuthDynamicFeature (
	    		new JwtAuthFilter.Builder<User>()
	    			.setJwtConsumer(consumer)
	    			.setRealm("realm")
	    			.setPrefix("Bearer")
	    			.setAuthenticator(new GetARoomAuthenticator(userController))
	    			.buildAuthFilter()));
	    	    
	    //Configure Cross Origin Resource Sharing
	    configureCors(environment);	    
	}
	
	private void configureCors(Environment environment) {
        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
      }

}
