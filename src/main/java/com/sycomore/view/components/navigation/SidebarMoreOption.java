package com.sycomore.view.components.navigation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarMoreOption extends JComponent {

    private boolean hovered = false;

    public SidebarMoreOption() {
        setPreferredSize(new Dimension(60, 40));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int w = getWidth();
        final int h = getHeight();
        final int space = w / 4;

        final int y = (h / 2) - 4;

        if (hovered) {
            g.setColor(UIManager.getColor("Component.borderColor"));
            g.fillRoundRect(5, 5, w - 11, h - 11, 20, 20);
        }

        g.setColor(hovered ? UIManager.getColor("Component.blueBootstrap") : UIManager.getColor("Component.foreground"));
        for (int i = 0; i < 3; i++) {
            int x = w / 4 + (space * i) - 4;
            g.fillRoundRect(x, y, 8, 8, 10, 10);
        }

        g.setColor(UIManager.getColor("Component.borderColor").darker());
        g.drawRoundRect(5, 5, w - 11, h - 11, 20, 20);

    }
}
