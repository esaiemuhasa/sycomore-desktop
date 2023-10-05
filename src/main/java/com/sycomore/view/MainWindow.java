package com.sycomore.view;

import com.sycomore.entity.SchoolYear;
import com.sycomore.helper.Config;
import com.sycomore.view.componets.TextFieldWrapper;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainWindow extends JFrame {

    private final Sidebar sidebar = new Sidebar();
    private final Workspace workspace = new Workspace();
    private SchoolYearDialog schoolYearDialog;

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

    private void buildSchoolYearDialog () {
        if (schoolYearDialog != null)
            return;

        schoolYearDialog = new SchoolYearDialog(this);
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

        sidebar.setMoreOptionListener(moreOptionListener);
    }


    private final SidebarMoreOptionListener moreOptionListener = new SidebarMoreOptionListener() {
        @Override
        public void doNewYear() {
            buildSchoolYearDialog();

            schoolYearDialog.setVisible(true);
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

    /**
     * Boite de dialogue d'insertion d'une nouvelle année scolaire
     */
    private static final class SchoolYearDialog extends JDialog {

        private final TextFieldWrapper fieldLabel = new TextFieldWrapper("Libellé de l'année scolaire", "");
        private final JButton buttonValidate = new JButton("Valider");
        private final JButton buttonCancel = new JButton("Annuler");

        SchoolYearDialog (MainWindow mainWindow) {
            super(mainWindow, "Nouvelle années scolaire", true);

            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            setLocationRelativeTo(mainWindow);

            JPanel content = new JPanel(new BorderLayout(10, 10));
            JPanel footer = new JPanel();

            footer.add(buttonValidate);
            footer.add(buttonCancel);

            content.add(fieldLabel, BorderLayout.NORTH);
            content.add(footer, BorderLayout.CENTER);
            content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            setContentPane(content);
            pack();
            setSize(300, getHeight());
            setResizable(false);

            listenEvents();
        }

        public void doDispose () {
            setVisible(false);
            dispose();
        }

        private void listenEvents () {
            buttonValidate.addActionListener(event -> {
                SchoolYear year = new SchoolYear();
                year.setArchived(false);
                year.setLabel(fieldLabel.getField().getText().trim());
                year.setRecordingDate(new Date());

                doDispose();
            });
            buttonCancel.addActionListener(event -> doDispose());


            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    buttonCancel.doClick();
                }
            });
        }
    }
}
