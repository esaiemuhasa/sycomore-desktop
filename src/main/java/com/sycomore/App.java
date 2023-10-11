package com.sycomore;

import com.formdev.flatlaf.FlatLightLaf;
import com.sycomore.helper.Config;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.MainWindow;
import com.sycomore.view.SplashWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        FlatLightLaf.registerCustomDefaultsSource("themes");
        FlatLightLaf.setup();

        Map<String, String> colors = Config.formPropertiesFile("themes/ColorLight");
        if (colors != null) {
            Set<String> keys = colors.keySet();
            for (String key : keys) {
                int c = Integer.decode(colors.get(key));
                Color color = new Color(c);
                UIManager.put(key, color);
            }
        }

        YearDataModel model = YearDataModel.getInstance();

        SplashWindow splash = new SplashWindow();
        splash.setVisible(true);

        MainWindow.setup();

        YearDataModelListener modelListener = new YearDataModelListener() {
            @Override
            public void onSetup() {}
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadFinish() {
                model.removeYearDataListener(this);
                splash.setVisible(false);
                splash.dispose();
                MainWindow.getInstance().setVisible(true);
            }
        };

        model.addYearDataListener(modelListener);
        model.setup();
    }
}
