package com.mycompany.buspass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {

    public LoginPanel(Consumer<String> navigate) {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(UITheme.BG);
        center.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel splitCard = new JPanel(new GridBagLayout());
        splitCard.setPreferredSize(new Dimension(920, 480));
        splitCard.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 0.42;
        gbc.insets = new Insets(0, 0, 0, 0);
        splitCard.add(buildImagePanel(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.58;
        splitCard.add(buildFormCard(navigate), gbc);

        center.add(splitCard);
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 16));
        header.setBackground(UITheme.PRIMARY);

        JLabel lock = new JLabel("\uD83D\uDD12");
        lock.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JLabel title = new JLabel("Student Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        header.add(lock);
        header.add(title);
        return header;
    }

    private JPanel buildImagePanel() {
        java.net.URL imgUrl = getClass().getResource("/images/campus_bus.jpg");
        final Image img = (imgUrl != null) ? new ImageIcon(imgUrl).getImage() : null;
        if (imgUrl == null) {
            System.err.println("[LoginPanel] Warning: /images/campus_bus.jpg not found on classpath. "
                    + "Make sure src/main/resources/images/campus_bus.jpg exists and the project was rebuilt.");
        }

        JPanel imagePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                RoundRectangle2D clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setClip(clip);

                if (img != null) {
                    g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2.setColor(UITheme.PRIMARY);
                    g2.fill(clip);
                }

                GradientPaint gp = new GradientPaint(
                        0, getHeight() * 0.55f, new Color(0, 0, 0, 0),
                        0, getHeight(), new Color(0, 0, 0, 160));
                g2.setPaint(gp);
                g2.fill(clip);
                g2.dispose();
            }
        };
        imagePanel.setOpaque(false);

        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcome.setForeground(Color.WHITE);

        JLabel sub = new JLabel("<html>Sign in to access your<br>bus pass dashboard</html>");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(255, 255, 255, 230));

        imagePanel.add(welcome);
        imagePanel.add(sub);

        imagePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = imagePanel.getWidth();
                int h = imagePanel.getHeight();
                welcome.setBounds(20, h - 78, w - 40, 28);
                sub.setBounds(20, h - 48, w - 40, 40);
            }
        });

        return imagePanel;
    }

    private RoundedPanel buildFormCard(Consumer<String> navigate) {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel title = new JLabel("Sign In");
        title.setFont(UITheme.FONT_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Enter your credentials to continue");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JTextField idField = FormUtil.textField();
        JPasswordField passField = FormUtil.passwordField();

        card.add(title);
        card.add(sub);
        card.add(FormUtil.labeled("Student ID", idField));
        card.add(Box.createVerticalStrut(12));
        card.add(FormUtil.labeled("Password", passField));

        JButton forgotPassBtn = new JButton("Forgot Password?");
        forgotPassBtn.setFont(UITheme.FONT_BODY);
        forgotPassBtn.setForeground(UITheme.PRIMARY);
        forgotPassBtn.setBorderPainted(false);
        forgotPassBtn.setContentAreaFilled(false);
        forgotPassBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

       
        forgotPassBtn.addActionListener(e -> handleForgotPassword(idField.getText().trim()));

        card.add(Box.createVerticalStrut(4));
        card.add(forgotPassBtn);
        card.add(Box.createVerticalStrut(12));

        JLabel error = new JLabel(" ");
        error.setForeground(UITheme.RED);
        error.setFont(UITheme.FONT_BODY);
        error.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton signIn = new RoundedButton("Sign In \u2192", UITheme.PRIMARY);
        signIn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signIn.setMaximumSize(new Dimension(2000, 42));
        signIn.addActionListener(e -> {
            String id = idField.getText().trim();
            String pw = new String(passField.getPassword());
            if (id.isEmpty() && pw.isEmpty()) {
                error.setText("Please fill in all fields");
                return;
            } else if (id.isEmpty()) {
                error.setText("Please enter your Student ID");
                return;
            } else if (pw.isEmpty()) {
                error.setText("Please enter your Password");
                return;
            }
            Student s = DataStore.authenticate(id, pw);
            if (s == null) {
                error.setText("Invalid Student ID or Password.");
                return;
            }
            DataStore.setCurrentStudent(s);
            navigate.accept("shell");
        });

        card.add(signIn);
        card.add(Box.createVerticalStrut(10));
        card.add(error);
        card.add(Box.createVerticalStrut(8));

        JPanel regRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        regRow.setOpaque(false);
        regRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel q = new JLabel("Don't have an account?");
        q.setFont(UITheme.FONT_BODY);

        JButton link = new JButton("Register here \u2192");
        link.setFont(UITheme.FONT_BOLD_BODY);
        link.setForeground(UITheme.PRIMARY);
        link.setBorderPainted(false);
        link.setContentAreaFilled(false);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addActionListener(e -> navigate.accept("register"));

        regRow.add(q);
        regRow.add(link);
        card.add(regRow);

        return card;
    }

private void handleForgotPassword(String currentInputId) {
    Window windowAncestor = SwingUtilities.getWindowAncestor(this);
    if (!(windowAncestor instanceof JFrame)) return;
    JFrame topFrame = (JFrame) windowAncestor;

    JPanel overlayPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(15, 23, 42, 140));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    };
    overlayPanel.setOpaque(false);
    overlayPanel.setLayout(new GridBagLayout());

    JDialog dialog = new JDialog(topFrame, true);
    dialog.setUndecorated(true);
    dialog.setBackground(new Color(0, 0, 0, 0));

    RoundedPanel mainPanel = new RoundedPanel(new Color(245, 247, 250), 24);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

 
    JPanel iconPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(UITheme.PRIMARY);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.dispose();
        }
    };
    iconPanel.setPreferredSize(new Dimension(50, 50));
    iconPanel.setMaximumSize(new Dimension(50, 50));
    iconPanel.setLayout(new GridBagLayout());
    iconPanel.setOpaque(false);
    iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel lockIcon = new JLabel("\uD83D\uDD12");
    lockIcon.setFont(new Font("SansSerif", Font.PLAIN, 22));
    lockIcon.setForeground(Color.WHITE);
    iconPanel.add(lockIcon);


    JLabel titleLabel = new JLabel("Reset Password");
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    titleLabel.setForeground(new Color(30, 41, 59));
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    RoundedPanel formCard = new RoundedPanel(Color.WHITE, 16);
    formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
    formCard.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    formCard.setAlignmentX(Component.CENTER_ALIGNMENT);

    JTextField inputIdField = FormUtil.textField();
    inputIdField.setText(currentInputId);

    JPasswordField newPassField = FormUtil.passwordField();

    formCard.add(FormUtil.labeled("Enter your Student ID", inputIdField));
    formCard.add(Box.createVerticalStrut(10));
    formCard.add(FormUtil.labeled("Enter New Password", newPassField));

 
    JLabel msgLabel = new JLabel(" ");
    msgLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    Runnable closeDialog = () -> {
        topFrame.getGlassPane().setVisible(false);
        dialog.dispose();
    };

   
    JPanel buttonRow = new JPanel(new GridLayout(1, 2, 10, 0));
    buttonRow.setOpaque(false);
    buttonRow.setMaximumSize(new Dimension(2000, 42));

    RoundedButton cancelBtn = new RoundedButton("Cancel", new Color(230, 235, 242));
    cancelBtn.setForeground(new Color(51, 65, 85));
    cancelBtn.addActionListener(e -> closeDialog.run());

    RoundedButton updateBtn = new RoundedButton("Update Password", UITheme.PRIMARY);
    updateBtn.addActionListener(e -> {
        String studentId = inputIdField.getText().trim();
        String newPassword = new String(newPassField.getPassword()).trim();

        if (studentId.isEmpty() || newPassword.isEmpty()) {
            msgLabel.setForeground(new Color(225, 29, 72)); // Rose red
            msgLabel.setText(" Fields cannot be empty!");
            return;
        }

        boolean success = DataStore.resetPassword(studentId, newPassword);
        if (success) {
            msgLabel.setForeground(new Color(16, 185, 129)); // Emerald green
            msgLabel.setText("✓ Password updated successfully!");
            
         
            Timer timer = new Timer(1000, ev -> closeDialog.run());
            timer.setRepeats(false);
            timer.start();
        } else {
            msgLabel.setForeground(new Color(225, 29, 72)); // Rose red
            msgLabel.setText(" Student ID not found!");
        }
    });

    buttonRow.add(cancelBtn);
    buttonRow.add(updateBtn);


    mainPanel.add(iconPanel);
    mainPanel.add(Box.createVerticalStrut(10));
    mainPanel.add(titleLabel);
    mainPanel.add(Box.createVerticalStrut(14));
    mainPanel.add(formCard);
    mainPanel.add(Box.createVerticalStrut(8));
    mainPanel.add(msgLabel); 
    mainPanel.add(Box.createVerticalStrut(12));
    mainPanel.add(buttonRow);

    dialog.setContentPane(mainPanel);
    dialog.setSize(380, 385);
    dialog.setLocationRelativeTo(topFrame);

    topFrame.setGlassPane(overlayPanel);
    topFrame.getGlassPane().setVisible(true);

    dialog.setVisible(true);
}}