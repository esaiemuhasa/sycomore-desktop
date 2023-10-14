package com.sycomore.view.moreoption;

import com.sycomore.entity.Classifiable;
import com.sycomore.view.componets.TextFieldWrapper;

import javax.swing.*;
import java.awt.*;

public class ClassifiableForm <T extends Classifiable> extends JPanel {

    private final TextFieldWrapper shortNameWrapper = new TextFieldWrapper("Abbreviation", "");
    private final TextFieldWrapper fullNameWrapper = new TextFieldWrapper("Appellation complete", "");

    private final JButton buttonValidate = new JButton("Sauvegarder");
    private final JButton buttonCancel = new JButton("Annuler");

    private final ClassifiableFormListener formListener;
    private T data;

    public ClassifiableForm (ClassifiableFormListener formListener) {
        super(new BorderLayout());
        this.formListener = formListener;
        init();
    }

    public void setData(T data) {
        this.data = data;

        razFields();

        if (data != null) {
            if (data.getShortName() != null)
                shortNameWrapper.getField().setText(data.getShortName());

            if (data.getFullName() != null)
                fullNameWrapper.getField().setText(data.getFullName());
        }
    }

    private void init () {
        JPanel footer = new JPanel();
        Box center = Box.createVerticalBox();

        center.add(shortNameWrapper);
        center.add(fullNameWrapper);

        footer.add(buttonValidate);
        footer.add(buttonCancel);

        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttonValidate.addActionListener(e -> {
            String shortName = shortNameWrapper.getField().getText();
            String fullName = fullNameWrapper.getField().getText();

            T d = data;
            data = null;

            formListener.onValidate(shortName, fullName, d);
            razFields();
        });

        buttonCancel.addActionListener(e -> {
            formListener.onCancel();
            razFields();
        });
    }

    private void razFields () {
        shortNameWrapper.getField().setText("");
        fullNameWrapper.getField().setText("");
    }


    /**
     * Interface d'écoute de changement d'etat du formulaire de saisi
     */
    public interface ClassifiableFormListener {

        /**
         * Lorsque l'utilisateur valide l'opération
         */
        void onValidate (String shortName, String fullName, Classifiable data);

        /**
         * Lorsque l'utilisateur annule l'opération
         */
        void onCancel ();
    }
}
