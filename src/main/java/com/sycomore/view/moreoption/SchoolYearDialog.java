package com.sycomore.view.moreoption;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.dao.SchoolYearRepository;
import com.sycomore.entity.SchoolYear;
import com.sycomore.view.MainWindow;
import com.sycomore.view.componets.TextFieldWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

/**
 * Boite de dialogue d'insertion d'une nouvelle année scolaire
 */
public class SchoolYearDialog extends JDialog {
    private final TextFieldWrapper fieldLabel = new TextFieldWrapper("Libellé de l'année scolaire", "");
    private final JButton buttonValidate = new JButton("Valider");
    private final JButton buttonCancel = new JButton("Annuler");

    private final SchoolYearRepository yearRepository;

    public SchoolYearDialog (MainWindow mainWindow) {
        super(mainWindow, "Nouvelle années scolaire", true);

        yearRepository = DAOFactory.getInstance(SchoolYearRepository.class);
        yearRepository.addRepositoryListener(new RepositoryAdapter<SchoolYear>() {
            @Override
            public void onCreate(SchoolYear year) {
                doDispose();
            }

            @Override
            public void onUpdate(SchoolYear oldState, SchoolYear newState) {
                doDispose();
            }
        });

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
        fieldLabel.getField().setText("");
        dispose();
    }

    private void listenEvents () {
        buttonValidate.addActionListener(event -> {
            SchoolYear year = new SchoolYear();
            year.setArchived(false);
            year.setLabel(fieldLabel.getField().getText().trim());
            year.setRecordingDate(new Date());

            yearRepository.persist(year);
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
