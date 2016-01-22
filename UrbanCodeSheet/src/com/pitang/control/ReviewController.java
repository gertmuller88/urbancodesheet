package com.pitang.control;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.pitang.business.Component;
import com.pitang.gui.WaitScreen;
import com.pitang.util.Util;
import com.pitang.util.log.Log;
import com.pitang.util.rest.Module;
import com.pitang.util.rest.Operation;

public class ReviewController extends Controller {

	private static ReviewController instance;

	private static Log logOK = new Log(Logger.getLogger("FILE_OK"));

	private static Log logTODO = new Log(Logger.getLogger("FILE_TODO"));
	
	private static Log logNOK = new Log(Logger.getLogger("FILE_NOK"));

	private ReviewController() {
		super();
	}

	public static ReviewController getInstance() {
		if(ReviewController.instance == null) {
			ReviewController.instance = new ReviewController();
		}
		
		return ReviewController.instance;
	}

	@Override
	protected void iteration(final Component component) throws InvalidFormatException, IOException, IllegalStateException, URISyntaxException, JSONException {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	protected String getMessage() {
		return "Veja os arquivos na pasta log para verificar os componentes cadastrados com sucesso e os que falharam.";
	}
	
	protected static void reviewComponents(final WaitScreen dialog, final Workbook workbook, final Integer start, final Integer end, final Map<String, Component> components) throws ClientProtocolException, IOException, IllegalStateException, JSONException {
		final Sheet sheet = workbook.getSheet("Componentes");

		for(int i = (start >= 2 ? start - 1 : 1); i < end; i++ ) {
			final Row row = sheet.getRow(i);

			if(row != null) {
				final String componentName = (row.getCell(0) != null) ? row.getCell(0).getStringCellValue().trim() : null;

				if((componentName != null) && !componentName.isEmpty()) {
					final HttpResponse response = Util.invokeHttpGetRequest(Module.component, Operation.info, "component=" + componentName);
					final JSONObject component = Util.getJSONObject(response.getEntity().getContent());

					final String applicationName = (row.getCell(1) != null) ? row.getCell(1).getStringCellValue() : null;
					
					if(component != null) {
						if(component.getBoolean("useVfs") == false) {
							ReviewController.logTODO.log(componentName, "Marcar propriedade \"Copy to CodeStation\".");
						}
						
						final HttpResponse resp1 = Util.invokeHttpGetRequest(Module.application, Operation.componentsInApplication, "application=" + applicationName);
						final JSONArray componentsInApplication = Util.getJSONArray(resp1.getEntity().getContent());
						
						if(componentsInApplication != null) {
							for(int j = 0; i < componentsInApplication.length(); j++ ) {
								final JSONObject compInApp = componentsInApplication.getJSONObject(j);
								
								if(compInApp.getString("name").equalsIgnoreCase(componentName)) {
									System.out.println("Não está associado à Aplicação " + applicationName);
									break;
								}
							}
						}
					} else {
						final String template = (row.getCell(2) != null) ? row.getCell(2).getStringCellValue() : null;
						final String[] environments = (row.getCell(3) != null) ? row.getCell(3).getStringCellValue().split(";") : new String[] {};
						final String[] properties = (row.getCell(4) != null) ? row.getCell(4).getStringCellValue().split(";") : new String[] {};

						ReviewController.logOK.log(componentName, "\"Copy to CodeStation\" desmarcado");
						ReviewController.logTODO.log(componentName, "\"Copy to CodeStation\" desmarcado");
						ReviewController.logNOK.log(componentName, "\"Copy to CodeStation\" desmarcado");
						System.out.println(template + environments + properties);
					}

				}
			}

			dialog.incrementProgressValue();
		}
	}

}
