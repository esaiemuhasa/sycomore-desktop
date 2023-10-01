package com.sycomore.view.componets;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Workspace extends JPanel {

    private final CardLayout cardLayout = new CardLayout();

    private final List<WorkspaceItem> items = new ArrayList<>();
    private WorkspaceItem currentItem;

    public Workspace () {
        super();
        setLayout(cardLayout);
    }

    /**
     * Ajout d'un nouvel onglet a l'espace de travail
     */
    public <T extends WorkspaceItem > Workspace addItem (T item) {
        add(item.getComponent(), item.getComponent().getName());
        items.add(item);
        if (currentItem == null) {
            currentItem = item;
            currentItem.onShow();
        }
        return this;
    }

    /**
     * Demande au conteneur d'afficher l'item dont le nom est en paramètre.
     */
    public void showItem (String name) {
        WorkspaceItem item = null;
        for (WorkspaceItem i : items)
            if (i.getComponent().getName().equals(name)) {
                item = i;
                break;
            }

        if (item == null)
            throw new RuntimeException("unknown workspace item : "+name);

        if (currentItem != null)
            currentItem.onHide();

        currentItem = item;
        item.onShow();

        cardLayout.show(this, name);
    }

    /**
     * Interface que doit implementer tout item de l'espace de travail.
     */
    public interface WorkspaceItem {
        JComponent getComponent ();

        void onShow ();

        void onHide ();
    }
}
