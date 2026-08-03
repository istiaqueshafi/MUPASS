package com.mycompany.buspass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.function.Consumer;

public class RegisterPanel extends JPanel {

    public RegisterPanel(Consumer<String> navigate) {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        
        add(buildHeader(), BorderLayout.NORTH);

        
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(UITheme.BG);
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel splitCard = new JPanel(new GridBagLayout());
        splitCard.setPreferredSize(new Dimension(980, 580));
        splitCard.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

       
        gbc.gridx = 0;
        gbc.weightx = 0.80;
        splitCard.add(buildImagePanel(), gbc);

       
        gbc.gridx = 1;
        gbc.weightx = 0.62;
        splitCard.add(buildFormCard(navigate), gbc);

        center.add(splitCard);

      
        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(14);

        add(scroll, BorderLayout.CENTER);
    }

   
    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 14));
        header.setBackground(UITheme.PRIMARY);

        JLabel icon = new JLabel("📝");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JLabel title = new JLabel("Student Registration");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        header.add(icon);
        header.add(title);
        return header;
    }

private JPanel buildImagePanel() {
    URL imgUrl = getClass().getResource("/images/mu.jpg");
    if (imgUrl == null) {
        imgUrl = getClass().getResource("/mu.jpg");
    }

    final Image img = (imgUrl != null) ? new ImageIcon(imgUrl).getImage() : null;

    JPanel imagePanel = new JPanel(null) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            
           
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            RoundRectangle2D clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16);
            g2.setClip(clip);

            if (img != null) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imgWidth = img.getWidth(this);
                int imgHeight = img.getHeight(this);

                if (imgWidth > 0 && imgHeight > 0) {
                   
                    double scale = Math.max((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                    int newWidth = (int) (scale * imgWidth);
                    int newHeight = (int) (scale * imgHeight);

                    int x = (panelWidth - newWidth) / 2;
                    int y = (panelHeight - newHeight) / 2;

                    g2.drawImage(img, x, y, newWidth, newHeight, this);
                }
            } else {
                g2.setColor(UITheme.PRIMARY);
                g2.fill(clip);
            }

            
            GradientPaint gp = new GradientPaint(
                    0, getHeight() * 0.5f, new Color(0, 0, 0, 0),
                    0, getHeight(), new Color(0, 0, 0, 170));
            g2.setPaint(gp);
            g2.fill(clip);
            g2.dispose();
        }
    };
    imagePanel.setOpaque(false);

    JLabel welcome = new JLabel("Join MuPass!");
    welcome.setFont(new Font("SansSerif", Font.BOLD, 22));
    welcome.setForeground(Color.WHITE);

    JLabel sub = new JLabel("<html>Register now to manage<br>your daily bus passes seamlessly</html>");
    sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
    sub.setForeground(new Color(255, 255, 255, 230));

    imagePanel.add(welcome);
    imagePanel.add(sub);

    imagePanel.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            int w = imagePanel.getWidth();
            int h = imagePanel.getHeight();
            welcome.setBounds(20, h - 82, w - 40, 28);
            sub.setBounds(20, h - 52, w - 40, 40);
        }
    });

    return imagePanel;
}

    
    private RoundedPanel buildFormCard(Consumer<String> navigate) {
        RoundedPanel card = new RoundedPanel(Color.WHITE, 16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Create New Account");
        title.setFont(UITheme.FONT_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Please fill in all required fields to register");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JTextField idField = FormUtil.textField();
        JTextField nameField = FormUtil.textField();
        JComboBox<String> deptBox = FormUtil.comboBox(new String[]{"CSE", "EEE", "BBA", "English", "Law"});
        JComboBox<String> batchBox = FormUtil.comboBox(new String[]{"59th", "60th", "61st", "62nd", "63rd"});
        JTextField emailField = FormUtil.textField();
        JTextField phoneField = FormUtil.textField();
        JPasswordField passField = FormUtil.passwordField();
        JPasswordField confirmField = FormUtil.passwordField();

        JCheckBox agree = new JCheckBox("I agree to the Terms of Service and Privacy Policy");
        agree.setOpaque(false);
        agree.setFont(UITheme.FONT_BODY);
        agree.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel row1 = twoCol(FormUtil.labeled("Student ID *", idField), FormUtil.labeled("Full Name *", nameField));
        JPanel row2 = twoCol(FormUtil.labeled("Department *", deptBox), FormUtil.labeled("Batch *", batchBox));
        JPanel row3 = twoCol(FormUtil.labeled("Email Address *", emailField), FormUtil.labeled("Phone Number *", phoneField));
        JPanel row4 = twoCol(FormUtil.labeled("Password *", passField), FormUtil.labeled("Confirm Password *", confirmField));

        JLabel error = new JLabel(" ");
        error.setForeground(UITheme.RED);
        error.setFont(UITheme.FONT_BODY);
        error.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton createBtn = new RoundedButton("🚌 Create Account →", UITheme.PRIMARY);
        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        createBtn.setMaximumSize(new Dimension(2000, 40));
        createBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String pw = new String(passField.getPassword());
            String cpw = new String(confirmField.getPassword());

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || pw.isEmpty()) {
                error.setText("We need a little more information, please complete all required fields.");
                return;
            }
            if (DataStore.idExists(id)) {
                error.setText("Welcome back! An account with this Student ID already exists.");
                return;
            }
            if (!pw.equals(cpw)) {
                error.setText("Password mismatch. Please check and retry.");
                return;
            }
            if (!agree.isSelected()) {
                error.setText("Please review and accept the Terms of Service to continue.");
                return;
            }
            Student s = new Student(id, name, (String) deptBox.getSelectedItem(),
                    (String) batchBox.getSelectedItem(), email, phone, pw);
            DataStore.addStudent(s);
            JOptionPane.showMessageDialog(this, "Account setup complete! You can now log in and continue.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            navigate.accept("login");
        });

        JButton backToLogin = new JButton("Already have an account? Sign in to continue");
        backToLogin.setFont(UITheme.FONT_BOLD_BODY);
        backToLogin.setForeground(UITheme.PRIMARY);
        backToLogin.setBorderPainted(false);
        backToLogin.setContentAreaFilled(false);
        backToLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        backToLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLogin.addActionListener(e -> navigate.accept("login"));

        card.add(title);
        card.add(sub);
        card.add(row1);
        card.add(Box.createVerticalStrut(8));
        card.add(row2);
        card.add(Box.createVerticalStrut(8));
        card.add(row3);
        card.add(Box.createVerticalStrut(8));
        card.add(row4);
        card.add(Box.createVerticalStrut(8));
        card.add(agree);
        card.add(Box.createVerticalStrut(10));
        card.add(createBtn);
        card.add(Box.createVerticalStrut(6));
        card.add(error);
        card.add(backToLogin);

        return card;
    }

    private JPanel twoCol(JComponent a, JComponent b) {
        JPanel p = new JPanel(new GridLayout(1, 2, 12, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(2000, 55));
        p.add(a);
        p.add(b);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bus Pass System - Register");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            RegisterPanel registerPanel = new RegisterPanel(screen -> {
                System.out.println("Navigating to: " + screen);
            });

            frame.add(registerPanel);
            frame.setSize(1050, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}