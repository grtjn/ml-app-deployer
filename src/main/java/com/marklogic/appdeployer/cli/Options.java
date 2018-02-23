package com.marklogic.appdeployer.cli;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;

import java.util.HashMap;
import java.util.Map;

public class Options {

	@DynamicParameter(names = "-P", description = "Use this argument to include any property defined by the ml-gradle Property Reference; e.g. -PmlAppName=example")
	private Map<String, String> params = new HashMap<>();

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
