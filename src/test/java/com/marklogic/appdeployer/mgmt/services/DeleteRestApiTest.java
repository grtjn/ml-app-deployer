package com.marklogic.appdeployer.mgmt.services;

import org.junit.Before;
import org.junit.Test;

import com.marklogic.appdeployer.AbstractMgmtTest;
import com.marklogic.appdeployer.mgmt.appservers.ServerManager;

/**
 * This test ensures that the convenience methods for creating and deleting a sample application work properly, and thus
 * they can be used in other tests that depend on having an app in place.
 */
public class DeleteRestApiTest extends AbstractMgmtTest {

    @Before
    public void setup() {
        initializeAppManager();
    }

    @Test
    public void createAndDelete() {
        ServiceManager mgr = new ServiceManager(manageClient);
        ServerManager serverMgr = new ServerManager(manageClient);

        createSampleAppRestApi();
        assertTrue("The REST API server should exist", mgr.restApiServerExists(SAMPLE_APP_NAME));
        assertTrue("The REST API app server should exist", serverMgr.serverExists(SAMPLE_APP_NAME));

        deleteSampleApp();
        assertFalse("The REST API server should have been deleted", mgr.restApiServerExists(SAMPLE_APP_NAME));
        assertFalse("The REST API app server have been deleted", serverMgr.serverExists(SAMPLE_APP_NAME));
    }
}