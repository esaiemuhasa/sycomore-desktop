package com.sycomore.view.workspace.dashboard;

import com.sycomore.helper.Config;
import com.sycomore.helper.chart.*;

import javax.swing.*;
import java.awt.*;

/**
 * Conteneur des graphiques de types histogram et lignes
 */
public class LinesChartContainer extends JPanel {

    public LinesChartContainer() {
        super(new GridBagLayout());

        DefaultPointCloud cloud = new DefaultPointCloud();
        DefaultPointCloud cloud1 = new DefaultPointCloud();
        DefaultPointCloud cloud2 = new DefaultPointCloud();
        DefaultPointCloud cloud3 = new DefaultPointCloud();

        for (int i = 0; i < 10; i++) {
            DefaultMaterialPoint point = new DefaultMaterialPoint(i,  i * Math.random() * 10d);
            cloud.addPoint(point);

            DefaultMaterialPoint p = new DefaultMaterialPoint(i,   Math.random() * 100d);
            p.setBackgroundColor(Config.getColorAt(2));
            p.setSize(10f);

            DefaultMaterialPoint p2 = new DefaultMaterialPoint(i,   Math.random() * 100d);
            p2.setBackgroundColor(Config.getColorAt(3));
            p2.setSize(10f);

            DefaultMaterialPoint p3 = new DefaultMaterialPoint(i,   Math.random() * 100d);
            p3.setBackgroundColor(Config.getColorAt(4));
            p3.setSize(10f);

            cloud1.addPoint(p);
            cloud2.addPoint(p2);
            cloud3.addPoint(p3);
        }

        cloud.setTitle("Recettes");
        cloud1.setTitle("Recettes ecole 1");
        cloud2.setTitle("Recettes ecole 2");
        cloud3.setTitle("Recettes ecole 3");

        cloud.setForegroundColor(Config.getColorAt(1));
        cloud.setBackgroundColor(Config.getAlphaColorAt(1));
        cloud.setBorderColor(Config.getColorAt(1));
        cloud.setType(PointCloud.CloudType.STICK_CHART);

        cloud1.setBorderWidth(3f);
        cloud2.setBorderWidth(3f);
        cloud3.setBorderWidth(3f);

        cloud1.setBorderColor(Config.getColorAt(2));
        cloud2.setBorderColor(Config.getColorAt(3));
        cloud3.setBorderColor(Config.getColorAt(4));

        DefaultCloudChartModel chartModel = new DefaultCloudChartModel();
        chartModel.addChart(cloud);
        chartModel.addChart(cloud1);
        chartModel.addChart(cloud2);
        chartModel.addChart(cloud3);

        DefaultPieModel model = new DefaultPieModel();
        for (int i = 1; i < 4; i++) {
            PiePart part = PiePartBuilder.build(i+3, i*Math.random()*100d, "School name "+i);
            model.addPart(part);
        }
        model.setSuffix("$");
        PiePanel pie = new PiePanel(model);
        pie.setBorder(null);

        Insets insets = new Insets(0, 0, 0, 0);
        GridBagConstraints c = new GridBagConstraints(
                2, 0, 1, 1, 1, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
        GridBagConstraints c2 = new GridBagConstraints(
                0, 0, 1, 1, 2, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 2, 0);

        JPanel renderContainer = new JPanel(new BorderLayout());
        renderContainer.setBorder(Config.DEFAULT_EMPTY_BORDER);
        renderContainer.add(new CloudChartRender(chartModel), BorderLayout.CENTER);

        add(pie, c);
        add(renderContainer, c2);

        setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
    }
}
