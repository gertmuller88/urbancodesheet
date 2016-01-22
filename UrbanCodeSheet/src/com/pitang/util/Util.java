package com.pitang.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.pitang.util.rest.Module;
import com.pitang.util.rest.Operation;
import com.urbancode.commons.httpcomponentsutil.HttpClientBuilder;

public class Util {

	public static final String CONFIG_FILE = "conf" + File.separator + "config.properties";

	public static final String LOG4J_CONFIG_FILE = "conf" + File.separator + "log4j.xml";

	public static final String URL = "DS_WEB_URL";

	public static final String USERNAME = "DS_USERNAME";

	public static final String PASSWORD = "DS_PASSWORD";

	public static final String AUTH_TOKEN = "DS_AUTH_TOKEN";

	public static HttpClient getClient() {
		final HttpClientBuilder clientBuilder = new HttpClientBuilder();
		final Session session = Session.getInstance();

		if((session.getAttribute(Util.USERNAME) != null) && (session.getAttribute(Util.PASSWORD) != null)) {
			clientBuilder.setUsername(String.valueOf(session.getAttribute(Util.USERNAME)));
			clientBuilder.setPassword(String.valueOf(session.getAttribute(Util.PASSWORD).toString()));
		}
		
		clientBuilder.setTrustAllCerts(true);
		return clientBuilder.buildClient();
	}

	public static HttpResponse invokeHttpGetRequest(final Module module, final Operation operation, final String parameters) throws ClientProtocolException, IOException {
		final Session session = Session.getInstance();
		
		String uri = String.valueOf(session.getAttribute(Util.URL)) + "/cli/" + module.getValue() + (operation != null ? ("/" + operation.getValue()) : "");
		if(parameters != null) {
			uri += ("?" + parameters);
		}

		final HttpGet request = new HttpGet(uri);

		if(session.getAttribute(Util.AUTH_TOKEN) != null) {
			request.addHeader("Authorization", "Basic " + Base64.encodeBase64String(("PasswordIsAuthToken:{\"token\":\"" + String.valueOf(session.getAttribute(Util.AUTH_TOKEN)) + "\"}").getBytes()));
		}
		
		final HttpClient client = Util.getClient();
		final HttpResponse response = client.execute(request);

		return response;
	}

	public static HttpResponse invokeHttpPutRequest(final Module module, final Operation operation, final String parameters, final JSONObject json) throws ClientProtocolException, IOException {
		final Session session = Session.getInstance();
		
		String uri = String.valueOf(session.getAttribute(Util.URL)) + "/cli/" + module.getValue() + (operation != null ? ("/" + operation.getValue()) : "");
		if(parameters != null) {
			uri += ("?" + parameters);
		}

		final HttpPut request = new HttpPut(uri);

		if((json != null) && !json.toString().isEmpty()) {
			request.addHeader("content-type", "application/json");
			
			final StringEntity entity = new StringEntity(json.toString());
			request.setEntity(entity);
		}

		if(session.getAttribute(Util.AUTH_TOKEN) != null) {
			request.addHeader("Authorization", "Basic " + Base64.encodeBase64String(("PasswordIsAuthToken:{\"token\":\"" + String.valueOf(session.getAttribute(Util.AUTH_TOKEN)) + "\"}").getBytes()));
		}
		
		final HttpClient client = Util.getClient();
		final HttpResponse response = client.execute(request);

		return response;
	}

	public static JSONObject getJSONObject(final InputStream inputStream) throws IOException, JSONException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder content = new StringBuilder();
		JSONObject result = null;

		String input;
		while((input = reader.readLine()) != null) {
			content.append(input);
		}

		if(!content.toString().isEmpty() && content.toString().startsWith("{")) {
			result = new JSONObject(content.toString());
		}

		return result;
	}

	public static JSONArray getJSONArray(final InputStream inputStream) throws IOException, JSONException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuilder content = new StringBuilder();
		JSONArray result = null;

		String input;
		while((input = reader.readLine()) != null) {
			content.append(input);
		}

		if(!content.toString().isEmpty() && content.toString().startsWith("[{")) {
			result = new JSONArray(content.toString());
		}

		return result;
	}

}
