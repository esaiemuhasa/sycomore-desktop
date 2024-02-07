package com.sycomore.view.workspace.students;

import javax.swing.*;
import java.awt.*;

/**
 * Conteneur des listes des étudiants, groupé par promotion
 */
public class StudentsDataViewContainer extends JPanel {
    public StudentsDataViewContainer() {
        super(new BorderLayout());
    }

    @Override
    protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);

        super.paintChildren(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(UIManager.getColor("border_color"));

        g.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
    }
}
