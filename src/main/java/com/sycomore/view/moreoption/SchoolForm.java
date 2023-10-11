package com.sycomore.view.moreoption;

import com.sycomore.entity.School;
import com.sycomore.view.componets.TextFieldWrapper;

import javax.swing.*;
import java.awt.*;

public class SchoolForm extends JPanel {

    private final JButton buttonValidate = new JButton("Valider");
    private final JButton buttonCancel = new JButton("Annuler");

    private final TextFieldWrapper fieldWrapper = new TextFieldWrapper("Appellation", "");

    private final SchoolFormListener formListener;

    private School currentSchool;//école en cours de modification

    public SchoolForm (SchoolFormListener formListener) {
        super(new BorderLayout(10, 10));
        this.formListener = formListener;
        init();
    }

    private void init () {
        JPanel footer = new JPanel();

        footer.add(buttonValidate);
        footer.add(buttonCancel);

        add(footer, BorderLayout.SOUTH);
        add(fieldWrapper, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttonCancel.addActionListener(e -> {
            fieldWrapper.getField().setText("");
            formListener.onCancel();
        });

        buttonValidate.addActionListener(e -> {
            String value = fieldWrapper.getField().getText();
            fieldWrapper.getField().setText("");
            formListener.onValidate(value, currentSchool);
            currentSchool = null;
        });
    }

    public void setSchool (School school) {
        currentSchool = school;
        if (school != null)
            fieldWrapper.getField().setText(school.getName());
    }

    /**
     * Interface d'écoute des évènements
     */
    public static interface SchoolFormListener {
        void onValidate(String value, School school);
        void onCancel();
    }
}
