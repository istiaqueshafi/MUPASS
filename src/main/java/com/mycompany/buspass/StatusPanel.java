package com.mycompany.buspass;import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class StatusPanel extends JPanel {
    public StatusPanel(Consumer<String> navigate) {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        add(new HeaderBar("Application Status", "Track your bus pass application in real time"), BorderLayout.NORTH);

        Student s = DataStore.getCurrentStudent();
        BusApplication app = s == null ? null : s.getApplication();

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        if (app == null) {
            RoundedPanel empty = new RoundedPanel(Color.WHITE, 14);
            empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
            empty.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                    BorderFactory.createEmptyBorder(24, 24, 24, 24)));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel l = new JLabel("No application found. Please apply from the 'Apply Pass' section.");
            l.setFont(UITheme.FONT_BODY);
            empty.add(l);
            empty.add(Box.createVerticalStrut(12));
            RoundedButton btn = new RoundedButton("\uD83D\uDE8C Apply Pass", UITheme.PRIMARY);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.addActionListener(e -> navigate.accept("apply"));
            empty.add(btn);
            body.add(empty);
            add(body, BorderLayout.CENTER);
            return;
        }

        RoundedPanel head = new RoundedPanel(Color.WHITE, 14);
        head.setLayout(new BorderLayout());
        head.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        head.setAlignmentX(Component.LEFT_ALIGNMENT);
        head.setMaximumSize(new Dimension(2000, 90));
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel appId = new JLabel("\uD83D\uDCCB Application ID: " + app.getApplicationId());
        appId.setFont(UITheme.FONT_HEADER);
        JLabel submitted = new JLabel("Submitted: " + app.fmtSubmitted());
        submitted.setFont(UITheme.FONT_BODY);
        submitted.setForeground(UITheme.TEXT_MUTED);
        left.add(appId);
        left.add(submitted);
        JLabel statusChip = new JLabel("\u2705 " + app.getStatus().toUpperCase());
        statusChip.setOpaque(true);
        statusChip.setBackground(new Color(0xDC, 0xF7, 0xE3));
        statusChip.setForeground(new Color(0x15, 0x80, 0x3D));
        statusChip.setFont(UITheme.FONT_BOLD_BODY);
        statusChip.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        head.add(left, BorderLayout.WEST);
        head.add(statusChip, BorderLayout.EAST);
        body.add(head);
        body.add(Box.createVerticalStrut(16));

        RoundedPanel timeline = new RoundedPanel(Color.WHITE, 14);
        timeline.setLayout(new BoxLayout(timeline, BoxLayout.Y_AXIS));
        timeline.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        timeline.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel tTitle = new JLabel("\uD83D\uDCC5 Application Timeline");
        tTitle.setFont(UITheme.FONT_HEADER);
        tTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        timeline.add(tTitle);
        timeline.add(Box.createVerticalStrut(10));
        timeline.add(step("\u2705", "Application Submitted", app.fmtSubmitted()));
        timeline.add(step("\u2705", "Document Verification", app.fmtVerified()));
        timeline.add(step("\u2705", "Approved", app.fmtApproved()));
        timeline.add(step("\uD83C\uDFAB", "Pass Generated & Ready", app.fmtApproved()));

        body.add(timeline);
        add(body, BorderLayout.CENTER);
    }

    private JPanel step(String icon, String title, String time) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(2000, 40));
        p.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        JLabel i = new JLabel(icon);
        i.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JPanel textP = new JPanel();
        textP.setOpaque(false);
        textP.setLayout(new BoxLayout(textP, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_BOLD_BODY);
        JLabel ti = new JLabel(time);
        ti.setFont(new Font("SansSerif", Font.PLAIN, 11));
        ti.setForeground(UITheme.TEXT_MUTED);
        textP.add(t);
        textP.add(ti);
        p.add(i, BorderLayout.WEST);
        p.add(textP, BorderLayout.CENTER);
        return p;
    }
}
