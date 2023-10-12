package com.sycomore.view.workspace.control;


import com.sycomore.entity.School;
import com.sycomore.model.PromotionTableModel;

import javax.swing.*;
import java.awt.*;

public class PromotionsPanel extends JPanel {

    private final JButton buttonAdding = new JButton("Ajouter autre(s) promotion(s)");
    private final DefaultComboBoxModel<School> boxModel = new DefaultComboBoxModel<>();
    private final JComboBox<School> comboBox = new JComboBox<>(boxModel);

    private final PromotionTableModel tableModel = new PromotionTableModel();
    private final JTable table = new JTable(tableModel);

    public PromotionsPanel() {
        super(new BorderLayout(10, 10));
        init();
    }

    private void init () {
        Box header = Box.createHorizontalBox();

        header.add(comboBox);
        header.add(Box.createHorizontalGlue());
        header.add(buttonAdding);

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
