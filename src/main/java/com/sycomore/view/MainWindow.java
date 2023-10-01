package com.sycomore.view;

import com.sycomore.view.componets.Workspace;
import com.sycomore.view.componets.navigation.SidebarItem;
import com.sycomore.view.workspace.ControlPanel;
import com.sycomore.view.workspace.DashboardPanel;
import com.sycomore.view.workspace.StudentsPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final Sidebar sidebar = new Sidebar();
    private final Workspace workspace = new Workspace();

    public MainWindow ()  {
        super("");

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (screen.width / 100) * 80;// 80 % de la largeur l'écran
        int h = (screen.height / 100) * 70;// 70 % de la hauteur de l'écran

        setSize(w, h);
        //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //
        JPanel container = (JPanel) getContentPane();
        container.add(sidebar, BorderLayout.WEST);
        container.add(workspace, BorderLayout.CENTER);

        init();
    }

    private void onSidebarItemChange (SidebarItem currentItem, SidebarItem oldItem) {
        workspace.showItem(currentItem.getItemModel().getName());
    }

    private void init () {
        sidebar.addItem("états", "dashboard.png", "dashboard")
                .addItem("Élèves", "student.png", "students")
                .addItem("Configuration", "control.png", "control");

        sidebar.setItemChangeListener(this::onSidebarItemChange);

        workspace.addItem(new DashboardPanel())
                .addItem(new StudentsPanel())
                .addItem(new ControlPanel());
    }
}
