package com.pitang.util.log;

import org.apache.log4j.Logger;

public class Log {

	private final Logger logger;

	public Log(final Logger logger) {
		this.logger = logger;
	}

	public void log(final String message, final String... additionalInformation) {
		String msg = ";" + message;

		for(final String info : additionalInformation) {
			msg += ";" + info;
		}

		this.logger.info(msg);
	}
}
