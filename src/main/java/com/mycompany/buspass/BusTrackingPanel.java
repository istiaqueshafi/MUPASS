package com.mycompany.buspass;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BusTrackingPanel extends JPanel {

    
    private final String[] routeStops = {
        "Campus Main Gate", 
        "Subidbazar Point", 
        "Ambarkhana", 
        "Chowhatta", 
        "Court Point", 
        "Railway Station"
    };
    
    private int currentStopIndex = 0;
    private boolean isMoving = false;

  
    private JLabel lblBusNo;
    private JLabel lblCurrentLocation;
    private JLabel lblNextStop;
    private JLabel lblEta;
    private JLabel lblStatusBadge;
    private JProgressBar progressBar;
    private JButton btnToggleSimulation;
    private Timer trackingTimer;

    public BusTrackingPanel() {
        
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        
        String busNo = "DH-MET-TIL-11-4410"; 
        try {
            Student currentStudent = DataStore.getCurrentStudent();
            if (currentStudent != null && currentStudent.getApplication() != null && currentStudent.getApplication().getBusNumber() != null) {
                busNo = currentStudent.getApplication().getBusNumber();
            }
        } catch (Exception ex) {
            
        }


        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Live Bus Tracking");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 37, 41));

        lblStatusBadge = new JLabel(" IDLE ", JLabel.CENTER);
        lblStatusBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatusBadge.setOpaque(true);
        lblStatusBadge.setBackground(new Color(220, 53, 69));
        lblStatusBadge.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblStatusBadge, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

     
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        
        lblBusNo = createInfoLabel("Bus Number: ", busNo);
        lblCurrentLocation = createInfoLabel("Current Location: ", routeStops[0]);
        lblNextStop = createInfoLabel("Next Stop: ", routeStops[1]);
        lblEta = createInfoLabel("Estimated Arrival (ETA): ", "3 mins");

        
        progressBar = new JProgressBar(0, routeStops.length - 1);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("Start: " + routeStops[0]);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, 25));

        
        cardPanel.add(lblBusNo);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(lblCurrentLocation);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(lblNextStop);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(lblEta);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(progressBar);

        add(cardPanel, BorderLayout.CENTER);

     
        btnToggleSimulation = new JButton("Start Live Simulation");
        btnToggleSimulation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnToggleSimulation.setFocusPainted(false);
        btnToggleSimulation.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnToggleSimulation.addActionListener(e -> toggleSimulation());
        add(btnToggleSimulation, BorderLayout.SOUTH);

       
        trackingTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advanceBus();
            }
        });
    }

    private JLabel createInfoLabel(String prefix, String value) {
        JLabel label = new JLabel("<html><b>" + prefix + "</b> " + value + "</html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private void toggleSimulation() {
        if (isMoving) {
            trackingTimer.stop();
            isMoving = false;
            btnToggleSimulation.setText("Resume Live Simulation");
            lblStatusBadge.setText(" PAUSED ");
            lblStatusBadge.setBackground(new Color(255, 193, 7));
            lblStatusBadge.setForeground(Color.BLACK);
        } else {
            trackingTimer.start();
            isMoving = true;
            btnToggleSimulation.setText("Pause Simulation");
            lblStatusBadge.setText(" LIVE ");
            lblStatusBadge.setBackground(new Color(40, 167, 69));
            lblStatusBadge.setForeground(Color.WHITE);
        }
    }

    private void advanceBus() {
        if (currentStopIndex < routeStops.length - 1) {
            currentStopIndex++;
        } else {
            currentStopIndex = 0; 
        }

        String current = routeStops[currentStopIndex];
        String next = (currentStopIndex < routeStops.length - 1) 
                ? routeStops[currentStopIndex + 1] 
                : "Terminal (End of Route)";

        lblCurrentLocation.setText("<html><b>Current Location:</b> " + current + "</html>");
        lblNextStop.setText("<html><b>Next Stop:</b> " + next + "</html>");
        
        int etaMins = (int) (Math.random() * 4) + 2; 
        lblEta.setText("<html><b>Estimated Arrival (ETA):</b> " + (next.equals("Terminal (End of Route)") ? "N/A" : etaMins + " mins") + "</html>");

        progressBar.setValue(currentStopIndex);
        progressBar.setString("Bus at: " + current);
    }
}