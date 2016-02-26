package com.DFM.Clients;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.HashMap;

//import org.apache.http.client.entity.UrlEncodedFormEntity;


public class WordPressBasicClient extends WordPressClient {

	public WordPressBasicClient(){}

	public WordPressBasicClient(String host, String username, String password){
		super(host, username, password);
	}

/*
	public HashMap<String, String> post(String endpoint, WordPressPost wpp) throws ClientProtocolException, IOException {
		String wppJSON = JsonUtil.toJSON(wpp);
		if (1==1) System.out.println("USING BASE");
		//if (1==11) System.out.println("JSON: " + wppJSON);
		return post(endpoint, wppJSON);
	}
*/

	public HashMap<String, String> post(String endpoint, String json) throws ClientProtocolException, IOException {
			String result = "";
			String url = super.getHost() + endpoint;
		if (2==2) System.out.println("No Creds URL: " + url);
		String credentials = super.getUsername() + ":" + super.getPassword();
		//if (1==11) System.out.println("credentials: " + credentials);
		String credentials64 = Base64.encodeBase64URLSafeString(credentials.getBytes());
		while((credentials64.length() % 4) > 0){
			credentials64 += "=";
		}
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		post.setHeader("Authorization", "Basic " + credentials64);
		post.setHeader(new BasicHeader("Content-Type", "application/json"));
		post.setHeader(new BasicHeader("Accept", "application/json"));
		//if (1==11) System.out.println("JSON: " + json);
		StringEntity input = new StringEntity(json);
		post.setEntity(input);
		HttpResponse response = client.execute(post);
		StatusLine sl = response.getStatusLine();
		if( WebClient.isOK(sl.getStatusCode())){
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result += line;
			}
		}
		else {
			result = sl.getReasonPhrase();
		}
		HashMap<String, String> mapResult = new HashMap<String, String>();
		mapResult.put("code", Integer.toString(sl.getStatusCode()));
		mapResult.put("result", result);
		return mapResult;
	}

	public HashMap<String, String> post(String endpoint) throws ClientProtocolException, IOException {
		String result = "";
		String url = super.getHost() + endpoint;
		String credentials = super.getUsername() + ":" + super.getPassword();
		//if (1==11) System.out.println("credentials: " + credentials);
		String credentials64 = Base64.encodeBase64URLSafeString(credentials.getBytes());
		while((credentials64.length() % 4) > 0){
			credentials64 += "=";
		}
		HttpClient client = HttpClientBuilder.create().build();
		//if (1==11) System.out.println("POST: " + url);
		HttpPost post = new HttpPost(url);
		post.setHeader("Authorization", "Basic " + credentials64);
		post.setHeader(new BasicHeader("Content-Type", "application/json"));
		post.setHeader(new BasicHeader("Accept", "application/json"));
		HttpResponse response = client.execute(post);
		StatusLine sl = response.getStatusLine();
		if( sl.getStatusCode() >= 200 && sl.getStatusCode() < 300){
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result += line;
			}
		}
		else {
			result = sl.getReasonPhrase();
		}
		HashMap<String, String> mapResult = new HashMap<String, String>();
		mapResult.put("code", Integer.toString(sl.getStatusCode()));
		mapResult.put("result", result);
		return mapResult;
	}

	public HashMap<String, String> delete(String endpoint) throws ClientProtocolException, IOException {
		HashMap<String, String> mapResult = new HashMap<String, String>();
		String result = "";
		String url = super.getHost() + endpoint;
		String credentials = super.getUsername() + ":" + super.getPassword();
		//if (1==11) System.out.println("credentials: " + credentials);
		String credentials64 = Base64.encodeBase64URLSafeString(credentials.getBytes());
		while((credentials64.length() % 4) > 0){
			credentials64 += "=";
		}
		HttpClient client = HttpClientBuilder.create().build();
		if (9==9) System.out.println("DELETE: " + url);
		HttpDelete delete = new HttpDelete();
		delete.setHeader("Authorization", "Basic " + credentials64);
		delete.setHeader(new BasicHeader("Content-Type", "application/json"));
		delete.setHeader(new BasicHeader("Accept", "application/json"));
		try{
			if (9==9) System.out.println("DELETE start: " + url);
			HttpResponse response = client.execute(delete);
			if (9==9) System.out.println("DELETE end: " + url);
			StatusLine sl = response.getStatusLine();
			if( sl.getStatusCode() >= 200 && sl.getStatusCode() < 300){
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					result += line;
				}
			}
			else {
				result = sl.getReasonPhrase();
			}
			mapResult.put("code", Integer.toString(sl.getStatusCode()));
			mapResult.put("result", result);
		}
		catch (java.lang.NullPointerException e){
			if (9==9) System.out.println("DELETE ERROR: " + e.getMessage());
			mapResult.put("code", "500");
			mapResult.put("result", e.getMessage());
		}
		return mapResult;
	}

/*
	public HashMap<String, String> put(String endpoint, WordPressPost wpp) throws ClientProtocolException, IOException {
		String wppJSON = JsonUtil.toJSON(wpp);
		//if (1==11) System.out.println("JSON: " + wppJSON);
		return put(endpoint, wppJSON);
	}
*/

	public HashMap<String, String> put(String endpoint, String json) throws ClientProtocolException, IOException {
		String result = "";
		String url = super.getHost() + endpoint;
		String credentials = super.getUsername() + ":" + super.getPassword();
		//if (1==11) System.out.println("credentials: " + credentials);
		String credentials64 = Base64.encodeBase64URLSafeString(credentials.getBytes());
		while((credentials64.length() % 4) > 0){
			credentials64 += "=";
		}
		HttpClient client = HttpClientBuilder.create().build();
		//if (1==11) System.out.println("PUT: " + url);
		HttpPut put = new HttpPut(url);
		put.setHeader("Authorization", "Basic " + credentials64);
		put.setHeader(new BasicHeader("Content-Type", "application/json"));
		put.setHeader(new BasicHeader("Accept", "application/json"));
		//if (1==11) System.out.println("JSON: " + json);
		StringEntity input = new StringEntity(json);
		put.setEntity(input);
		HttpResponse response = client.execute(put);
		StatusLine sl = response.getStatusLine();
		if( sl.getStatusCode() >= 200 && sl.getStatusCode() < 300){
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result += line;
			}
		}
		else {
			result = sl.getReasonPhrase();
		}
		HashMap<String, String> mapResult = new HashMap<String, String>();
		mapResult.put("code", Integer.toString(sl.getStatusCode()));
		mapResult.put("result", result);
		return mapResult;
	}

	public HashMap<String, String> put(String endpoint) throws ClientProtocolException, IOException {
		String result = "";
		String url = super.getHost() + endpoint;
		String credentials = super.getUsername() + ":" + super.getPassword();
		//if (1==11) System.out.println("credentials: " + credentials);
		String credentials64 = Base64.encodeBase64URLSafeString(credentials.getBytes());
		while((credentials64.length() % 4) > 0){
			credentials64 += "=";
		}
		HttpClient client = HttpClientBuilder.create().build();
		//if (1==11) System.out.println("PUT: " + url);
		HttpPut put = new HttpPut(url);
		put.setHeader("Authorization", "Basic " + credentials64);
		put.setHeader(new BasicHeader("Content-Type", "application/json"));
		put.setHeader(new BasicHeader("Accept", "application/json"));
		HttpResponse response = client.execute(put);
		StatusLine sl = response.getStatusLine();
		if( sl.getStatusCode() >= 200 && sl.getStatusCode() < 300){
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result += line;
			}
		}
		else {
			result = sl.getReasonPhrase();
		}
		HashMap<String, String> mapResult = new HashMap<String, String>();
		mapResult.put("code", Integer.toString(sl.getStatusCode()));
		mapResult.put("result", result);
		return mapResult;
	}


	public HashMap<String, String> uploadImage(String endpoint, String imageSource, String imageType, String imageName) throws ClientProtocolException, IOException {
		WebClient wc = new WebClient(imageSource);
		BufferedImage bi = wc.GetImage();
		return upload(endpoint, bi, imageType, imageName);
	}

	public HashMap<String, String> upload(String endpoint, BufferedImage image, String imageType, String imageName) throws ClientProtocolException, IOException {
		String result = "";
		String url = super.getHost() + endpoint;
		String credentials = super.getUsername() + ":" + super.getPassword();
		String authorizationString = "Basic " + Base64.encodeBase64URLSafeString(credentials.getBytes());
		HttpClient client = HttpClientBuilder.create().build();
		//if (1==11) System.out.println("UPLOAD: " + url);

		HttpPost post = new HttpPost(url);
		post.setHeader(new BasicHeader("Content-Type", imageType));
		post.setHeader(new BasicHeader("Content-Disposition", "filename=" + imageName));
		post.setHeader(new BasicHeader("Accept", "application/json"));
		post.setHeader("Authorization", authorizationString);

		ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
		ImageIO.write(image, "jpg", baos);
		//baos.flush();

		ByteArrayEntity bits = new ByteArrayEntity(baos.toByteArray());
		baos.close();

		post.setEntity(bits);

		HttpResponse response = client.execute(post);
		StatusLine sl = response.getStatusLine();
		if( sl.getStatusCode() >= 200 && sl.getStatusCode() < 300){
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result += line;
			}
		}
		else {
			result = sl.getReasonPhrase();
			//if (1==11) System.out.println("BAD result: " + result);
		}
		HashMap<String, String> mapResult = new HashMap<String, String>();
		mapResult.put("code", Integer.toString(sl.getStatusCode()));
		mapResult.put("result", result);
		return mapResult;
	}
}
