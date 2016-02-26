package com.DFM.Handlers;

import com.DFM.Clients.*;
import com.DFM.Interfaces.*;

import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mick on 2/12/2016.
 */
public class WordPressHandler {
    //Handle calls to WordPress

    public static Response PostPost(String redisType,
                                    String redisKey,
                                    String remoteEndpoint,
                                    InputStream incomingData){
        StringBuilder Builder = new StringBuilder();
        Map<String, String> mapResult = new HashMap<String, String>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                Builder.append(line);
            }
            String json = Builder.toString();
            RedisClient redisClient = new RedisClient(redisType);
            Map<String, String> subscriberMap = redisClient.hgetAll(redisKey);
            String postEndpoint = subscriberMap.get("url") + remoteEndpoint;
            WordPressClient wpc = NewClient(subscriberMap);
            mapResult = WordPressInterface.postJson(json, postEndpoint, wpc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Response.status(500)
                    .header("error", String.format("CMS Error: %s, Broker Error: %s", mapResult.get("error"), e.getMessage()))
                    .entity(mapResult.get("error"))
                    .build();
        }

        if(WebClient.isBad(Integer.parseInt(mapResult.get("code")))){
            return Response.status(500)
                    .header("error", mapResult.get("error"))
                    .entity(mapResult.get("error"))
                    .build();
        }

        return Response.status(Integer.parseInt(mapResult.get("code")))
                .header("id", mapResult.get("wpPostId"))
                .header("location", mapResult.get("postLocation"))
                .entity(mapResult.get("result"))
                .build();

    }


    public static Response GetPost(String redisType,
                                   String redisKey,
                                   String remoteEndpoint) {
        Map<String, String> mapResult = new HashMap<String, String>();
        try {
            RedisClient redisClient = new RedisClient(redisType);
            Map<String, String> subscriberMap = redisClient.hgetAll(redisKey);
            String postEndpoint = subscriberMap.get("url") + remoteEndpoint;
            WordPressClient wpc = NewClient(subscriberMap);
            mapResult = WordPressInterface.getPost(postEndpoint, wpc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Response.status(500)
                    .header("error", String.format("CMS Error: %s, Broker Error: %s", mapResult.get("error"), e.getMessage()))
                    .build();
        }

        if(WebClient.isBad(Integer.parseInt(mapResult.get("code")))){
            return Response.status(500)
                    .header("error", mapResult.get("error"))
                    .build();
        }

        return Response.status(Integer.parseInt(mapResult.get("code")))
                .header("id", mapResult.get("wpPostId"))
                .header("location", mapResult.get("postLocation"))
                .entity(mapResult.get("body"))
                .build();
    }



    public static Response PostMedia(String redisType,
                                     String redisKey,
                                     String remoteEndpoint,
                                     InputStream incomingData) {
        StringBuilder Builder = new StringBuilder();
        Map<String, String> mapResult = new HashMap<String, String>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                Builder.append(line);
            }
            String json = Builder.toString();
            RedisClient redisClient = new RedisClient(redisType);
            Map<String, String> subscriberMap = redisClient.hgetAll(redisKey);
            String mediaEndpoint = subscriberMap.get("url") + remoteEndpoint;
            WordPressClient wpc = NewClient(subscriberMap);
            mapResult = WordPressInterface.postMedia(json, mediaEndpoint, wpc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Response.status(500)
                    .header("error", String.format("CMS Error: %s, Broker Error: %s", mapResult.get("error"), e.getMessage()))
                    .build();
        }

        if(WebClient.isBad(Integer.parseInt(mapResult.get("code")))){
            return Response.status(500)
                    .header("error", mapResult.get("error"))
                    .build();
        }

        return Response.status(Integer.parseInt(mapResult.get("code")))
                .header("id", mapResult.get("wpImageId"))
                .header("location", mapResult.get("mediaLocation"))
                .entity(mapResult.get("result"))
                .build();
    }



    public static Response DeleteAttributes(String redisType,
                                            String redisKey,
                                            String remoteEndpoint) {
        Map<String, String> mapResult = new HashMap<String, String>();
        try {
            RedisClient redisClient = new RedisClient(redisType);
            Map<String, String> subscriberMap = redisClient.hgetAll(redisKey);
            String postEndpoint = subscriberMap.get("url") + remoteEndpoint;
            WordPressClient wpc = NewClient(subscriberMap);
            mapResult = WordPressInterface.postDelete(postEndpoint, wpc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Response.status(500)
                    .header("error", String.format("CMS Error: %s, Broker Error: %s", mapResult.get("error"), e.getMessage()))
                    .build();
        }

        if(WebClient.isBad(Integer.parseInt(mapResult.get("code")))){
            return Response.status(500)
                    .header("error", mapResult.get("error"))
                    .build();
        }

        return Response.status(Integer.parseInt(mapResult.get("code")))
                .header("location", mapResult.get("location"))
                .entity(mapResult.get("result"))
                .build();
    }


    //Sets default author
    public static Response PostDefaultAuthor(String redisType,
                                             String redisKey,
                                             String remoteEndpoint){
        Map<String, String> mapResult = new HashMap<String, String>();
        try {
            RedisClient redisClient = new RedisClient(redisType);
            Map<String, String> subscriberMap = redisClient.hgetAll(redisKey);
            String postEndpoint = subscriberMap.get("url") + remoteEndpoint;
            String json = String.format("{\"author\":\"%s\"}", subscriberMap.get("userid"));
            WordPressClient wpc = NewClient(subscriberMap);
            mapResult = WordPressInterface.postJson(json, postEndpoint, wpc);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return Response.status(500)
                    .header("error", String.format("CMS Error: %s, Broker Error: %s", mapResult.get("error"), e.getMessage()))
                    .build();
        }

        if(WebClient.isBad(Integer.parseInt(mapResult.get("code")))){
            return Response.status(500)
                    .header("error", mapResult.get("error"))
                    .build();
        }

        return Response.status(Integer.parseInt(mapResult.get("code")))
                .header("id", mapResult.get("wpPostId"))
                .header("location", mapResult.get("postLocation"))
                .entity(mapResult.get("result"))
                .build();

    }


    private static WordPressClient NewClient(Map<String, String> subscriberMap) throws Exception {
        if (subscriberMap.get("AC").equalsIgnoreCase("O1")) {
            if (2 == 12) {
                System.out.println("DISCOVERED OAUTH: '" + subscriberMap.get("AC") + "'");
            }
            return new WordPressOauth1Client(subscriberMap.get("url"), subscriberMap.get("AT"), subscriberMap.get("AS"), subscriberMap.get("CK"), subscriberMap.get("CS"));
        } else if (subscriberMap.get("AC").equalsIgnoreCase("B")) {
            if (2 == 12)
                System.out.println("DISCOVERED BASIC: '" + subscriberMap.get("AC").toUpperCase() + "'");
            return new WordPressBasicClient(subscriberMap.get("url"), subscriberMap.get("stamp"), subscriberMap.get("validity"));
        } else {
            if (3 == 13) {
                System.out.println("DISCOVERED NOTHING: '" + subscriberMap.get("AC").toUpperCase() + "'");
            }
            throw new Exception("WordPressClient type is unknown. Access type requested: " + subscriberMap.get("AC"));
        }
    }



}
