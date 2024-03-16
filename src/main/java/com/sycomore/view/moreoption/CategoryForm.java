package com.sycomore.view.moreoption;

import com.sycomore.entity.Category;
import com.sycomore.view.components.TextFieldWrapper;

import javax.swing.*;
import java.awt.*;

public class CategoryForm extends JPanel {

    private final JButton buttonValidate = new JButton("Valider");
    private final JButton buttonCancel = new JButton("Annuler");

    private final TextFieldWrapper fieldWrapper = new TextFieldWrapper("Appellation", "");

    private final CategoryFormListener formListener;

    private Category category;//catégorie en cours de modification

    public CategoryForm(CategoryFormListener formListener) {
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
            formListener.onValidate(value, category);
            category = null;
        });
    }

    public void setCategory(Category category) {
        this.category = category;
        String label = (category != null && category.getLabel() != null) ? category.getLabel() : "";
        fieldWrapper.getField().setText(label);
    }

    /**
     * Interface d'écoute des évènements
     */
    public interface CategoryFormListener {
        void onValidate(String value, Category category);
        void onCancel();
    }
}
