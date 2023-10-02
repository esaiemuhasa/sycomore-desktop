package com.sycomore;

import com.formdev.flatlaf.FlatLightLaf;
import com.sycomore.helper.Config;
import com.sycomore.view.MainWindow;
import com.sycomore.view.SplashWindow;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

        EntityManagerFactory factory = Persistence.createEntityManagerFactory(Config.get("persistence_unit"));
        EntityManager manager = factory.createEntityManager();

        //SplashWindow splash = new SplashWindow();
        //splash.setVisible(true);

        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        
    }
}
