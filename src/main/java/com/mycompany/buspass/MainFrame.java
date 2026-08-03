package com.mycompany.buspass;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout layout = new CardLayout();
    private JPanel root = new JPanel(layout);
    private AppShell shell;

    public MainFrame() {
        setTitle("MuPass - Metropolitan University Bus Pass Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        root.add(new LoginPanel(this::navigate), "login");
        root.add(new RegisterPanel(this::navigate), "register");
        shell = new AppShell(this::navigate);
        root.add(shell, "shell");

        setContentPane(root);
    }
    

    private void navigate(String key) {
        if (key.equals("shell")) {
            layout.show(root, "shell");
            shell.onShown();
        } else {
            layout.show(root, key);
        }
    }
}
