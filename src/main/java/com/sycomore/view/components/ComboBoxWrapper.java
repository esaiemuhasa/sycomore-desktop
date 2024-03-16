package com.sycomore.view.components;

import javax.swing.*;
import java.awt.*;

public class ComboBoxWrapper <T> extends JPanel {

    private final JComboBox<T> field;
    private final JLabel label = new JLabel();

    public ComboBoxWrapper() {
        super(new BorderLayout());
        field = new JComboBox<>();
        init();
    }

    public ComboBoxWrapper(String label, ComboBoxModel<T> model) {
        super(new BorderLayout());
        this.label.setText(label);
        this.field = new JComboBox<>(model);
        init();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        label.setEnabled(enabled);
        field.setEnabled(enabled);
    }

    public JComboBox<T> getField() {
        return field;
    }

    public JLabel getLabel() {
        return label;
    }

    private void init () {
        setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
        add(label, BorderLayout.NORTH);
        add(field, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    }
}
