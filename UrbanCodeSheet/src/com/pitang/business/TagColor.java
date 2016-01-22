package com.pitang.business;

public enum TagColor {

	DES("DES", "D9182D"), QA("QA", "FFCF01"), UAT("UAT", "00B2EF"), PRD("PRD", "17AF4B"), APP("APP", "AB1A86"), POOL("POOL", "404041");

	private String type;
	
	private String hex;

	private TagColor(final String type, final String hex) {
		this.type = type;
		this.hex = hex;
	}

	public String getType() {
		return this.type;
	}

	public String getHex() {
		return this.hex;
	}

	public static TagColor fromType(final String type) {
		TagColor result = null;
		
		for(final TagColor value : TagColor.values()) {
			if(value.getType().equals(type)) {
				result = value;
			}
		}
		
		return result;
	}
}
