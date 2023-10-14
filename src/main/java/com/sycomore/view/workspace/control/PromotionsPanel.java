package com.sycomore.view.workspace.control;


import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionRepository;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.School;
import com.sycomore.model.PromotionTableModel;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PromotionsPanel extends JPanel {

    private final JButton buttonAdding = new JButton("Ajouter autre(s) promotion(s)");
    private final DefaultComboBoxModel<School> boxModel = new DefaultComboBoxModel<>();
    private final JComboBox<School> comboBox = new JComboBox<>(boxModel);

    private final JPopupMenu menu = new JPopupMenu();
    private final JMenuItem itemDelete = new JMenuItem("Supprimer");

    private final PromotionTableModel tableModel = new PromotionTableModel();

    private final PromotionRepository promotionRepository;
    private final YearDataModel dataModel;

    private JDialog dialog;
    private final JTable table = new JTable(tableModel);

    public PromotionsPanel() {
        super(new BorderLayout(10, 10));
        dataModel = YearDataModel.getInstance();
        promotionRepository = DAOFactory.getInstance(PromotionRepository.class);
        init();

        dataModel.addYearDataListener(dataModelListener);
    }

    private void init () {
        Box header = Box.createHorizontalBox();

        header.add(comboBox);
        header.add(Box.createHorizontalGlue());
        header.add(buttonAdding);

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        menu.add(itemDelete);
        itemDelete.addActionListener(e -> handleRemovePromotion());

        buttonAdding.addActionListener(event -> doAddingPromotion());
        comboBox.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED)
                return;

            School school = boxModel.getElementAt(comboBox.getSelectedIndex());
            tableModel.setSchool(school);
        });

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() != -1)
                    menu.show(table, e.getX(), e.getY());
            }
        });
    }

    /**
     * Action de suppression d'une promotion
     */
    private void handleRemovePromotion () {
        int index = table.getSelectedRow();
        if (index == -1)
            return;

        Promotion promotion = tableModel.getRow(index);
        int status = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer la promotion \n"+promotion, "Suppression promotion", JOptionPane.OK_CANCEL_OPTION);

        if (status == JOptionPane.OK_OPTION)
            promotionRepository.remove(promotion);
    }


    /**
     * Utilitaire de chargement de la boite de dialogue d'ajout des promotions.
     */
    private void buildDialog () {
        if (dialog != null)
            return;

        dialog = new JDialog(MainWindow.getInstance(), "Ajout des promotions");
        PromotionForm promotionForm = new PromotionForm(formListener);

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(promotionForm);
        dialog.pack();

        promotionForm.setup();
    }

    private void doAddingPromotion () {
        buildDialog();

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private final PromotionForm.PromotionFormListener formListener = new PromotionForm.PromotionFormListener() {
        @Override
        public void onValidate(Promotion... promotions) {
            onCancel();

            for (Promotion p : promotions) {
                p.setYear(dataModel.getYear());
                promotionRepository.persist(p);
            }
        }

        @Override
        public void onCancel() {
            dialog.setVisible(false);
            dialog.dispose();
        }
    };

    protected final YearDataModelListener dataModelListener = new YearDataModelAdapter() {

        @Override
        public void onLoadFinish() {
            boxModel.removeAllElements();

            School [] schools = dataModel.getSchools();
            if (schools != null) {
                for (School s : schools)
                    boxModel.addElement(s);
            }
        }

        @Override
        public void onPromotionTreeChange(Promotion... promotions) {
            School school = boxModel.getSize() > 0 ? boxModel.getElementAt(comboBox.getSelectedIndex()) : null;
            School [] schools = dataModel.getSchools();

            boxModel.removeAllElements();

            boolean exist = false;

            if (school != null) {
                for (Promotion p : promotions) {
                    if (p.getSchool().equals(school)) {
                        exist = true;
                        break;
                    }
                }
            }

            if (schools != null) {
                for (School s : schools) {
                    boxModel.addElement(s);
                }

                if (exist) {
                    comboBox.setSelectedItem(school);
                }
            }
        }
    };
}
