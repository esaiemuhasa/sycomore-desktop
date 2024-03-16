package com.sycomore.view.workspace.dashboard;

import com.sycomore.helper.Config;
import com.sycomore.helper.chart.DefaultPieModel;
import com.sycomore.helper.chart.PiePartBuilder;
import com.sycomore.helper.chart.PieRender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Conteneur des graphiques de types PieChart
 */
public class PiesChartContainer extends JPanel {

    public PiesChartContainer () {
        super(new GridLayout(1, 3, Config.DEFAULT_H_GAP, Config.DEFAULT_V_GAP));

        JPanel students = new JPanel(new BorderLayout());
        JPanel forecasting = new JPanel(new BorderLayout());
        JPanel cash = new JPanel(new BorderLayout());

        DefaultPieModel model = new DefaultPieModel();
        for (int i = 1; i < 4; i++) {
            model.addPart(PiePartBuilder.build(i-1, i*2d, "Items "+i));
        }

        model.setSelectablePart(true);

        students.add(new PieRender(model), BorderLayout.CENTER);
        forecasting.add(new PieRender(model), BorderLayout.CENTER);
        cash.add(new PieRender(model), BorderLayout.CENTER);

        students.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        forecasting.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        cash.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

        add(students);
        add(forecasting);
        add(cash);
    }
}
