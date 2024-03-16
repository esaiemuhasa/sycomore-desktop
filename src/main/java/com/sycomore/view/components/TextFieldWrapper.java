package com.sycomore.view.components;

import javax.swing.*;
import java.awt.*;

public class TextFieldWrapper extends JPanel {

    private final JTextField field = new JTextField();
    private final JLabel label = new JLabel();

    public TextFieldWrapper() {
        super(new BorderLayout());
        init();
    }

    public TextFieldWrapper(String label, String value) {
        super(new BorderLayout());
        this.label.setText(label);
        this.field.setText(value);
        init();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        label.setEnabled(enabled);
        field.setEnabled(enabled);
    }

    public JTextField getField() {
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
