package com.sycomore.view.moreoption;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.SchoolRepository;
import com.sycomore.entity.School;
import com.sycomore.model.SchoolTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

public class SchoolsPanel extends JPanel {

    private final SchoolTableModel tableModel = new SchoolTableModel();
    private final JTable table = new JTable(tableModel);
    private final JPopupMenu popup = new JPopupMenu();
    private final JMenuItem update = new JMenuItem("Modifier");
    private final JMenuItem delete = new JMenuItem("Supprimer");
    private final JButton buttonAdding = new JButton("Ajouter");

    private JDialog dialog;//boite de dialogue de manipulation des infos d'une école
    private SchoolForm schoolForm;

    private final SchoolRepository schoolRepository;
    public SchoolsPanel () {
        super(new BorderLayout(10, 10));
        schoolRepository = DAOFactory.getInstance(SchoolRepository.class);

        init();
        tableModel.init();
    }

    private void buildDialog () {
        if (dialog != null)
            return;

        dialog = new JDialog();
        schoolForm = new SchoolForm(formListener);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(schoolForm);
        dialog.pack();
        dialog.setSize(dialog.getWidth() + 200, dialog.getHeight()+10);
        dialog.setResizable(false);
    }

    private void init () {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel center = new JPanel(new BorderLayout());

        top.add(buttonAdding);
        center.add(table);
        center.setBorder(BorderFactory.createLineBorder(UIManager.getColor("border_color")));

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(mouseListener);
        buttonAdding.addActionListener(event -> doAdding());

        //popup menu
        popup.add(update);
        popup.add(delete);
        //==

        update.addActionListener(e -> doUpdating());
        delete.addActionListener(e -> doDelete());
    }

    private School getSelectedItem () {
        return tableModel.getRow(table.getSelectedRow());
    }

    /**
     * Ouverture de la boite de dialogue d'insertion d'une nouvelle école
     */
    private void doAdding ()  {
        buildDialog();

        dialog.setTitle("Insertion d'une école");
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Action de mis en jours du profil d'une école
     */
    private void doUpdating () {
        buildDialog();

        schoolForm.setSchool(getSelectedItem());
        dialog.setLocationRelativeTo(this);
        dialog.setTitle("Modification de l'appellation");
        dialog.setVisible(true);
    }

    /**
     * Action de suppression d'une école
     */
    private void doDelete () {
        School school = getSelectedItem();
        int status = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer l'école?\n"
                +"\""+school.getName()+"\"",
                "Suppression", JOptionPane.OK_CANCEL_OPTION);

        if (status == JOptionPane.OK_OPTION) {
            schoolRepository.remove(school);
        }
    }

    private final SchoolForm.SchoolFormListener formListener = new SchoolForm.SchoolFormListener() {
        @Override
        public void onValidate(String value, School school) {
            if (school == null) {
                school = new School();
                school.setRecordingDate(new Date());
            }

            dialog.setVisible(false);
            dialog.dispose();

            school.setName(value);
            schoolRepository.persist(school);
        }

        @Override
        public void onCancel() {
            dialog.setVisible(false);
            dialog.dispose();
        }
    };

    private final MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (!e.isPopupTrigger() || table.getSelectedRowCount() == 0)
                return;

            popup.show(table, e.getX(), e.getY());
        }
    };


}
