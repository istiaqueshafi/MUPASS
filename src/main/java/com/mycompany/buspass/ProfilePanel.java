package com.mycompany.buspass;import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ProfilePanel extends JPanel {
    public ProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        add(new HeaderBar("My Profile", "View and update your personal information"), BorderLayout.NORTH);

        Student s = DataStore.getCurrentStudent();

        JPanel body = new JPanel(new GridLayout(1, 2, 18, 0));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        
        RoundedPanel left = new RoundedPanel(Color.WHITE, 14);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(24, 18, 24, 18)));

        JComponent avatar = new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xDB, 0xEA, 0xFE));
                g2.fill(new Ellipse2D.Float(0, 0, 90, 90));
                g2.setColor(UITheme.PRIMARY);
                g2.setFont(new Font("SansSerif", Font.BOLD, 34));
                String init = s.getFullName().substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(init, (90 - fm.stringWidth(init)) / 2f, 58);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(90, 90));
        avatar.setMaximumSize(new Dimension(90, 90));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel(s.getFullName());
        name.setFont(UITheme.FONT_HEADER);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel id = new JLabel(s.getStudentId());
        id.setFont(UITheme.FONT_BOLD_BODY);
        id.setForeground(UITheme.PRIMARY);
        id.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel dept = new JLabel(s.getDepartment() + " \u2022 " + s.getBatch());
        dept.setFont(UITheme.FONT_BODY);
        dept.setForeground(UITheme.TEXT_MUTED);
        dept.setAlignmentX(Component.CENTER_ALIGNMENT);

        left.add(avatar);
        left.add(Box.createVerticalStrut(12));
        left.add(name);
        left.add(id);
        left.add(dept);
        left.add(Box.createVerticalStrut(16));
        JLabel passChip = new JLabel(s.getApplication() != null && "Approved".equals(s.getApplication().getStatus())
                ? "\u2705 Pass Active" : "No active pass", SwingConstants.CENTER);
        passChip.setOpaque(true);
        passChip.setBackground(new Color(0xDC, 0xF7, 0xE3));
        passChip.setForeground(new Color(0x15, 0x80, 0x3D));
        passChip.setAlignmentX(Component.CENTER_ALIGNMENT);
        passChip.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        passChip.setMaximumSize(new Dimension(160, 30));
        left.add(passChip);

     
        RoundedPanel right = new RoundedPanel(Color.WHITE, 14);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));

        JLabel editTitle = new JLabel("\u270F\uFE0F Edit Profile");
        editTitle.setFont(UITheme.FONT_HEADER);
        editTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        right.add(editTitle);
        right.add(Box.createVerticalStrut(14));

        JTextField nameF = FormUtil.textField();
        nameF.setText(s.getFullName());
        JTextField emailF = FormUtil.textField();
        emailF.setText(s.getEmail());
        JTextField phoneF = FormUtil.textField();
        phoneF.setText(s.getPhone());

        right.add(FormUtil.labeled("Full Name", nameF));
        right.add(Box.createVerticalStrut(10));
        right.add(FormUtil.labeled("Email", emailF));
        right.add(Box.createVerticalStrut(10));
        right.add(FormUtil.labeled("Phone", phoneF));
        right.add(Box.createVerticalStrut(16));

        JLabel pwTitle = new JLabel("\uD83D\uDD12 Change Password ");
        pwTitle.setFont(UITheme.FONT_BOLD_BODY);
        pwTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        right.add(pwTitle);
        right.add(Box.createVerticalStrut(8));

        JPasswordField newPw = FormUtil.passwordField();
        JPasswordField confirmPw = FormUtil.passwordField();
        JPanel pwRow = new JPanel(new GridLayout(1, 2, 14, 0));
        pwRow.setOpaque(false);
        pwRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        pwRow.setMaximumSize(new Dimension(2000, 60));
        pwRow.add(FormUtil.labeled("New Password", newPw));
        pwRow.add(FormUtil.labeled("Confirm Password", confirmPw));
        right.add(pwRow);
        right.add(Box.createVerticalStrut(18));

        JLabel msg = new JLabel(" ");
        msg.setFont(UITheme.FONT_BODY);
        msg.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton save = new RoundedButton("\uD83D\uDCBE Save Changes", UITheme.PRIMARY);
        save.setAlignmentX(Component.LEFT_ALIGNMENT);
        save.addActionListener(e -> {
            if (nameF.getText().trim().isEmpty() || emailF.getText().trim().isEmpty()) {
                msg.setForeground(UITheme.RED);
                msg.setText("Name and email are required.");
                return;
            }
            String npw = new String(newPw.getPassword());
            String cpw = new String(confirmPw.getPassword());
            if (!npw.isEmpty()) {
                if (!npw.equals(cpw)) {
                    msg.setForeground(UITheme.RED);
                    msg.setText("New passwords do not match.");
                    return;
                }
                s.setPassword(npw);
            }
            s.setFullName(nameF.getText().trim());
            s.setEmail(emailF.getText().trim());
            s.setPhone(phoneF.getText().trim());
            DataStore.updateCurrentStudent();
            msg.setForeground(UITheme.GREEN);
            msg.setText("Your profile has been updated");
        });

        right.add(save);
        right.add(Box.createVerticalStrut(8));
        right.add(msg);

        body.add(left);
        body.add(right);
        add(body, BorderLayout.CENTER);
    }
}
