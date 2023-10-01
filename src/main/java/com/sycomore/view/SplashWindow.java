package com.sycomore.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SplashWindow extends JWindow {

    private final JProgressBar progressBar = new JProgressBar();

    public SplashWindow() {
        final int w = 500;
        final int h = 300;

        setSize(w, h);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        JPanel content = new SplashBkg();
        setContentPane(content);
        content.setBorder(BorderFactory.createLineBorder(UIManager.getColor("blue_bootstrap")));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        bottom.add(progressBar, BorderLayout.CENTER);

        progressBar.setStringPainted(true);
        progressBar.setString("Chargement encours...");
        progressBar.setIndeterminate(true);

        content.add(bottom, BorderLayout.SOUTH);
    }

    private static class SplashBkg extends JPanel {
        private final Image bkg;

        SplashBkg () {
            super(new BorderLayout());
            try {
                bkg = ImageIO.read(new File("icons/splash-background.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(bkg, 0, 0, getWidth(), getHeight(), null);
        }
    }
}