package com.sycomore.view;

import com.sycomore.view.componets.navigation.SidebarItem;
import com.sycomore.view.componets.navigation.SidebarItemModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {
    public final MigLayout layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
    public final JPanel container = new JPanel(layout);
    private final JPanel header = new JPanel(new BorderLayout());
    private SidebarItemChangeListener itemChangeListener;
    private SidebarItem currentItem;

    public Sidebar () {
        super(new BorderLayout());

        initHeader();

        add(header, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);

        setOpaque(true);
        setBackground(UIManager.getColor("sidebar_background"));
        header.setBackground(getBackground());
        container.setBackground(getBackground());
    }

    public SidebarItem getCurrentItem() {
        return currentItem;
    }

    public void setItemChangeListener (SidebarItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    private void initHeader () {
        String filename = "icons/logo-80x100.png";
        final JLabel label = new JLabel(new ImageIcon(filename));
        header.add(label, BorderLayout.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void onAction (SidebarItem item) {
        if (item == currentItem)
            return;

        if (currentItem != null)
            currentItem.setActive(false);

        SidebarItem old = currentItem;
        currentItem = item;
        item.setActive(true);

        if (itemChangeListener != null)
            itemChangeListener.onChange(item, old);
    }

    /**
     * Action d'ajout d'un item au menu principale.
     */
    public Sidebar addItem (String caption, String icon, String name) {
        SidebarItemModel model = new SidebarItemModel(caption, icon, name);
        SidebarItem item = new SidebarItem(model, this::onAction);
        container.add(item, "h 90!");
        if (currentItem == null) {
            currentItem = item;
            item.setActive(true);
        }
        return this;
    }

    @Override
    protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(UIManager.getColor("border_color"));
        g.drawLine(getWidth() - 1, 0, getWidth()-1, getHeight());
    }

    /**
     * Interface d'écoute de changement de l'item actuellement sélectionné
     */
    public interface SidebarItemChangeListener {
        void onChange (SidebarItem currentItem, SidebarItem oldItem);
    }
}
