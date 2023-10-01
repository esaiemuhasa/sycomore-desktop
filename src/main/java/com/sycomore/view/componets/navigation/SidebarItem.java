package com.sycomore.view.componets.navigation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class SidebarItem extends JComponent {
    private static final Font customFont = new Font("Arial", Font.BOLD, 12);
    private final SidebarItemModel itemModel;
    private final Image icon;
    private final int maxWidth;
    private boolean hovered = false;
    private boolean active = false;

    public SidebarItem(SidebarItemModel itemModel) {
        this.itemModel = itemModel;

        //load img
        try {
            icon = ImageIO.read(new File("icons/"+itemModel.getIcon()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //==

        FontMetrics metrics = getFontMetrics(customFont);
        maxWidth = metrics.stringWidth(itemModel.getCaption());

        setPreferredSize(new Dimension(maxWidth + 20, 100));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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

            private void repaitRect () {
                repaint(5, 5, getWidth() - 10, getHeight() - 10);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g.setColor(UIManager.getColor("border_color"));
        if (hovered) {
            g.fillRoundRect(5, 5, getWidth() - 12, getHeight() - 12, 10, 10);
        } else {
            g.drawRoundRect(5, 5, getWidth() - 12, getHeight() - 12, 10, 10);
        }

        if (active) {
            g.setColor(UIManager.getColor("blue_bootstrap"));
        } else {
            g.setColor(UIManager.getColor("sidebar_foreground"));
        }

        g.drawImage(icon, (getWidth()/2) - 20, 10, 40, 40, null);
        g.drawString(itemModel.getCaption(), getWidth() / 2 - maxWidth / 2, 65);
    }
}
