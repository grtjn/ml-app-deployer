package com.marklogic.appdeployer;

import com.beust.jcommander.JCommander;
import com.marklogic.appdeployer.command.Command;
import com.marklogic.appdeployer.command.CommandMapBuilder;
import com.marklogic.appdeployer.command.databases.DeployDatabaseCommand;
import com.marklogic.appdeployer.command.databases.DeployOtherDatabasesCommand;
import com.marklogic.appdeployer.impl.SimpleAppDeployer;
import com.marklogic.mgmt.ManageClient;
import com.marklogic.mgmt.ManageConfig;
import com.marklogic.mgmt.admin.AdminConfig;
import com.marklogic.mgmt.admin.AdminManager;
import com.marklogic.mgmt.util.SimplePropertySource;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		args = new String[]{
		"-PmlAppName=cli",
		"-PmlContentForestsPerHost=1",
		"-PmlConfigPath=src/test/resources/sample-app/db-only-config"
		};

		Options options = new Options();
		JCommander commander = JCommander
			.newBuilder()
			.addObject(options)
			.acceptUnknownOptions(true)
			.build();

		commander.setCaseSensitiveOptions(false);
		commander.parse(args);

		Map<String, String> params = options.getParams();
		System.out.println(options.getParams());

		ManageClient client = new ManageClient(new ManageConfig());
		AdminManager adminManager = new AdminManager(new AdminConfig());

		CommandMapBuilder commandMapBuilder = new CommandMapBuilder();
		Map<String, List<Command>> commandMap = commandMapBuilder.buildCommandMap();
		List<Command> databaseCommands = commandMap.get("mlDatabaseCommands");
		SimpleAppDeployer deployer = new SimpleAppDeployer(client, adminManager, databaseCommands.toArray(new Command[]{}));

		DefaultAppConfigFactory factory = new DefaultAppConfigFactory(name -> params.get(name));
		AppConfig appConfig = factory.newAppConfig();
		deployer.deploy(appConfig);
	}
}
