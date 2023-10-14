package com.sycomore.view.moreoption;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.LevelRepository;
import com.sycomore.entity.Classifiable;
import com.sycomore.entity.Level;
import com.sycomore.model.LevelTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class LevelsPanel extends JPanel {

    private final JPopupMenu menu = new JPopupMenu();
    private final JMenuItem itemDelete = new JMenuItem("Supprimer");
    private final JMenuItem itemUpdate = new JMenuItem("Modifier");

    private final LevelTableModel tableModel = new LevelTableModel();
    private final JTable table = new JTable(tableModel);
    private final JButton buttonAddingLevel = new JButton("Ajouter une section");
    private final LevelRepository levelRepository;
    private JDialog dialog;
    private ClassifiableForm<Level> classifiableForm;

    public LevelsPanel() {
        super(new BorderLayout());

        levelRepository = DAOFactory.getInstance(LevelRepository.class);

        init();

        tableModel.init();
    }

    private void buildDialog () {
        if (dialog != null)
            return;

        dialog = new JDialog();
        classifiableForm = new ClassifiableForm<>(formListener);
        dialog.setContentPane(classifiableForm);
        dialog.pack();
        dialog.setSize(dialog.getWidth() + 200, dialog.getHeight());
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void init () {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        header.add(buttonAddingLevel);

        add(header, BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);

        menu.add(itemUpdate);
        menu.add(itemDelete);

        buttonAddingLevel.addActionListener(e -> {
            buildDialog();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        itemUpdate.addActionListener(e -> handleUpdate());
        itemDelete.addActionListener(e -> handleDelete());

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() != -1)
                    menu.show(table, e.getX(), e.getY());
            }
        });
    }

    private Level getSelectedItem () {
        return tableModel.getRow(table.getSelectedRow());
    }

    private void handleDelete () {
        Level level = getSelectedItem();
        int status = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer \n "+level, "Suppression", JOptionPane.OK_CANCEL_OPTION);

        if (status == JOptionPane.OK_OPTION) {
            levelRepository.remove(level);
        }
    }
    private void handleUpdate () {

        Level level = getSelectedItem();

        buildDialog();
        classifiableForm.setData(level);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private final ClassifiableForm.ClassifiableFormListener formListener = new ClassifiableForm.ClassifiableFormListener() {
        @Override
        public void onValidate(String shortName, String fullName, Classifiable data) {

            dialog.setVisible(false);
            dialog.dispose();

            Level level = (Level) data;
            if (data == null)
                level = new Level();

            level.setShortName(shortName);
            level.setFullName(fullName);

            if (level.getRecordingDate() == null)
                level.setRecordingDate(new Date());
            else
                level.setUpdatingDate(new Date());

            levelRepository.persist(level);
        }

        @Override
        public void onCancel() {
            dialog.setVisible(false);
            dialog.dispose();
        }
    };
}
