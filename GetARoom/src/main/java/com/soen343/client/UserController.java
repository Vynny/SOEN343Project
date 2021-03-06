package com.soen343.client;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import com.codahale.metrics.annotation.Timed;
import com.soen343.core.User;
import com.soen343.db.UserTDG;
import com.soen343.mappers.UserMapper;

import jersey.repackaged.com.google.common.base.Throwables;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
	
	private UserMapper userMapper;
    private final byte[] tokenSecret;

    public UserController(UserTDG userTDG, byte[] tokenSecret) {
    	this.tokenSecret = tokenSecret;
    	this.userMapper = new UserMapper(userTDG);
    }
    
    @GET
    @Path("/{id}")
    @Timed
    public Map<String, String> getUser(@PathParam("id") Integer id) {
    	User user = userMapper.get(id);
    	if (user != null) {
    		HashMap<String,String> response = new HashMap<String,String>();
    		response.put("username", user.getName());
            return response;
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
    
    public User getUser(long id) {
    	return userMapper.get(id);
    }
    
    @GET
    @Path("/login")
    public Map<String, String> generateUserToken(@QueryParam("username") String username, @QueryParam("password") String password) {
		User session = userMapper.get(username);
		
		if (session != null && session.getPassword().equals(password)) {
	    	final JwtClaims claims = new JwtClaims();
	        claims.setSubject(Long.toString(session.getId()));
	        claims.setExpirationTimeMinutesInTheFuture(30);

	        final JsonWebSignature jws = new JsonWebSignature();
	        jws.setPayload(claims.toJson());
	        jws.setAlgorithmHeaderValue(HMAC_SHA256);
	        jws.setKey(new HmacKey(tokenSecret));

	        try {
	            //return singletonMap("token", jws.getCompactSerialization());
	        	Map<String, String> response = new HashMap<String, String>();
	        	response.put("userId", Long.toString(session.getId()));
	        	response.put("token", jws.getCompactSerialization());
	            return response;
	        }
	        catch (JoseException e) { throw Throwables.propagate(e); }
		}
		else {
			return null;
		}
    }
}
