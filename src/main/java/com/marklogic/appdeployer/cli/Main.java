package com.marklogic.appdeployer.cli;

import com.beust.jcommander.JCommander;
import com.marklogic.appdeployer.AppConfig;
import com.marklogic.appdeployer.DefaultAppConfigFactory;
import com.marklogic.appdeployer.command.Command;
import com.marklogic.appdeployer.command.CommandMapBuilder;
import com.marklogic.appdeployer.impl.SimpleAppDeployer;
import com.marklogic.mgmt.ManageClient;
import com.marklogic.mgmt.ManageConfig;
import com.marklogic.mgmt.admin.AdminConfig;
import com.marklogic.mgmt.admin.AdminManager;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[]{
			"-PmlAppName=cli",
			"-PmlContentForestsPerHost=1",
			"-PmlConfigPath=src/test/resources/sample-app/db-only-config",
			"mlDeployContentDatabases"
		};

		CommandMapBuilder commandMapBuilder = new CommandMapBuilder();
		Map<String, List<Command>> commandMap = commandMapBuilder.buildCommandMap();

		Options options = new Options();

		JCommander.Builder builder = JCommander
			.newBuilder()
			.addObject(options)
			.acceptUnknownOptions(true);

		// Combine all commands into an ordered map
		Map<String, CommandArray> orderedCommandMap = new TreeMap<>();
		orderedCommandMap.put("mlDeploy", new DeployCommand(commandMap));

		for (String commandGroup : commandMap.keySet()) {
			for (Command command : commandMap.get(commandGroup)) {
				String className = command.getClass().getSimpleName();
				if (className.endsWith("Command")) {
					className = className.substring(0, className.length() - "Command".length());
				}
				orderedCommandMap.put("ml" + className, new CommandWrapper(command));
			}
		}

		for (String commandName : orderedCommandMap.keySet()) {
			builder.addCommand(commandName, orderedCommandMap.get(commandName));
		}

		JCommander commander = builder.build();
		commander.setProgramName("java -jar deployer.jar");
		commander.setCaseSensitiveOptions(false);
		commander.parse(args);

		String parsedCommand = commander.getParsedCommand();
		if (parsedCommand == null) {
			commander.usage();
		} else {
			JCommander parsedCommander = commander.getCommands().get(parsedCommand);
			CommandArray commandArray = (CommandArray) parsedCommander.getObjects().get(0);

			// TODO Build these via factory methods
			ManageClient client = new ManageClient(new ManageConfig());
			AdminManager adminManager = new AdminManager(new AdminConfig());

			Map<String, String> params = options.getParams();
			DefaultAppConfigFactory factory = new DefaultAppConfigFactory(name -> params.get(name));
			AppConfig appConfig = factory.newAppConfig();

			SimpleAppDeployer deployer = new SimpleAppDeployer(client, adminManager, commandArray.getCommands());
			deployer.deploy(appConfig);
		}
	}
}
