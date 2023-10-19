package com.sycomore.view.workspace;

import com.sycomore.view.componets.Workspace;
import com.sycomore.view.workspace.students.Sidebar;
import com.sycomore.view.workspace.students.StudentsDataViewContainer;
import com.sycomore.view.workspace.students.Toolbar;

import javax.swing.*;
import java.awt.*;

public class StudentsPanel extends JPanel implements Workspace.WorkspaceItem {
    public StudentsPanel() {
        super(new BorderLayout());
        JPanel center = new JPanel(new BorderLayout());

        Toolbar toolbar = new Toolbar();
        StudentsDataViewContainer viewContainer = new StudentsDataViewContainer();
        Sidebar sidebar = new Sidebar();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, viewContainer, sidebar);
        split.setDividerSize(10);
        split.setOneTouchExpandable(true);

        center.add(split, BorderLayout.CENTER);

        add(toolbar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
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
