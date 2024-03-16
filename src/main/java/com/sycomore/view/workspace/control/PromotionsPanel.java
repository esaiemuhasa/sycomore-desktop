package com.sycomore.view.workspace.control;


import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionRepository;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.PromotionStudyFees;
import com.sycomore.entity.School;
import com.sycomore.helper.Config;
import com.sycomore.helper.chart.*;
import com.sycomore.model.PromotionTableModel;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.MainWindow;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
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
    private final DefaultPieModel pieModel = new DefaultPieModel();

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
        JPanel center = new JPanel(new BorderLayout(Config.DEFAULT_H_GAP, Config.DEFAULT_V_GAP));

        PiePanel piePanel = new PiePanel(pieModel);

        header.add(comboBox);
        header.add(Box.createHorizontalGlue());
        header.add(buttonAdding);

        pieModel.setSuffix(" $");
        piePanel.setAlignment(PiePanel.Alignment.VERTICAL);
        piePanel.setPreferredSize(new Dimension(450, 600));

        center.add(new JScrollPane(table), BorderLayout.CENTER);
        center.add(piePanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        menu.add(itemDelete);
        itemDelete.addActionListener(e -> handleRemovePromotion());

        buttonAdding.addActionListener(event -> doAddingPromotion());
        comboBox.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED)
                return;

            School school = boxModel.getElementAt(comboBox.getSelectedIndex());
            tableModel.setSchool(school);

            if (tableModel.getRowCount() > 0) {
                table.setRowSelectionInterval(0, 0);
                doSelectPromotion();
            }
        });

        tableModel.addTableModelListener(event -> {
            if(event.getType() == TableModelEvent.UPDATE) {
                doSelectPromotion();
            }
        });

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                doSelectPromotion();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && table.getSelectedRow() != -1)
                    menu.show(table, e.getX(), e.getY());
            }
        });
    }

    private void doSelectPromotion () {
        int index = table.getSelectedRow();

        if (index == -1)
            return;

        Promotion promotion = tableModel.getRow(index);
        revalidatePieRender(promotion);
    }

    /**
     * Action de revalidation du rendu du graphique de visualisation de la repartition des frais d'étude que doit payer une promotion.
     */
    public void revalidatePieRender (Promotion promotion) {

        Promotion old = (Promotion) pieModel.getObject();
        PromotionStudyFees[] fees = dataModel.getPromotionStudyFees(promotion);

        pieModel.setTitle(String.format(" %s / %s", promotion.getSchool().toString(), promotion.toString()));


        if (promotion.equals(old)) {//revalidation de parts
            if (fees == null) {
                pieModel.removeAll();
                return;
            }

            for (int i = pieModel.getCountPart()-1; i >= 0 ; i--) {
                PiePart part = pieModel.getPartAt(i);

                boolean check = false;

                for (PromotionStudyFees f : fees)
                    if (f.equals(part.getData())) {
                        part.setValue(f.getConfig().getAmount());
                        part.setLabel(f.getConfig().getCaption());
                        check = true;
                        break;
                    }

                if (!check)
                    pieModel.removePartAt(i);
            }

            //items à ajouter
            for (PromotionStudyFees f : fees) {
                PiePart part = pieModel.findByData(f);
                if (part != null)
                    continue;

                pieModel.addPart(PiePartBuilder.build(pieModel.getCountPart(), f.getConfig().getAmount(), f.getConfig().getCaption()));
            }

            return;
        } else {
            pieModel.removeAll();
        }

        pieModel.setObject(promotion);
        if (fees == null)
            return;

        PiePart[] parts = new PiePart[fees.length];

        for (int i = 0; i < fees.length; i++) {
            PromotionStudyFees f = fees[i];
            PiePart part = PiePartBuilder.build(i,f.getConfig().getAmount(), f.getConfig().getCaption());
            part.setData(f);
            parts[i] = part;
        }

        pieModel.addParts(parts);
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
