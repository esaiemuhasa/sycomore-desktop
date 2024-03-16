package com.sycomore.view.workspace.students;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Barre de recherche de l'espace de visualisation de la liste des élèves
 */
public class Toolbar extends JPanel {

    private final JButton buttonInscription = new JButton("Inscription");
    private final JTextField searchTextField = new JTextField();

    private String searchLastData = "";

    private final ToolbarListener listener;


    public Toolbar(ToolbarListener listener) {
        super(new BorderLayout());

        this.listener = listener;

        //setBackground(UIManager.getColor("Component.borderColor"));

        Box  box = Box.createHorizontalBox();

        buttonInscription.setIcon(new FlatSVGIcon("icons/svg/group_add.svg"));
        searchTextField.setPreferredSize(new Dimension(400, 30));
        searchTextField.setMaximumSize(new Dimension(600, 35));

        box.add(searchTextField);
        box.add(Box.createHorizontalGlue());
        box.add(buttonInscription);
        box.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(box);
        initEvents();
    }

    private void initEvents () {
        buttonInscription.addActionListener(e -> listener.onInscriptionRequest());
        searchTextField.addCaretListener( e -> {
            String value = searchTextField.getText();

            if (!value.trim().isEmpty() && !value.equals(searchLastData)) {
                listener.onSearch(value);
            }

            searchLastData = value;
        });
    }

    @Override
    protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);

        super.paintChildren(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(UIManager.getColor("Component.borderColor"));

        g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
    }

    /**
     * Interface d'écoute des événements du toolbar
     */
    public interface ToolbarListener {

        /**
         * Lors de la demande d'insertion d'une nouvelle inscription
         */
        void onInscriptionRequest ();

        /**
         * Lors du changement du text, comme indice de recherche
         */
        void onSearch(String value);
    }
}
