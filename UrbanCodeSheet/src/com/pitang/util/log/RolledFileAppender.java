package com.pitang.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.FileAppender;

public class RolledFileAppender extends FileAppender {

	public RolledFileAppender() {}

	@Override
	public void activateOptions() {

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy.HH-mm-ss");

		final String file = this.getFile();
		final String filePreffix = file.substring(0, file.lastIndexOf("."));
		final String fileSuffix = file.substring(file.lastIndexOf("."));
		final String rolledFile = filePreffix + "." + dateFormat.format(new Date()) + fileSuffix;

		this.setFile(rolledFile);
		super.activateOptions();
	}

}
