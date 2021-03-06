package com.DFM.Clients;

import org.glassfish.jersey.client.oauth1.AccessToken;
import org.glassfish.jersey.client.oauth1.ConsumerCredentials;
import org.glassfish.jersey.client.oauth1.OAuth1ClientSupport;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by Mick on 12/21/2015.
 */
public class WebOauth1Client {
    public String PROPERTY_ACCESS_TOKEN;
    public String PROPERTY_ACCESS_TOKEN_SECRET;
    public String PROPERTY_CONSUMER_KEY;
    public String PROPERTY_CONSUMER_SECRET;


    public WebOauth1Client() {
    }

    public WebOauth1Client(String property_access_token, String property_access_token_secret, String property_consumer_key, String property_consumer_secret) {
        this.PROPERTY_ACCESS_TOKEN = property_access_token;
        this.PROPERTY_ACCESS_TOKEN_SECRET = property_access_token_secret;
        this.PROPERTY_CONSUMER_KEY = property_consumer_key;
        this.PROPERTY_CONSUMER_SECRET = property_consumer_secret;
    }

    public String get(String uri) throws IOException {
        Client client = buildClient();

        // make requests to protected Resources
        Response response = client.target(uri).request().get();
        return response.readEntity(String.class);
    }

    public HashMap<String, String> post(String uri) throws IOException {
        return post(uri, "{}");
    }

    public HashMap<String, String> post(String uri, String json) throws IOException {
        Client client = buildClient();

        // make requests to protected Resources
        if (2 == 12) showAccessTokens(uri);
        Entity entity = Entity.json(json);
        Response response = client.target(uri).request().post(entity);
        HashMap<String, String> mapResult = new HashMap<String, String>();
        mapResult.put("code", Integer.toString(response.getStatus()));
        mapResult.put("location", String.valueOf(response.getLocation()));
        mapResult.put("result", response.readEntity(String.class));
        client.close();
        //System.out.println(mapResult);
        return mapResult;
    }

    public HashMap<String, String> uploadImage(String uri, BufferedImage image, String imageType, String imageName) throws IOException, ParseException {
        Client client = buildClient();

        WebTarget target = client.target(uri);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        ImageIO.write(image, "jpg", baos);

        byte[] bits = baos.toByteArray();

/*
        BodyPart body = new BodyPart(bits, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        MultiPart multiPartEntity = new MultiPart().bodyPart(body);
        final Entity entity = Entity.entity(multiPartEntity, multiPartEntity.getMediaType());
        //Request req = (Request) client.target(uri).request();
*/

        Response response = target.request()
                .header("Content-Type", imageType)
                .header("Content-Disposition", "filename=" + imageName)
                .header("Accept", "application/json")
                .post(Entity.entity(bits, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        HashMap<String, String> mapResult = new HashMap<String, String>();
        mapResult.put("code", Integer.toString(response.getStatus()));
        mapResult.put("location", String.valueOf(response.getLocation()));
        mapResult.put("result", response.readEntity(String.class));
        //System.out.println(mapResult);

        return mapResult;
    }

    public HashMap<String, String> delete(String uri) throws IOException {
        Client client = buildClient();

        // make requests to protected Resources
        Response response = client.target(uri).request()
                .header("force", "true")
                .delete();
        HashMap<String, String> mapResult = new HashMap<String, String>();
        mapResult.put("code", Integer.toString(response.getStatus()));
        mapResult.put("result", response.readEntity(String.class));
        client.close();
        return mapResult;
    }

    public Client buildClient() {
        //Access tokens are already available from last execution
        ConsumerCredentials consumerCredentials = new ConsumerCredentials(PROPERTY_CONSUMER_KEY, PROPERTY_CONSUMER_SECRET);
        AccessToken storedToken = new AccessToken(PROPERTY_ACCESS_TOKEN, PROPERTY_ACCESS_TOKEN_SECRET);
        Feature filterFeature;
        // build a new filter feature from the stored consumer credentials and access token
        filterFeature = OAuth1ClientSupport.builder(consumerCredentials).feature()
                .accessToken(storedToken).build();

        //ClientConfig configuration = new ClientConfig();
        // configuration = configuration.property(ClientProperties.ASYNC_THREADPOOL_SIZE, 1000);

        // create a new Jersey client and register filter feature that will add OAuth signatures and
        // JacksonFeature that will process returned json data.
        Client client = ClientBuilder.newBuilder()
                .register(filterFeature)
                .register(JacksonFeature.class)
                .register(MultiPartFeature.class)
                //.withConfig(configuration)
                .build();
        return client;
    }

    private void showAccessTokens(String uri) {
        System.out.println("WebOauth1Client uri: " + uri);
        System.out.println("WebOauth1Client PROPERTY_ACCESS_TOKEN: " + PROPERTY_ACCESS_TOKEN);
        System.out.println("WebOauth1Client PROPERTY_ACCESS_TOKEN_SECRET: " + PROPERTY_ACCESS_TOKEN_SECRET);
        System.out.println("WebOauth1Client PROPERTY_CONSUMER_KEY: " + PROPERTY_CONSUMER_KEY);
        System.out.println("WebOauth1Client PROPERTY_CONSUMER_SECRET: " + PROPERTY_CONSUMER_SECRET);
    }
}
