package com.sycomore.view.moreoption;

import javax.swing.*;
import java.awt.*;

public class GlobalSettingDialog extends JDialog {

    public GlobalSettingDialog(Frame owner) {
        super(owner);
        setTitle("Configuration globale");
        setSize(700, 400);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
    }

    private void init () {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Écoles", new SchoolsPanel());
        tabbedPane.addTab("Sections et options", new JPanel());
        tabbedPane.addTab("Classe d'études", new JPanel());
        tabbedPane.addTab("Catégorisation classe d'étude", new JPanel());

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(b);
    }
}
