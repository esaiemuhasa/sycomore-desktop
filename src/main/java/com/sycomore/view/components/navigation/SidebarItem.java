package com.sycomore.view.components.navigation;

import com.sun.istack.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SidebarItem extends JComponent {
    private static final Font CUSTOM_FONT = new Font("Arial", Font.BOLD, 9);
    private final SidebarItemModel itemModel;
    private final BufferedImage icon;
    private final int captionWidth;
    private boolean hovered = false;
    private boolean active = false;

    public SidebarItem(@NotNull SidebarItemModel itemModel, @NotNull SidebarItemListener itemListener) {
        this.itemModel = itemModel;

        //load img
        try {
            icon = ImageIO.read(new File("icons/"+itemModel.getIcon()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //==

        FontMetrics metrics = getFontMetrics(CUSTOM_FONT);
        captionWidth = metrics.stringWidth(itemModel.getCaption());

        setPreferredSize(new Dimension(captionWidth + 20, 100));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                itemListener.onAction(SidebarItem.this);
                repaitRect();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaitRect();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaitRect();
            }

        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g.setColor(UIManager.getColor("Component.borderColor"));
        if (hovered || active)
            g.fillRoundRect(5, 5, getWidth() - 11, getHeight() - 11, 10, 10);
        else
            g.drawRoundRect(5, 5, getWidth() - 11, getHeight() - 11, 10, 10);

        Color c = active ? UIManager.getColor("Component.activeColor") : UIManager.getColor("Sidebar.foreground");
        g.setColor(c);

        if (active)
            g.drawRoundRect(5, 5, getWidth() - 11, getHeight() - 11, 10, 10);

        final int img_w = 50;
        g.drawImage(icon, (getWidth()/2) - (img_w / 2), 10, img_w, img_w, null);
        g.setFont(CUSTOM_FONT);
        g.drawString(itemModel.getCaption(), getWidth() / 2 - captionWidth / 2, img_w + 20);
    }

    private void repaitRect () {
        repaint(5, 5, getWidth() - 10, getHeight() - 10);
    }

    public void setActive(boolean active) {
        if (this.active == active)
            return;

        this.active = active;
        repaitRect();
    }

    public SidebarItemModel getItemModel() {
        return itemModel;
    }
}
