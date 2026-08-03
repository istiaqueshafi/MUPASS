package com.mycompany.buspass;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class AppShell extends JPanel {
    private CardLayout contentLayout = new CardLayout();
    private JPanel content = new JPanel(contentLayout);
    private Sidebar sidebar;
    private Consumer<String> globalNavigate;

    public AppShell(Consumer<String> globalNavigate) {
        this.globalNavigate = globalNavigate;
        setLayout(new BorderLayout());
        content.setOpaque(false);

        sidebar = new Sidebar(this::handleNav);
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    private void handleNav(String key) {
        if (key.equals("logout")) {
            DataStore.setCurrentStudent(null);
            globalNavigate.accept("login");
            return;
        }
        refreshAndShow(key);
    }


    public void refreshAndShow(String key) {
        content.removeAll();
        content.add(new DashboardPanel(this::goTo), "dashboard");
        content.add(new ApplyPassPanel(this::goTo), "apply");
        content.add(new RoutesPanel(), "routes");
        content.add(new StatusPanel(this::goTo), "status");
        content.add(new MyPassPanel(), "mypass");
        content.add(new ProfilePanel(), "profile");
        contentLayout.show(content, key);
        sidebar.setActiveQuiet(key);
        content.revalidate();
        content.repaint();
    
        content.add(new BusTrackingPanel(), "tracking");
        
        contentLayout.show(content, key);
        sidebar.setActiveQuiet(key);
        content.revalidate();
        content.repaint();
    }

    public void goTo(String key) {
        refreshAndShow(key);
    }

    public void onShown() {
        refreshAndShow("dashboard");
    }
}


//shafi