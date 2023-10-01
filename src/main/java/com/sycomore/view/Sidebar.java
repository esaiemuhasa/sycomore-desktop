package com.sycomore.view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel{
    public final MigLayout layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
    public final JPanel container = new JPanel(layout);
    private final JPanel header = new JPanel(new BorderLayout());

    public Sidebar () {
        super(new BorderLayout());

        initHeader();
        initContainer();

        add(header, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);

        setOpaque(true);
        setBackground(UIManager.getColor("border_color"));
        header.setBackground(getBackground());
        container.setBackground(getBackground());
    }

    private void initHeader () {
        String filename = "icons/logo-80x100.png";
        final JLabel label = new JLabel(new ImageIcon(filename));
        header.add(label, BorderLayout.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void initContainer () {

    }

    @Override
    protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(UIManager.getColor("border_color"));
        g.drawLine(getWidth() - 1, 0, getWidth()-1, getHeight());
    }
}
