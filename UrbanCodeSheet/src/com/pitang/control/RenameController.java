package com.pitang.control;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.codehaus.jettison.json.JSONException;
import com.pitang.business.Component;
import com.pitang.gui.WaitScreen;

public class RenameController extends Controller {
	
	private static RenameController instance;
	
	private RenameController() {
		super();
	}
	
	public static RenameController getInstance() {
		if(RenameController.instance == null) {
			RenameController.instance = new RenameController();
		}
		
		return RenameController.instance;
	}
	
	@Override
	public String execute(final WaitScreen dialog, final File file, final Integer start, final Integer end) throws InvalidFormatException, IOException, IllegalStateException, URISyntaxException, JSONException {
		return this.getMessage();
	}
	
	@Override
	protected void iteration(final Component component) throws InvalidFormatException, IOException, IllegalStateException, URISyntaxException, JSONException {
		// Do nothing!
	}
	
	@Override
	protected String getMessage() {
		return "";
	}
	
}
