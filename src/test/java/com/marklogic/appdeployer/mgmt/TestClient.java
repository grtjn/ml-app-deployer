package com.marklogic.appdeployer.mgmt;

import java.io.File;

import com.marklogic.appdeployer.AppConfig;

public class TestClient {

    public static void main(String[] args) throws Exception {

        // Define how to connect to the ML manage API - this defaults to localhost/8002/admin/admin
        ManageConfig manageConfig = new ManageConfig();

        // Build a client for talking to the ML manage API - this wraps an instance of Spring RestTemplate with some
        // convenience methods for talking to the manage API
        ManageClient client = new ManageClient(manageConfig);

        // Define where configuration files for the ML app are - defaults to src/main/ml-config
        ConfigDir configDir = new ConfigDir(new File("src/test/resources/sample-app/src/main/ml-config"));

        // Build a ConfigManager with all the fun methods
        ConfigManager configMgr = new ConfigManager(client);

        // Define app configuration
        AppConfig appConfig = new AppConfig();
        appConfig.setName("sample-app");
        appConfig.setRestPort(8100);
        appConfig.setTestRestPort(8101);

        // Now start calling fun methods that get things done
        configMgr.createRestApi(configDir, appConfig);

        // configMgr.uninstallApp(appConfig, true, true);

        System.out.println("All done!");
    }
}
