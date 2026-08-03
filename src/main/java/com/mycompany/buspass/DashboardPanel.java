package com.mycompany.buspass;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class DashboardPanel extends JPanel {
    public DashboardPanel(Consumer<String> navigate) {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        Student s = DataStore.getCurrentStudent();
        String name = s == null ? "" : s.getFullName();

        add(new HeaderBar("Student Dashboard", "Welcome back, " + name + " \uD83D\uDC4B"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        BusApplication app = s == null ? null : s.getApplication();

        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setOpaque(false);
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.setMaximumSize(new Dimension(2000, 110));

        String passStatus = app == null ? "NONE" : ("Approved".equals(app.getStatus()) ? "ACTIVE" : app.getStatus().toUpperCase());
        stats.add(new StatCard("\uD83C\uDFAB", "Active Pass", passStatus,
                app != null && "Approved".equals(app.getStatus()) ? "Expires: " + app.fmtExpiry() : "Apply from menu",
                new Color(0xDC, 0xF7, 0xE3), new Color(0x15, 0x80, 0x3D)));

        stats.add(new StatCard("\uD83D\uDCCB", "Application", app == null ? "None" : app.getStatus(),
                app == null ? "Not submitted" : "Submitted: " + app.fmtSubmitted(),
                new Color(0xDB, 0xEA, 0xFE), new Color(0x1D, 0x4E, 0xD8)));

        stats.add(new StatCard("\uD83D\uDCC5", "Expiry Date", app != null && app.getExpiryDate() != null ? app.fmtExpiry() : "-",
                app != null && app.getExpiryDate() != null ? "Pass valid" : "No active pass",
                new Color(0xFE, 0xF3, 0xC7), new Color(0xB4, 0x54, 0x09)));

        stats.add(new StatCard("\uD83D\uDD14", "Notifications", app == null ? "0 New" : "1 New",
                "View all alerts", new Color(0xFE, 0xE2, 0xE2), new Color(0xB9, 0x1C, 0x1C)));

        body.add(stats);
        body.add(Box.createVerticalStrut(18));

        JPanel row = new JPanel(new GridLayout(1, 2, 18, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(2000, 320));

        RoundedPanel passCard = new RoundedPanel(Color.WHITE, 14);
        passCard.setLayout(new BoxLayout(passCard, BoxLayout.Y_AXIS));
        passCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        JLabel passTitle = new JLabel("\uD83C\uDFAB My Current Bus Pass");
        passTitle.setFont(UITheme.FONT_HEADER);
        passTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        passCard.add(passTitle);
        passCard.add(Box.createVerticalStrut(12));

        if (app == null) {
            JLabel none = new JLabel("<html>You haven't applied for a bus pass yet.<br>Go to <b>Apply Pass</b> from the menu to get started.</html>");
            none.setFont(UITheme.FONT_BODY);
            none.setForeground(UITheme.TEXT_MUTED);
            none.setAlignmentX(Component.LEFT_ALIGNMENT);
            passCard.add(none);
            passCard.add(Box.createVerticalStrut(14));
            RoundedButton applyBtn = new RoundedButton("\uD83D\uDE8C Apply Now", UITheme.PRIMARY);
            applyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            applyBtn.addActionListener(e -> navigate.accept("apply"));
            passCard.add(applyBtn);
        } else {
            passCard.add(infoRow("Pass No.:", app.getPassNumber() == null ? "Pending" : app.getPassNumber(),
                    "Route:", app.getPickupPoint() + " \u2192 " + app.getDestination()));
            passCard.add(Box.createVerticalStrut(8));
            passCard.add(infoRow("Bus No.:", app.getBusNumber(), "Shift:", app.getShift()));
            passCard.add(Box.createVerticalStrut(8));
            passCard.add(infoRow("Dept.:", s.getDepartment(), "Status:",
                    ("Approved".equals(app.getStatus()) ? "\u2705 Active" : app.getStatus())));
        }

        RoundedPanel actCard = new RoundedPanel(Color.WHITE, 14);
        actCard.setLayout(new BoxLayout(actCard, BoxLayout.Y_AXIS));
        actCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        JLabel actTitle = new JLabel("\uD83D\uDCCB Recent Activity");
        actTitle.setFont(UITheme.FONT_HEADER);
        actTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        actCard.add(actTitle);
        actCard.add(Box.createVerticalStrut(12));
        if (app == null) {
            actCard.add(activityLine("\uD83D\uDD14", "Registered Successfully", "Account created"));
        } else {
            actCard.add(activityLine("\uD83D\uDCE4", "Application Submitted", app.fmtSubmitted()));
            actCard.add(Box.createVerticalStrut(8));
            if (app.getVerifiedAt() != null)
                actCard.add(activityLine("\uD83D\uDCDD", "Document Verified", app.fmtVerified()));
            if (app.getApprovedAt() != null) {
                actCard.add(Box.createVerticalStrut(8));
                actCard.add(activityLine("\u2705", "Pass Approved", app.fmtApproved()));
            }
        }

        row.add(passCard);
        row.add(actCard);
        body.add(row);

        add(body, BorderLayout.CENTER);
    }

    private JPanel infoRow(String l1, String v1, String l2, String v2) {
        JPanel p = new JPanel(new GridLayout(1, 2, 10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(kv(l1, v1));
        p.add(kv(l2, v2));
        return p;
    }

    private JPanel kv(String k, String v) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel kl = new JLabel(k);
        kl.setFont(UITheme.FONT_BODY);
        kl.setForeground(UITheme.TEXT_MUTED);
        JLabel vl = new JLabel(v);
        vl.setFont(UITheme.FONT_BOLD_BODY);
        p.add(kl);
        p.add(vl);
        return p;
    }
    

    private JPanel activityLine(String icon, String title, String time) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel i = new JLabel(icon);
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
