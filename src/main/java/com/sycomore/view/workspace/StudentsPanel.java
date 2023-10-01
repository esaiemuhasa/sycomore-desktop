package com.sycomore.view.workspace;

import com.sycomore.view.componets.Workspace;

import javax.swing.*;
import java.awt.*;

public class StudentsPanel extends JPanel implements Workspace.WorkspaceItem {
    public StudentsPanel() {
        setBackground(Color.GREEN);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public String getName() {
        return "students";
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }
}
