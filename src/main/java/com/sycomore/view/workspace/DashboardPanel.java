package com.sycomore.view.workspace;

import com.sycomore.view.componets.Workspace;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel implements Workspace.WorkspaceItem {

    public DashboardPanel() {
        setBackground(Color.BLACK);
    }

    @Override
    public String getName() {
        return "dashboard";
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
