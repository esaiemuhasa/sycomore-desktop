package com.sycomore.view.moreoption;

import com.sycomore.dao.CategoryRepository;
import com.sycomore.dao.DAOFactory;
import com.sycomore.entity.Category;
import com.sycomore.model.CategoryTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

public class CategoriesPanel extends JPanel {

    private final CategoryTableModel tableModel = new CategoryTableModel();
    private final JTable table = new JTable(tableModel);
    private final JPopupMenu popup = new JPopupMenu();
    private final JMenuItem update = new JMenuItem("Modifier");
    private final JMenuItem delete = new JMenuItem("Supprimer");
    private final JButton buttonAdding = new JButton("Ajouter");

    private final CategoryRepository categoryRepository;

    private JDialog dialog;//boite de dialogue de manipulation des infos d'une catégorie
    private CategoryForm categoryForm;

    public CategoriesPanel() {
        super (new BorderLayout(5, 5));
        categoryRepository = DAOFactory.getInstance(CategoryRepository.class);
        init();

        tableModel.init();
    }

    private Category getSelectedItem () {
        return tableModel.getRow(table.getSelectedRow());
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

    /**
     * Ouverture de la boite de dialogue d'insertion d'une nouvelle catégorie
     */
    private void doAdding ()  {
        buildDialog();

        dialog.setTitle("Insertion d'une catégorie");
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Action de mis en jours du profil d'une catégorie
     */
    private void doUpdating () {
        buildDialog();

        categoryForm.setCategory(getSelectedItem());
        dialog.setLocationRelativeTo(this);
        dialog.setTitle("Modification de l'appellation");
        dialog.setVisible(true);
    }

    /**
     * Action de suppression d'une école
     */
    private void doDelete () {
        Category category = getSelectedItem();
        int status = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer la catégorie?\n"
                        +"\""+category.getLabel()+"\"",
                "Suppression", JOptionPane.OK_CANCEL_OPTION);

        if (status == JOptionPane.OK_OPTION) {
            categoryRepository.remove(category);
        }
    }

    private void buildDialog () {
        if (dialog != null)
            return;

        dialog = new JDialog();
        categoryForm = new CategoryForm(formListener);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(categoryForm);
        dialog.pack();
        dialog.setSize(dialog.getWidth() + 200, dialog.getHeight()+10);
        dialog.setResizable(false);
    }

    private final MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (!e.isPopupTrigger() || table.getSelectedRowCount() == 0)
                return;

            popup.show(table, e.getX(), e.getY());
        }
    };


    private final CategoryForm.CategoryFormListener formListener = new CategoryForm.CategoryFormListener() {
        @Override
        public void onValidate(String value, Category category) {
            dialog.setVisible(false);
            dialog.dispose();

            if (category == null)
                category = new Category();

            category.setLabel(value);
            if (category.getRecordingDate() == null)
                category.setRecordingDate(new Date());
            else
                category.setUpdatingDate(new Date());

            categoryRepository.persist(category);
        }

        @Override
        public void onCancel() {
            dialog.setVisible(false);
            dialog.dispose();
        }
    };
}
