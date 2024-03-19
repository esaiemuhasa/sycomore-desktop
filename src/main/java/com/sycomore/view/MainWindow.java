package com.sycomore.view;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.ParameterRepository;
import com.sycomore.entity.SchoolYear;
import com.sycomore.entity.helper.Parameter;
import com.sycomore.helper.Config;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.components.Workspace;
import com.sycomore.view.components.navigation.SidebarItem;
import com.sycomore.view.components.navigation.SidebarMoreOptionListener;
import com.sycomore.view.moreoption.GlobalSettingDialog;
import com.sycomore.view.moreoption.SchoolYearDialog;
import com.sycomore.view.workspace.ControlPanel;
import com.sycomore.view.workspace.DashboardPanel;
import com.sycomore.view.workspace.ReportsPanel;
import com.sycomore.view.workspace.StudentsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainWindow extends JFrame {

    private static MainWindow instance;

    private final Sidebar sidebar = new Sidebar();
    private final Workspace workspace = new Workspace();

    private final ParameterRepository parameterRepository;
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

        parameterRepository = DAOFactory.getInstance(ParameterRepository.class);

        Parameter parameter = parameterRepository.findOneByName(Parameter.PARAM_MAIN_WINDOW_INSET);
        if (parameter != null) {
            String [] values = parameter.getValue().split(";");
            boolean maximize = "1".equals(values[4]);
            if (maximize) {
                setState(JFrame.MAXIMIZED_BOTH);
            } else {
                setSize(Integer.parseInt(values[2]), Integer.parseInt(values[3]));
                setLocation(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            }
        } else {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int w = (screen.width / 100) * 80;// 80 % de la largeur l'écran
            int h = (screen.height / 100) * 70;// 70 % de la hauteur de l'écran
            setSize(w, h);
            setLocationRelativeTo(null);
        }

        //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //positionnement des conteneurs principaux de la fenêtre
        JPanel container = (JPanel) getContentPane();
        container.add(sidebar, BorderLayout.WEST);
        container.add(workspace, BorderLayout.CENTER);
    }

    public static MainWindow getInstance () {
        if (instance == null) {
            instance = new MainWindow();
            instance.init();
        }
        return instance;
    }

    public static void updateTitle (String title) {
        MainWindow window = getInstance();
        if (window.dataModel.getYear() == null)
            return;

        window.setTitle(Config.get("app_name")+" - "+window.dataModel.getYear().getLabel()+" | "+title);
    }

    /**
     * Action de demande d'initialisation de la fenêtre principale du soft.
     */
    public static void setup () {
        getInstance();
    }

    /**
     * Action de demande de chargement de la fenêtre de configuration des années scolaires
     */
    private void buildSchoolYearDialog () {
        if (schoolYearDialog != null)
            return;

        schoolYearDialog = new SchoolYearDialog(this);
    }

    /**
     * Action de chargement en memoire de la fenêtre de gestion des configurations globales
     */
    private void buildGlobalSettingDialog () {
        if (globalSettingDialog != null)
            return;

        globalSettingDialog = new GlobalSettingDialog(this);
    }

    /**
     * Action de feedback, lors du changément du ménu active sur la barre de menu principale.
     */
    private void onSidebarItemChange (SidebarItem currentItem, SidebarItem oldItem) {
        updateTitle(currentItem.getItemModel().getCaption());
        workspace.showItem(currentItem.getItemModel().getName());
        if (canSaveNavItem) {
            canSaveNavItem = false;
            EventQueue.invokeLater(this::saveCurrentNavItem);
        }
    }

    /**
     * Action de sauvegarde du menu en cours de consultation.
     */
    private boolean canSaveNavItem = true;
    private synchronized void saveCurrentNavItem () {
        canSaveNavItem = false;
        Parameter parameter = parameterRepository.findOneByName(Parameter.PARAM_MAIN_WINDOW_ACTIVE_MAV_ITEM);
        Date now = new Date();

        if (parameter == null) {
            parameter = new Parameter();
            parameter.setRecordingDate(now);
            parameter.setName(Parameter.PARAM_MAIN_WINDOW_ACTIVE_MAV_ITEM);
        } else {
            parameter.setUpdatingDate(now);
        }

        String current = sidebar.getCurrentItem().getItemModel().getName();
        if (current != null && current.equals(parameter.getValue())) {
            canSaveNavItem = true;
            return;
        }

        parameter.setValue(current);
        parameterRepository.persist(parameter);
        canSaveNavItem = true;
    }

    /**
     * Action d'initialisation de la fenêtre principale.
     */
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
        dataModel.addYearDataListener(dataModelListener);

        Parameter parameter = parameterRepository.findOneByName(Parameter.PARAM_MAIN_WINDOW_ACTIVE_MAV_ITEM);
        if (parameter != null) {
            sidebar.setCurrentItem(parameter.getValue());
        }

        //Lors du changement d'etat de la fenêtre, on sauvegarde certaines informations
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
               if (canSaveWindowLocation) {
                   canSaveWindowLocation = false;
                   EventQueue.invokeLater(MainWindow.this::saveWindowLocation);
               }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (canSaveWindowLocation) {
                    canSaveWindowLocation = false;
                    EventQueue.invokeLater(MainWindow.this::saveWindowLocation);
                }
            }
        });
        //
    }

    /**
     * Sauvegarde la position de la fenêtre sur l'écran de l'utilisateur
     */
    private boolean canSaveWindowLocation = true;
    private synchronized void saveWindowLocation () {
        canSaveWindowLocation = false;
        String value = String.format("%d;%d;%d;%d;%d", getX(), getY(), getWidth(), getHeight(), getState() == JFrame.MAXIMIZED_BOTH ? 1: 0);
        Parameter parameter = parameterRepository.findOneByName(Parameter.PARAM_MAIN_WINDOW_INSET);
        Date now = new Date();
        if (parameter == null) {
            parameter = new Parameter();
            parameter.setRecordingDate(now);
            parameter.setName(Parameter.PARAM_MAIN_WINDOW_INSET);
        } else
            parameter.setUpdatingDate(now);

        if (getState() == JFrame.MAXIMIZED_BOTH && parameter.getId() != null) {
            String[] old = parameter.getValue().split(";");
            value = String.format("%s;%s;%s;%s;%d", old[0], old[1], old[2], old[3], 1);
        }

        parameter.setValue(value);
        parameterRepository.persist(parameter);
        canSaveWindowLocation = true;
    }

    /**
     * Lors la fermeture de l'application, s'il a des ressources à liberer, ce dans cette action que cela est faite.
     */
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
            updateTitle(sidebar.getCurrentItem().getItemModel().getCaption());
        }

        @Override
        public void onLoadFinish() {
            updateTitle(sidebar.getCurrentItem().getItemModel().getCaption());
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
