package com.marklogic.mgmt;

import com.marklogic.rest.util.RestConfig;

/**
 * Defines the configuration data for talking to the Mgmt REST API. Also includes properties for the admin user, as this
 * user is typically needed for creating an app-specific user (which may depend on app-specific roles and privileges)
 * which is then used for deploying every other resources. If adminUsername and adminPassword are not set, they default
 * to the username/password attribute values.
 */
public class ManageConfig extends RestConfig {

	/**
	 * These are assumed as sensible defaults in a development environment, where teams often use admin/admin for the
	 * admin login. They are of course expected to change in a real environment.
	 */
	public static final String DEFAULT_USERNAME = "admin";
	public static final String DEFAULT_PASSWORD = "admin";

	private String securityUsername;
	private String securityPassword;

	private boolean cleanJsonPayloads = false;

	public ManageConfig() {
		this("localhost", DEFAULT_PASSWORD);
	}

	public ManageConfig(String host, String password) {
		super(host, 8002, DEFAULT_USERNAME, password);
	}

	public ManageConfig(String host, int port, String username, String password) {
		super(host, port, username, password);
		setSecurityUsername(username);
		setSecurityPassword(password);
	}

	@Override
	public String toString() {
		return String.format("[ManageConfig host: %s, port: %d, username: %s, security username: %s]", getHost(),
			getPort(), getUsername(), getSecurityUsername());
	}

	public boolean isCleanJsonPayloads() {
		return cleanJsonPayloads;
	}

	public void setCleanJsonPayloads(boolean cleanJsonPayloads) {
		this.cleanJsonPayloads = cleanJsonPayloads;
	}

	/**
	 * Use getSecurityUsername instead.
	 *
	 * @return
	 */
	@Deprecated
	public String getAdminUsername() {
		return getSecurityUsername();
	}

	/**
	 * Use setSecurityUsername instead.
	 *
	 * @param username
	 */
	@Deprecated
	public void setAdminUsername(String username) {
		setSecurityUsername(username);
	}

	/**
	 * Use getSecurityPassword instead.
	 *
	 * @return
	 */
	@Deprecated
	public String getAdminPassword() {
		return getSecurityPassword();
	}

	/**
	 * Use setSecurityPassword instead.
	 *
	 * @param password
	 */
	@Deprecated
	public void setAdminPassword(String password) {
		setSecurityPassword(password);
	}

	public String getSecurityUsername() {
		return securityUsername;
	}

	public void setSecurityUsername(String securityUsername) {
		this.securityUsername = securityUsername;
	}

	public String getSecurityPassword() {
		return securityPassword;
	}

	public void setSecurityPassword(String securityPassword) {
		this.securityPassword = securityPassword;
	}
}
