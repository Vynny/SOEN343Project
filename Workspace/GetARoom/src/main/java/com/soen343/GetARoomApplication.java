package com.soen343;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class GetARoomApplication extends Application<GetARoomConfiguration> {

    public static void main(final String[] args) throws Exception {
        new GetARoomApplication().run(args);
    }

    @Override
    public String getName() {
        return "GetARoom";
    }

    @Override
    public void initialize(final Bootstrap<GetARoomConfiguration> bootstrap) {
        // TODO: application initialization		
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
    public void run(final GetARoomConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    	((DefaultServerFactory) configuration.getServerFactory()).setJerseyRootPath("/api/*");
    }
    /*
    private void configureCors(Environment environment) {
        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
      }*/

}