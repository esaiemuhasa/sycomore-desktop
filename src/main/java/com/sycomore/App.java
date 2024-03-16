package com.sycomore;

import com.formdev.flatlaf.FlatLightLaf;
import com.sycomore.helper.StateChecker;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.MainWindow;
import com.sycomore.view.SplashWindow;

/**
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        FlatLightLaf.registerCustomDefaultsSource("themes");
        FlatLightLaf.setup();

        YearDataModel model = YearDataModel.getInstance();

        SplashWindow splash = new SplashWindow();
        splash.setVisible(true);

        MainWindow.setup();
        StateChecker.setup();

        YearDataModelListener modelListener = new YearDataModelAdapter() {
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
