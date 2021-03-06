package com.marklogic.appdeployer.command.appservers;

import com.marklogic.appdeployer.AppConfig;
import com.marklogic.appdeployer.ConfigDir;
import com.marklogic.appdeployer.command.AbstractCommand;
import com.marklogic.appdeployer.command.CommandContext;
import com.marklogic.appdeployer.command.SortOrderConstants;
import com.marklogic.mgmt.PayloadParser;
import com.marklogic.mgmt.SaveReceipt;
import com.marklogic.mgmt.resource.ResourceManager;
import com.marklogic.mgmt.resource.appservers.ServerManager;

import java.io.File;

/**
 * Command for updating an existing REST API server that was presumably created via /v1/rest-apis.
 */
public class UpdateRestApiServersCommand extends AbstractCommand {

	private String restApiFilename;

	public UpdateRestApiServersCommand() {
		setExecuteSortOrder(SortOrderConstants.UPDATE_REST_API_SERVERS);
	}

	public UpdateRestApiServersCommand(String restApiFilename) {
		this();
		this.restApiFilename = restApiFilename;
	}

	/**
	 * This uses a different file than that of creating a REST API, as the payload for /v1/rest-apis differs from that
	 * of the /manage/v2/servers endpoint.
	 */
	@Override
	public void execute(CommandContext context) {
		File f = findRestApiConfigFile(context);
		if (f != null && f.exists()) {
			AppConfig appConfig = context.getAppConfig();

			ServerManager mgr = new ServerManager(context.getManageClient(), appConfig.getGroupName());

			saveResource(mgr, context, f);

			if (appConfig.isTestPortSet()) {
				String payload = copyFileToString(f);
				payload = payloadTokenReplacer.replaceTokens(payload, appConfig, true);
				payload = adjustPayloadBeforeSavingResource(mgr, context, f, payload);
				mgr.save(payload);
			}
		}
	}

	@Override
	protected String adjustPayloadBeforeSavingResource(ResourceManager mgr, CommandContext context, File f, String payload) {
		payload = super.adjustPayloadBeforeSavingResource(mgr, context, f, payload);
		if (payload != null) {
			String urlRewriter = new PayloadParser().getPayloadFieldValue(payload, "url-rewriter", false);
			if (urlRewriter != null && !urlRewriter.contains("rest-api")) {
				logger.warn("The REST server is being updated with a url-rewriter that does not contain the string 'rest-api' in its URI. " +
					"This may result in a 'Port is in use' exception if the command for creating a REST server is run. " +
					"This is because the command to create a REST server first sends a GET to /v1/rest-apis, and that endpoint " +
					"only returns servers that have the string 'rest-api' in the URI of the url-rewriter. It is recommended to " +
					"include the string 'rest-api' in the URI of a url-rewriter for a REST server to avoid this problem.");
			}
		}
		return payload;
	}

	protected File findRestApiConfigFile(CommandContext context) {
		if (restApiFilename != null) {
			File f = null;
			for (ConfigDir configDir : context.getAppConfig().getConfigDirs()) {
				File tmpFile = new File(configDir.getBaseDir(), restApiFilename);
				if (tmpFile.exists()) {
					f = tmpFile;
					if (logger.isInfoEnabled()) {
						logger.info("Found REST API configuration file at: " + f.getAbsolutePath());
					} else if (logger.isInfoEnabled()) {
						logger.info("Did not find REST API configuration file at: " + tmpFile.getAbsolutePath());
					}
				}
			}
			return f;
		} else {
			File f = null;
			for (ConfigDir configDir : context.getAppConfig().getConfigDirs()) {
				File tmpFile = configDir.getRestApiServerFile();
				if (tmpFile.exists()) {
					f = tmpFile;
					if (logger.isInfoEnabled()) {
						logger.info("Found REST API configuration file at: " + f.getAbsolutePath());
					}
				} else if (logger.isInfoEnabled()) {
					logger.info("Did not find REST API configuration file at: " + tmpFile.getAbsolutePath());
				}
			}
			return f;
		}
	}
}
