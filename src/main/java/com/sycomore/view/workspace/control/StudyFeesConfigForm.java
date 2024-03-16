package com.sycomore.view.workspace.control;

import com.sycomore.entity.StudyFeesConfig;
import com.sycomore.view.components.DateFieldWrapper;
import com.sycomore.view.components.TextFieldWrapper;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class StudyFeesConfigForm extends JPanel {

    private final TextFieldWrapper amountWrapper = new TextFieldWrapper("Montant (en USD)", "1");
    private final DateFieldWrapper startDateWrapper = new DateFieldWrapper("Début de recouvrement");
    private final DateFieldWrapper endDateWrapper = new DateFieldWrapper("Find de recouvrement");
    private final TextFieldWrapper descriptionWrapper = new TextFieldWrapper("Courte description", "");

    private final JButton buttonValidate = new JButton("Sauvegarder");
    private final JButton buttonCancel = new JButton("Annuler");
    private final StudyFeesConfigFormListener formListener;

    private StudyFeesConfig config;

    public StudyFeesConfigForm(StudyFeesConfigFormListener formListener) {
        super(new BorderLayout());
        this.formListener = formListener;

        init();
    }

    private void init () {
        JPanel dates = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel bottom = new JPanel();
        JPanel container = new JPanel(new BorderLayout(10, 10));

        dates.add(startDateWrapper);
        dates.add(endDateWrapper);

        container.add(amountWrapper, BorderLayout.NORTH);
        container.add(dates, BorderLayout.CENTER);
        container.add(descriptionWrapper, BorderLayout.SOUTH);

        bottom.add(buttonValidate);
        bottom.add(buttonCancel);

        add(container, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        buttonValidate.addActionListener(e -> handleValidate());
        buttonCancel.addActionListener(e -> handleCancel());
        setBorder(BorderFactory.createEmptyBorder(10, 10,10, 10));
    }

    public void setConfig(StudyFeesConfig config) {
        this.config = config;

        if (config != null){
            amountWrapper.getField().setText(String.valueOf(config.getAmount()));
            descriptionWrapper.getField().setText(config.getCaption());
            startDateWrapper.getField().setDate(config.getStart());
            endDateWrapper.getField().setDate(config.getEnd());
        } else {
            razField();
        }
    }

    private void razField () {
        amountWrapper.getField().setText("1");
        descriptionWrapper.getField().setText("");
        startDateWrapper.getField().setDate(new Date());
        endDateWrapper.getField().setDate(new Date(System.currentTimeMillis() + (60L * 1000L * 24L * 30L)));
    }

    private void handleValidate () {
        double amount = Double.parseDouble(amountWrapper.getField().getText());
        Date start = startDateWrapper.getField().getDate();
        Date end = endDateWrapper.getField().getDate();
        String caption = descriptionWrapper.getField().getText();

        formListener.onValidate(amount, start, end, caption, config);
        config = null;
    }

    private void handleCancel () {
        razField();
        formListener.onCancel();
    }

    
    public interface StudyFeesConfigFormListener {
        
        void onValidate (
                double amount, Date startDate, Date endDate, String caption,
                StudyFeesConfig config);
        
        void onCancel ();
    }
}
