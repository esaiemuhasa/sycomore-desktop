package com.sycomore.view.moreoption;

import com.sycomore.entity.Section;
import com.sycomore.model.OptionTableModel;
import com.sycomore.model.SectionComboBoxModel;

import javax.swing.*;
import java.awt.*;

public class SectionsPanel extends JPanel {

    private final OptionTableModel tableModel = new OptionTableModel();
    private final JTable table = new JTable(tableModel);

    private final SectionComboBoxModel boxModel = new SectionComboBoxModel();
    private final JComboBox<Section> comboBox = new JComboBox<>(boxModel);

    private final JButton buttonAddingSection = new JButton("Ajouter une section");
    private final JButton buttonAddingOption = new JButton("Ajouter une option");

    public SectionsPanel() {
        super(new BorderLayout(10, 10));
        init();
    }

    private void init () {
        JPanel buttons = new JPanel(new GridLayout(1, 2,5, 5));
        JPanel head =  new JPanel(new BorderLayout(10, 10));

        buttons.add(buttonAddingSection);
        buttons.add(buttonAddingOption);

        head.add(buttons, BorderLayout.EAST);
        head.add(comboBox, BorderLayout.CENTER);
        head.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(head, BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
    }
}
