package com.marklogic.appdeployer.command.security;

import com.marklogic.appdeployer.command.AbstractResourceCommand;
import com.marklogic.appdeployer.command.CommandContext;
import com.marklogic.appdeployer.command.SortOrderConstants;
import com.marklogic.mgmt.ResourceManager;
import com.marklogic.mgmt.security.PrivilegeManager;

import java.io.File;

public class DeployPrivilegesCommand extends AbstractResourceCommand {

    public DeployPrivilegesCommand() {
        setExecuteSortOrder(SortOrderConstants.DEPLOY_PRIVILEGES);
        setUndoSortOrder(SortOrderConstants.DELETE_PRIVILEGES);
    }

    @Override
    protected File[] getResourceDirs(CommandContext context) {
        return new File[] { new File(context.getAppConfig().getConfigDir().getSecurityDir(), "privileges") };
    }

    @Override
    protected ResourceManager getResourceManager(CommandContext context) {
        return new PrivilegeManager(context.getManageClient());
    }

}
