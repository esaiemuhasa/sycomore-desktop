package com.sycomore.view.workspace;

import com.sycomore.helper.Config;
import com.sycomore.model.dashboard.CashCounterCardModel;
import com.sycomore.model.dashboard.ForecastCounterCardModel;
import com.sycomore.model.dashboard.StudentCounterCardModel;
import com.sycomore.view.components.Workspace;
import com.sycomore.view.components.swing.Card;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel implements Workspace.WorkspaceItem {


    public DashboardPanel() {
        super(new BorderLayout(Config.DEFAULT_H_GAP, Config.DEFAULT_V_GAP));

        setBackground(UIManager.getColor("Component.background"));
        setBorder(Config.DEFAULT_EMPTY_BORDER);

        JPanel helder = new JPanel(new GridLayout(1, 3, Config.DEFAULT_H_GAP, Config.DEFAULT_V_GAP));

        helder.add(new Card(StudentCounterCardModel.getInstance()));
        helder.add(new Card(ForecastCounterCardModel.getInstance()));
        helder.add(new Card(CashCounterCardModel.getInstance()));

        add(helder, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.CENTER);
    }

    @Override
    public String getName() {
        return "dashboard";
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }
}
