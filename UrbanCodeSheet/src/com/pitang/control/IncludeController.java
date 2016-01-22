package com.pitang.control;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.pitang.business.Component;
import com.pitang.business.Environment;
import com.pitang.business.Property;
import com.pitang.business.TagColor;
import com.pitang.util.Util;
import com.pitang.util.log.Log;
import com.pitang.util.rest.Module;
import com.pitang.util.rest.Operation;

public class IncludeController extends Controller {

	private static IncludeController instance;
	
	private static Log logOK = new Log(Logger.getLogger("FILE_OK"));

	private static Log logNOK = new Log(Logger.getLogger("FILE_NOK"));

	private IncludeController() {
		super();
	}

	public static IncludeController getInstance() {
		if(IncludeController.instance == null) {
			IncludeController.instance = new IncludeController();
		}

		return IncludeController.instance;
	}
	
	@Override
	protected void iteration(final Component component) throws InvalidFormatException, IOException, IllegalStateException, URISyntaxException, JSONException {
		JSONObject response = null;

		// Consulta a Aplicação
		response = Util.getJSONObject(Util.invokeHttpGetRequest(Module.application, Operation.info, "application=" + component.getApplication()).getEntity().getContent());
		
		// Verifica a existência da Aplicação
		if(response == null) {
			IncludeController.createApplication(component);
		} else {
			IncludeController.logNOK.log(component.getApplication(), "Aplicação já cadastrada!");
		}

		for(final Entry<String, Environment> environment : component.getPool().getEnvironments().entrySet()) {
			// Consulta o Ambiente
			response = Util.getJSONObject(Util.invokeHttpGetRequest(Module.environment, Operation.info, "application=" + component.getApplication() + "&environment=" + environment.getValue().getName()).getEntity().getContent());
			
			// Verifica a existência do Ambiente
			if(response == null) {
				IncludeController.createEnvironment(component.getApplication(), environment.getValue().getName(), component.getPool().getName(), TagColor.fromType(environment.getValue().getName()));
				
				for(final Entry<String, Property> property : environment.getValue().getProperties().entrySet()) {
					IncludeController.setPropertyOnEnvironment(component.getApplication(), environment.getValue().getName(), property.getValue().getMetaProperty().getName(), property.getValue().getValue());
				}
			}
		}
		
		// Consulta o Componente
		response = Util.getJSONObject(Util.invokeHttpGetRequest(Module.component, Operation.info, "component=" + component.getName()).getEntity().getContent());
		
		// Verifica a existência do Componente
		if(response == null) {
			IncludeController.createComponent(component);

			for(final Entry<String, Property> property : component.getProperties().entrySet()) {
				IncludeController.setPropertyOnComponent(component.getName(), property.getValue().getMetaProperty().getName(), property.getValue().getValue());
			}
			
			IncludeController.logOK.log(component.getName());
		} else {
			IncludeController.logNOK.log(component.getName(), "Componente já cadastrado!");
		}
	}
	
	@Override
	protected String getMessage() {
		return "Veja os arquivos na pasta log para verificar os componentes/aplicações cadastrados com sucesso e os que falharam.";
	}
	
	private static void createComponent(final Component component) throws JSONException, ClientProtocolException, IOException {
		final JSONObject json = new JSONObject();
		json.put("name", component.getName());
		json.put("description", component.getAcronym());
		json.put("templateName", component.getTemplate().getName());
		json.put("useVfs", "true");
		
		Util.invokeHttpPutRequest(Module.component, Operation.create, null, json);
		
		IncludeController.addTagToComponent(component.getName(), component.getAcronym(), TagColor.APP);
		IncludeController.addTagToComponent(component.getName(), component.getPool().getName(), TagColor.POOL);

		IncludeController.addComponentToTeam(component.getName(), "");

		IncludeController.addComponentToApplication(component.getName(), component.getApplication());
	}

	private static void addTagToComponent(final String component, final String tag, final TagColor color) throws ClientProtocolException, IOException {
		String parameters = "component=" + component + "&tag=" + tag;

		if(color != null) {
			parameters += "&color=" + color.getHex();
		}

		Util.invokeHttpPutRequest(Module.component, Operation.tag, parameters, null);
	}

	private static void addComponentToTeam(final String component, final String team) throws ClientProtocolException, IOException {
		final String parameters = "component=" + component + "&team=" + team;
		Util.invokeHttpPutRequest(Module.component, Operation.teams, parameters, null);
	}

	private static void addComponentToApplication(final String component, final String application) throws ClientProtocolException, IOException {
		final String parameters = "component=" + component + "&application=" + application;
		Util.invokeHttpPutRequest(Module.application, Operation.addComponentToApp, parameters, null);
	}
	
	private static void setPropertyOnComponent(final String component, final String name, final String value) throws ClientProtocolException, IOException {
		final String parameters = "component=" + component + "&name=" + name + "&value=" + URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		Util.invokeHttpPutRequest(Module.component, Operation.propValue, parameters, null);
	}
	
	private static void createApplication(final Component component) throws JSONException, ClientProtocolException, IOException {
		final JSONObject json = new JSONObject();
		json.put("name", component.getApplication());
		json.put("description", component.getAcronym());
		json.put("templateName", component.getTemplate().getName());

		Util.invokeHttpPutRequest(Module.application, Operation.create, null, json);

		IncludeController.addTagToApplication(component.getApplication(), component.getAcronym(), TagColor.APP);
		
		IncludeController.addApplicationToTeam(component.getApplication(), "");
	}

	private static void addTagToApplication(final String application, final String tag, final TagColor color) throws ClientProtocolException, IOException {
		String parameters = "application=" + application + "&tag=" + tag;

		if(color != null) {
			parameters += "&color=" + color.getHex();
		}

		Util.invokeHttpPutRequest(Module.application, Operation.tag, parameters, null);
	}

	private static void addApplicationToTeam(final String application, final String team) throws ClientProtocolException, IOException {
		final String parameters = "application=" + application + "&team=" + team;
		Util.invokeHttpPutRequest(Module.application, Operation.teams, parameters, null);
	}
	
	private static void createEnvironment(final String application, final String name, final String pool, final TagColor color) throws ClientProtocolException, IOException {
		final String parameters = "application=" + application + "&name=" + name + "&color=" + URLEncoder.encode("#", StandardCharsets.UTF_8.toString()) + color.getHex();
		Util.invokeHttpPutRequest(Module.environment, Operation.createEnvironment, parameters, null);

		IncludeController.addBaseResourceToEnvironment(application, name, pool);
		IncludeController.addEnvironmentToTeam(application, name, "");
	}
	
	private static void addBaseResourceToEnvironment(final String application, final String environment, final String pool) throws ClientProtocolException, IOException {
		final String parameters = "application=" + application + "&environment=" + environment + "&resource=/" + environment + "/" + pool;
		Util.invokeHttpPutRequest(Module.environment, Operation.addBaseResource, parameters, null);
	}

	private static void addEnvironmentToTeam(final String application, final String environment, final String team) throws ClientProtocolException, IOException {
		final String parameters = "application=" + application + "&environment=" + environment + "&team=" + team;
		Util.invokeHttpPutRequest(Module.environment, Operation.teams, parameters, null);
	}
	
	private static void setPropertyOnEnvironment(final String application, final String environment, final String name, final String value) throws ClientProtocolException, IOException {
		final String parameters = "application=" + application + "&environment=" + environment + "&name=" + name + "&value=" + URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		Util.invokeHttpPutRequest(Module.environment, Operation.propValue, parameters, null);
	}

}
