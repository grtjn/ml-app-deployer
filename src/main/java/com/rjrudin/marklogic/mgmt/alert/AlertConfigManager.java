package com.rjrudin.marklogic.mgmt.alert;

import com.rjrudin.marklogic.mgmt.AbstractResourceManager;
import com.rjrudin.marklogic.mgmt.ManageClient;
import com.rjrudin.marklogic.mgmt.SaveReceipt;

public class AlertConfigManager extends AbstractResourceManager {

    private String databaseIdOrName;

    public AlertConfigManager(ManageClient client, String databaseIdOrName) {
        super(client);
        this.databaseIdOrName = databaseIdOrName;
    }

    @Override
    public String getResourcesPath() {
        return format("/manage/v2/databases/%s/alert/configs", databaseIdOrName);
    }

    @Override
    public String getResourcePath(String resourceNameOrId) {
        return getResourcesPath();
    }

    @Override
    protected String[] getUpdateResourceParams(String payload) {
        return new String[] { "uri", payloadParser.getPayloadFieldValue(payload, "uri") };
    }

    @Override
    protected String getIdFieldName() {
        return "uri";
    }

    /**
     * This addresses a bug in ML 8.0-4 (36550) where the domains are not saved when an alert config is created, but
     * they are saved when the config is updated. So when the config is first created, we immediately update it to
     * ensure that the domains are setup correctly.
     */
    @Override
    public SaveReceipt save(String payload) {
        SaveReceipt receipt = super.save(payload);
        if (receipt.hasLocationHeader()) {
            if (logger.isInfoEnabled()) {
                logger.info("Immediately updating alert config after it's been created to ensure that CPF domains are set");
            }
            super.save(payload);
        }
        return receipt;
    }
}