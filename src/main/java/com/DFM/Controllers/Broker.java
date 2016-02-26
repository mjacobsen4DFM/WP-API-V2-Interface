package com.DFM.Controllers;

import com.DFM.Handlers.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by Mick on 2/3/2016.
 */

@Path("/broker")
public class Broker {
    @POST
    @Path("/wordpress/posts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@HeaderParam("user-agent")
    public Response PostPost(@HeaderParam("RedisType") String redisType,
                             @HeaderParam("RedisKey") String redisKey,
                             @HeaderParam("remoteEndpoint") String remoteEndpoint,
                             InputStream incomingData) {
        return WordPressHandler.PostPost(redisType, redisKey, remoteEndpoint, incomingData);
    }

    @GET
    @Path("/wordpress/posts")
    @Produces(MediaType.APPLICATION_JSON)
    //@HeaderParam("user-agent")
    public Response GetPost(@HeaderParam("RedisType") String redisType,
                            @HeaderParam("RedisKey") String redisKey,
                            @HeaderParam("remoteEndpoint") String remoteEndpoint) {
        return WordPressHandler.GetPost(redisType, redisKey, remoteEndpoint);
    }

    @POST
    @Path("/wordpress/posts/attributes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@HeaderParam("user-agent")
    public Response PostAttributes(@HeaderParam("RedisType") String redisType,
                                   @HeaderParam("RedisKey") String redisKey,
                                   @HeaderParam("remoteEndpoint") String remoteEndpoint,
                                   InputStream incomingData) {
        return WordPressHandler.PostPost(redisType, redisKey, remoteEndpoint, incomingData);
    }



    @DELETE
    @Path("/wordpress/posts/attributes")
    @Produces(MediaType.APPLICATION_JSON)
    //@HeaderParam("user-agent")
    public Response DeleteAttributes(@HeaderParam("RedisType") String redisType,
                                     @HeaderParam("RedisKey") String redisKey,
                                     @HeaderParam("remoteEndpoint") String remoteEndpoint) {
        return WordPressHandler.DeleteAttributes(redisType, redisKey, remoteEndpoint);
    }



    @POST
    @Path("/wordpress/posts/media")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@HeaderParam("user-agent")
    public Response PostMedia(@HeaderParam("RedisType") String redisType,
                              @HeaderParam("RedisKey") String redisKey,
                              @HeaderParam("remoteEndpoint") String remoteEndpoint,
                              InputStream incomingData) {
        return WordPressHandler.PostMedia(redisType, redisKey, remoteEndpoint, incomingData);
    }


    @POST
    @Path("/wordpress/posts/defaultauthor")
    @Produces(MediaType.APPLICATION_JSON)
    //@HeaderParam("user-agent")
    public Response PostAuthor(@HeaderParam("RedisType") String redisType,
                               @HeaderParam("RedisKey") String redisKey,
                               @HeaderParam("remoteEndpoint") String remoteEndpoint) {
        return WordPressHandler.PostDefaultAuthor(redisType, redisKey, remoteEndpoint);
    }


}
