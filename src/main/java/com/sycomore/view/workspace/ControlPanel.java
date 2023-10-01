package com.sycomore.view.workspace;

import com.sycomore.view.componets.Workspace;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel implements Workspace.WorkspaceItem {

    public ControlPanel() {
        setBackground(Color.BLUE);
    }

    @Override
    public String getName() {
        return "control";
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
