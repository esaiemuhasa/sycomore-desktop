package com.sycomore.view;

import com.sycomore.entity.SchoolYear;
import com.sycomore.helper.Config;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.componets.TextFieldWrapper;
import com.sycomore.view.componets.Workspace;
import com.sycomore.view.componets.navigation.SidebarItem;
import com.sycomore.view.componets.navigation.SidebarMoreOptionListener;
import com.sycomore.view.moreoption.GlobalSettingDialog;
import com.sycomore.view.moreoption.SchoolYearDialog;
import com.sycomore.view.workspace.ControlPanel;
import com.sycomore.view.workspace.DashboardPanel;
import com.sycomore.view.workspace.ReportsPanel;
import com.sycomore.view.workspace.StudentsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainWindow extends JFrame {

    private static MainWindow instance;

    private final Sidebar sidebar = new Sidebar();
    private final Workspace workspace = new Workspace();

    private final YearDataModel dataModel = YearDataModel.getInstance();
    private SchoolYearDialog schoolYearDialog;
    private GlobalSettingDialog globalSettingDialog;

    private MainWindow ()  {
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

        dataModel.addYearDataListener(dataModelListener);
    }

    public static MainWindow getInstance () {
        if (instance == null)
            instance = new MainWindow();
        return instance;
    }

    public static void updateTitle (String title) {
        MainWindow window = getInstance();
        window.setTitle(Config.get("app_name")+" - "+window.dataModel.getYear().getLabel()+" | "+title);
    }

    public static void setup () {
        getInstance();
    }

    private void buildSchoolYearDialog () {
        if (schoolYearDialog != null)
            return;

        schoolYearDialog = new SchoolYearDialog(this);
    }

    private void buildGlobalSettingDialog () {
        if (globalSettingDialog != null)
            return;

        globalSettingDialog = new GlobalSettingDialog(this);
    }

    private void onSidebarItemChange (SidebarItem currentItem, SidebarItem oldItem) {
        updateTitle(currentItem.getItemModel().getCaption());
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

        sidebar.setMoreOptionListener(moreOptionListener);
    }

    private void doClosing () {
        dataModel.removeYearDataListener(dataModelListener);
    }

    private final YearDataModelListener dataModelListener = new YearDataModelAdapter() {
        @Override
        public void onSetup() {
            updateTitle(sidebar.getCurrentItem().getItemModel().getCaption());
        }

        @Override
        public void onLoadStart() {
            setTitle(Config.get("app_name")+" - "+dataModel.getYear().getLabel());
        }

        @Override
        public void onLoadFinish() {
            setTitle(Config.get("app_name")+" - "+dataModel.getYear().getLabel());
        }
    };


    private final SidebarMoreOptionListener moreOptionListener = new SidebarMoreOptionListener() {
        @Override
        public void doNewYear() {
            buildSchoolYearDialog();

            schoolYearDialog.setVisible(true);
        }

        @Override
        public void doLogout() {}

        @Override
        public void doLoadYear(SchoolYear year) {
            setTitle(Config.get("app_name")+" - "+year.getLabel());
        }

        @Override
        public void doOpenOptions() {
            buildGlobalSettingDialog();

            globalSettingDialog.setVisible(true);
        }
    };
}
