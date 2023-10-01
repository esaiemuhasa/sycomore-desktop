package com.sycomore.view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final Sidebar sidebar = new Sidebar();

    public MainWindow ()  {
        super("");

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (screen.width / 100) * 80;// 80 % de la largeur l'écran
        int h = (screen.height / 100) * 70;// 70 % de la hauteur de l'écran

        setSize(w, h);
        //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //
        JPanel container = (JPanel) getContentPane();
        container.add(sidebar, BorderLayout.WEST);
    }
}
