package com.sycomore.view.workspace.students;

import com.sycomore.entity.Inscription;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.School;
import com.sycomore.model.YearDataModel;
import com.sycomore.view.components.ComboBoxWrapper;
import com.sycomore.view.components.DateFieldWrapper;
import com.sycomore.view.components.TextFieldWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Date;

public class InscriptionForm extends JPanel {

    private final DefaultComboBoxModel<School> schoolComboBoxModel = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<Promotion> promotionComboBoxModel = new DefaultComboBoxModel<>();

    private final ComboBoxWrapper<School> boxWrapperSchool = new ComboBoxWrapper<>("École", schoolComboBoxModel);
    private final ComboBoxWrapper<Promotion> boxWrapperPromotion = new ComboBoxWrapper<>("Promotion", promotionComboBoxModel);

    private final TextFieldWrapper fieldWrapperRegistrationNumber = new TextFieldWrapper("Numéro matricule", "");
    private final DateFieldWrapper fieldWrapperBirthDate = new DateFieldWrapper("Date de naissance", new Date(System.currentTimeMillis() - (60L * 1000L * 24L * 12L * 10)));
    private final TextFieldWrapper fieldWrapperNames = new TextFieldWrapper("Nom, Post-nom et Prénom", "");

    private final JButton buttonValidate = new JButton("Enregistrer");
    private final JButton buttonCancel = new JButton("Annuler");

    private final YearDataModel yearDataModel;
    private final InscriptionFormListener listener;
    private Inscription inscription;//dans le cas d'une modification, il s'agit de la reference vers l'inscription encoiurs de modification

    public InscriptionForm(InscriptionFormListener listener) {
        super(new BorderLayout());
        this.listener = listener;
        yearDataModel = YearDataModel.getInstance();
        init();
        initEvents();
    }

    /**
     * Construction des composants graphiques
     */
    private void init () {
        Box box = Box.createVerticalBox();
        JPanel bottom = new JPanel();

        box.add(boxWrapperSchool);
        box.add(Box.createVerticalStrut(10));
        box.add(boxWrapperPromotion);
        box.add(Box.createVerticalStrut(10));
        box.add(fieldWrapperNames);
        box.add(Box.createVerticalStrut(10));
        box.add(fieldWrapperBirthDate);
        box.add(Box.createVerticalStrut(10));
        box.add(fieldWrapperRegistrationNumber);
        box.add(Box.createVerticalStrut(10));

        bottom.add(buttonValidate);
        bottom.add(buttonCancel);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        add(box, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void reload () {
        schoolComboBoxModel.removeAllElements();
        promotionComboBoxModel.removeAllElements();

        School [] schools = yearDataModel.getSchools();
        for (School s: schools)
            schoolComboBoxModel.addElement(s);
    }

    /**
     * Écoute des événements des composants graphiques
     */
    private void initEvents () {
        boxWrapperSchool.getField().addItemListener(this::onSchoolChange);
        buttonValidate.addActionListener(e -> {

            String names = fieldWrapperNames.getField().getText();
            String registrationNumber = fieldWrapperRegistrationNumber.getField().getText();
            Date birthDate = fieldWrapperBirthDate.getField().getDate();

            Promotion promotion = promotionComboBoxModel.getElementAt(boxWrapperPromotion.getField().getSelectedIndex());

            listener.onValidate(names, registrationNumber, birthDate, promotion, inscription);
            inscription = null;
        });

        buttonCancel.addActionListener( e -> listener.onCancel());
    }

    private void onSchoolChange (ItemEvent event) {
        promotionComboBoxModel.removeAllElements();
        if (event.getStateChange() == ItemEvent.DESELECTED || schoolComboBoxModel.getSize() == 0)
            return;

        School school = schoolComboBoxModel.getElementAt(boxWrapperSchool.getField().getSelectedIndex());

        Promotion [] promotions = yearDataModel.getPromotions(school);
        for (Promotion p : promotions)
            promotionComboBoxModel.addElement(p);
    }

    /**
     * Mutation de l'école actuellement sélectionnée
     */
    public void setSchool (School school) {
        School selected = schoolComboBoxModel.getElementAt(boxWrapperSchool.getField().getSelectedIndex());

        if (school.equals(selected))
            return;

        for (int i = 0; i < schoolComboBoxModel.getSize(); i++) {
            if (school.equals(schoolComboBoxModel.getElementAt(i))) {
                boxWrapperSchool.getField().setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Mutation de la promotion actuellement sélectionnée
     */
    public void setPromotion (Promotion promotion) {

        setSchool(promotion.getSchool());

        Promotion p = promotionComboBoxModel.getElementAt(boxWrapperPromotion.getField().getSelectedIndex());
        if (!p.equals(promotion))
            for (int i = 0; i < promotionComboBoxModel.getSize(); i++) {
                if (promotion.equals(promotionComboBoxModel.getElementAt(i))) {
                    boxWrapperPromotion.getField().setSelectedIndex(i);
                    break;
                }
            }
    }

    public interface InscriptionFormListener {

        /**
         * Lorsque l'utilisateur clic sur le button valider
         */
        void onValidate (String names, String registrationNumber, Date birthDate, Promotion promotion, Inscription inscription);

        /**
         * Lors l'utilisateur annule l'opération
         */
        void onCancel ();
    }
}
