package com.sycomore.view;

import com.sycomore.entity.SchoolYear;
import com.sycomore.helper.Config;
import com.sycomore.view.componets.Workspace;
import com.sycomore.view.componets.navigation.SidebarItem;
import com.sycomore.view.componets.navigation.SidebarMoreOptionListener;
import com.sycomore.view.workspace.ControlPanel;
import com.sycomore.view.workspace.DashboardPanel;
import com.sycomore.view.workspace.ReportsPanel;
import com.sycomore.view.workspace.StudentsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {

    private final Sidebar sidebar = new Sidebar();
    private final Workspace workspace = new Workspace();

    public MainWindow ()  {
        super(Config.get("app_name"));

        try {
            setIconImage(ImageIO.read(new File("icons/logo.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
                .addItem("Rapports", "printing.png", "reports")
                .addItem("Configuration", "control.png", "control");

        sidebar.setItemChangeListener(this::onSidebarItemChange);

        workspace.addItem(new DashboardPanel())
                .addItem(new StudentsPanel())
                .addItem(new ReportsPanel())
                .addItem(new ControlPanel());
    }


    private SidebarMoreOptionListener moreOptionListener = new SidebarMoreOptionListener() {
        @Override
        public void doNewYear() {

        }

        @Override
        public void doLogout() {

        }

        @Override
        public void doLoadYear(SchoolYear year) {

        }

        @Override
        public void doOpenOptions() {

        }
    };
}
