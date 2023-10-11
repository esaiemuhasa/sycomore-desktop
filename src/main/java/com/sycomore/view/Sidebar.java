package com.sycomore.view;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.dao.SchoolYearRepository;
import com.sycomore.entity.SchoolYear;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.componets.navigation.SidebarItem;
import com.sycomore.view.componets.navigation.SidebarItemModel;
import com.sycomore.view.componets.navigation.SidebarMoreOption;
import com.sycomore.view.componets.navigation.SidebarMoreOptionListener;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Sidebar extends JPanel {
    public final MigLayout layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
    public final JPanel container = new JPanel(layout);
    private final JPanel header = new JPanel(new BorderLayout());
    private SidebarItemChangeListener itemChangeListener;
    private SidebarMoreOptionListener moreOptionListener;
    private SidebarItem currentItem;

    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JMenu schoolYears = new JMenu("Années scolaire");
    private final ButtonGroup schoolYearGroup = new ButtonGroup();//group items année scolaire
    private final SchoolYearRepository yearRepository;
    private final YearDataModel yearDataModel = YearDataModel.getInstance();

    public Sidebar () {
        super(new BorderLayout());

        yearRepository = DAOFactory.getInstance(SchoolYearRepository.class);

        initHeader();
        initFooter();

        JPanel footer = new JPanel();
        SidebarMoreOption option = new SidebarMoreOption();

        footer.setOpaque(false);
        footer.add(option);

        option.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doShowPopup();
            }
        });

        add(header, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        setOpaque(true);
        setBackground(UIManager.getColor("sidebar_background"));
        header.setBackground(getBackground());
        container.setBackground(getBackground());

        yearRepository.addRepositoryListener(new RepositoryAdapter<SchoolYear>() {
            @Override
            public void onCreate(SchoolYear year) {
                reloadYearItems();
            }

            @Override
            public void onUpdate(SchoolYear oldState, SchoolYear newState) {
                reloadYearItems();
            }

            @Override
            public void onDelete(SchoolYear year) {
                reloadYearItems();
            }
        });

        yearDataModel.addYearDataListener(new YearDataModelListener() {
            @Override
            public void onSetup() {
                reloadYearItems();
            }

            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadFinish() {}
        });
    }

    private void initFooter () {
        JMenuItem logout = new JMenuItem("Quitter l'application", new ImageIcon("icons/close.png"));
        JMenuItem newYear = new JMenuItem("Nouvelle années scolaire", new ImageIcon("icons/tipp.png"));
        JMenuItem options = new JMenuItem("Orientation et classe d'études", new ImageIcon("icons/versions.png"));

        logout.addActionListener(event -> {
            if (moreOptionListener != null)
                moreOptionListener.doLogout();
        });

        newYear.addActionListener(event -> {
            if (moreOptionListener != null)
                moreOptionListener.doNewYear();
        });

        options.addActionListener( event -> {
            if (moreOptionListener != null)
                moreOptionListener.doOpenOptions();
        });

        popupMenu.add(schoolYears);
        popupMenu.add(newYear);
        popupMenu.add(options);
        popupMenu.addSeparator();
        popupMenu.add(logout);
    }

    private void reloadYearItems () {
        SchoolYear [] years = yearDataModel.getYears();

        Component [] components = schoolYears.getComponents();

        if (components != null && components.length > 1) {
            for (Component c: components) {
                if (c instanceof JRadioButtonMenuItem) {
                    JRadioButtonMenuItem item = (JRadioButtonMenuItem) c;
                    item.removeActionListener(this::doChooseYear);
                    schoolYearGroup.remove(item);
                }
            }

            schoolYears.removeAll();
        }

        SchoolYear current = yearDataModel.getYear();
        if (years != null) {
            for (SchoolYear y : years) {
                JRadioButtonMenuItem item = new JRadioButtonMenuItem(y.getLabel());

                if (current  != null) {
                    if (current.equals(y))
                        item.setSelected(true);
                }

                schoolYears.add(item);
                schoolYearGroup.add(item);
            }
        }
    }

    /**
     * Lors du click sur un item du menu qui représente une année scolaire
     */
    private void doChooseYear (ActionEvent event) {}


    private void doShowPopup () {
        int y = getHeight() - 145;
        popupMenu.show(this, 10, y);
    }

    public SidebarItem getCurrentItem() {
        return currentItem;
    }

    public void setItemChangeListener (SidebarItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    private void initHeader () {
        String filename = "icons/logo-80x100.png";
        final JLabel label = new JLabel(new ImageIcon(filename));
        header.add(label, BorderLayout.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void onAction (SidebarItem item) {
        if (item == currentItem)
            return;

        if (currentItem != null)
            currentItem.setActive(false);

        SidebarItem old = currentItem;
        currentItem = item;
        item.setActive(true);

        if (itemChangeListener != null)
            itemChangeListener.onChange(item, old);
    }

    /**
     * Action d'ajout d'un item au menu principale.
     */
    public Sidebar addItem (String caption, String icon, String name) {
        SidebarItemModel model = new SidebarItemModel(caption, icon, name);
        SidebarItem item = new SidebarItem(model, this::onAction);
        container.add(item, "h 90!");
        if (currentItem == null) {
            currentItem = item;
            item.setActive(true);
        }
        return this;
    }

    @Override
    protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(UIManager.getColor("border_color"));
        g.drawLine(getWidth() - 1, 0, getWidth()-1, getHeight());
    }

    /**
     * Interface d'écoute de changement de l'item actuellement sélectionné
     */
    public interface SidebarItemChangeListener {
        void onChange (SidebarItem currentItem, SidebarItem oldItem);
    }

    public void setMoreOptionListener(SidebarMoreOptionListener moreOptionListener) {
        this.moreOptionListener = moreOptionListener;
    }
}
