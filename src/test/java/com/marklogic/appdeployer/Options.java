package com.marklogic.appdeployer;

import com.beust.jcommander.DynamicParameter;

import java.util.HashMap;
import java.util.Map;

public class Options {

	@DynamicParameter(names = "-P", description = "Dynamic parameters go here")
	private Map<String, String> params = new HashMap<>();

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
