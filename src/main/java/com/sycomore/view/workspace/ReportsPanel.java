package com.sycomore.view.workspace;

import com.sycomore.view.components.Workspace;

import javax.swing.*;

public class ReportsPanel extends JPanel implements Workspace.WorkspaceItem {

    @Override
    public String getName() {
        return "reports";
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }
}
