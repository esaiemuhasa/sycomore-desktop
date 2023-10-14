package com.sycomore.view.componets;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class DateFieldWrapper extends JPanel {

    private final JDateChooser field = new JDateChooser();
    private final JLabel label = new JLabel();

    public DateFieldWrapper() {
        super(new BorderLayout());
        init();
    }

    public DateFieldWrapper(String label) {
        super(new BorderLayout());
        this.label.setText(label);
        init();
    }

    public DateFieldWrapper(String label, Date value) {
        super(new BorderLayout());
        this.label.setText(label);
        this.field.setDate(value);
        init();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        label.setEnabled(enabled);
        field.setEnabled(enabled);
    }

    public JDateChooser getField() {
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
