package com.sycomore.view.workspace;

import com.sycomore.view.componets.Workspace;
import com.sycomore.view.workspace.control.PromotionsPanel;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel implements Workspace.WorkspaceItem {

    public ControlPanel() {
        super(new BorderLayout());

        JTabbedPane pane = new JTabbedPane();

        pane.addTab("Promotions", new PromotionsPanel());
        pane.addTab("Frais d'étude", new JPanel());
        pane.addTab("Frais connexe", new JPanel());
        add(pane, BorderLayout.CENTER);
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
