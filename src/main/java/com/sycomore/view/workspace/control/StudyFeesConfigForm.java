package com.sycomore.view.workspace.control;

import com.sycomore.entity.StudyFeesConfig;
import com.sycomore.view.componets.DateFieldWrapper;
import com.sycomore.view.componets.TextFieldWrapper;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class StudyFeesConfigForm extends JPanel {

    private final TextFieldWrapper amountWrapper = new TextFieldWrapper("Montant (en USD)", "1");
    private final DateFieldWrapper startDate = new DateFieldWrapper("Début de recouvrement");
    private final DateFieldWrapper endDate = new DateFieldWrapper("Find de recouvrement");
    private final TextFieldWrapper descriptionWrapper = new TextFieldWrapper("Courte description", "");

    private final JButton buttonValidate = new JButton("Sauvegarder");
    private final JButton buttonCancel = new JButton("Annuler");
    private final StudyFeesConfigFormListener formListener;

    public StudyFeesConfigForm(StudyFeesConfigFormListener formListener) {
        super(new BorderLayout());
        this.formListener = formListener;

        init();
    }

    private void init () {
        JPanel dates = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel bottom = new JPanel();
        JPanel container = new JPanel(new BorderLayout(10, 10));

        dates.add(startDate);
        dates.add(endDate);

        container.add(amountWrapper, BorderLayout.NORTH);
        container.add(dates, BorderLayout.CENTER);
        container.add(descriptionWrapper, BorderLayout.SOUTH);

        bottom.add(buttonValidate);
        bottom.add(buttonCancel);

        add(container, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        buttonValidate.addActionListener(e -> handleValidate());
        buttonCancel.addActionListener(e -> handleCancel());
    }

    private void handleValidate () {

    }

    private void handleCancel () {

    }

    
    public interface StudyFeesConfigFormListener {
        
        void onValidate (
                double amount, Date startDate, Date endDate, String caption,
                StudyFeesConfig config);
        
        void onCancel ();
    }
}
