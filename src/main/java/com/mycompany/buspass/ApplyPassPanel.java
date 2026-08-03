package com.mycompany.buspass;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class ApplyPassPanel extends JPanel {
    private CardLayout steps = new CardLayout();
    private JPanel stepPanel = new JPanel(steps);
    private JLabel[] stepDots = new JLabel[3];
    private int currentStep = 0;


    private JTextField idF, nameF, deptF;
    private JComboBox<String> shiftBox;
    private JLabel photoLabel;
    private String photoPath = null;


    private JComboBox<String> pickupBox;
    private JComboBox<String> routeBox;
    private JLabel busNoLabel, etaLabel;

    private static final String[] ROUTES = {"Route 01 — Tilaghor Line", "Route 02 — MU Express", "Route 03 — Rikabibazar Line", "Route 04— South Surma Express"};
    private static final String[] BUS_NO = {"DH-MET-TIL-11-4410", "DH-MET-KHO-11-6220", "DH-MET-RIK-11-7788", "DH-MET-SRM-11-1288"};
    private static final String[] ETA = {"8:35 AM • 12.2 km • 35 mins", "8:50 AM • 15.7 km • 45 mins", "8:40 AM • 13.4 km • 38 mins"};
    private static final String[] PICKUPS = {"Khojarkhola Square", "Kazir Bazar", "Tilaghor", "Rikabi Bazar", "Shahi Eidgah", "Shibganj", "Humayun Rashid Chhatar", "Baluchar", "Chowhatta", "Shah paran"};

    private Consumer<String> navigate;

    public ApplyPassPanel(Consumer<String> navigate) {
        this.navigate = navigate;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(new HeaderBar("Apply for Bus Pass", "Complete the form to submit your application"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        body.add(buildStepIndicator());
        body.add(Box.createVerticalStrut(16));

        RoundedPanel formCard = new RoundedPanel(Color.WHITE, 14);
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        stepPanel.setOpaque(false);
        stepPanel.add(buildStudentInfoStep(), "0");
        stepPanel.add(buildRouteStep(), "1");
        stepPanel.add(buildReviewStep(), "2");
        formCard.add(stepPanel, BorderLayout.CENTER);

        body.add(formCard);
        add(body, BorderLayout.CENTER);
    }

    private JPanel buildStepIndicator() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] labels = {"1  Student Info", "2  Route Select", "3  Review & Submit"};
        for (int i = 0; i < 3; i++) {
            JLabel l = new JLabel(labels[i]);
            l.setFont(UITheme.FONT_BOLD_BODY);
            l.setOpaque(true);
            l.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
            stepDots[i] = l;
            p.add(l);
        }
        updateStepIndicator();
        return p;
    }

    private void updateStepIndicator() {
        for (int i = 0; i < 3; i++) {
            boolean active = i == currentStep;
            boolean done = i < currentStep;
            stepDots[i].setBackground(active ? UITheme.PRIMARY : (done ? new Color(0xDC, 0xF7, 0xE3) : UITheme.SIDEBAR_BG));
            stepDots[i].setForeground(active ? Color.WHITE : UITheme.TEXT_DARK);
        }
    }

    private void goStep(int idx) {
        currentStep = idx;
        steps.show(stepPanel, String.valueOf(idx));
        updateStepIndicator();
    }

    private JPanel buildStudentInfoStep() {
        Student s = DataStore.getCurrentStudent();
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel h = new JLabel("Student Information");
        h.setFont(UITheme.FONT_HEADER);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(h);
        p.add(Box.createVerticalStrut(12));

        idF = FormUtil.textField();
        nameF = FormUtil.textField();

        if (s != null) {
            if (s.getStudentId() != null) idF.setText(s.getStudentId());
            if (s.getFullName() != null) nameF.setText(s.getFullName());
        }

        JPanel row1 = new JPanel(new GridLayout(1, 2, 14, 0));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row1.setMaximumSize(new Dimension(2000, 60));
        row1.add(FormUtil.labeled("Student ID *", idF));
        row1.add(FormUtil.labeled("Full Name *", nameF));
        p.add(row1);
        p.add(Box.createVerticalStrut(10));

        deptF = FormUtil.textField();
        JTextField emailF = FormUtil.textField();

        if (s != null) {
            if (s.getDepartment() != null) deptF.setText(s.getDepartment());
            if (s.getEmail() != null) emailF.setText(s.getEmail());
        }

        emailF.setEditable(false);

        JPanel row2 = new JPanel(new GridLayout(1, 2, 14, 0));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.setMaximumSize(new Dimension(2000, 60));
        row2.add(FormUtil.labeled("Department *", deptF));
        row2.add(FormUtil.labeled("Email", emailF));
        p.add(row2);
        p.add(Box.createVerticalStrut(10));

        shiftBox = FormUtil.comboBox(new String[]{"Morning (8 AM)", "Day (3 PM)", "Evening (5 PM)"});
        p.add(FormUtil.labeled("Select Shift *", shiftBox));
        p.add(Box.createVerticalStrut(12));

        photoLabel = new JLabel("No photo selected");
        photoLabel.setFont(UITheme.FONT_BODY);
        photoLabel.setForeground(UITheme.TEXT_MUTED);
        photoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton uploadBtn = new JButton("📤 Upload Student Photo + ID Card");
        uploadBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        uploadBtn.setFont(UITheme.FONT_BOLD_BODY);
        uploadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int r = chooser.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                photoPath = f.getAbsolutePath();
                photoLabel.setText("Selected: " + f.getName());
            }
        });
        p.add(uploadBtn);
        p.add(photoLabel);
        p.add(Box.createVerticalStrut(18));

        RoundedButton next = new RoundedButton("Continue → Route Selection", UITheme.PRIMARY);
        next.setAlignmentX(Component.LEFT_ALIGNMENT);
        next.addActionListener(e -> {
            if (idF.getText().trim().isEmpty() || nameF.getText().trim().isEmpty() || deptF.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields (Student ID, Name, Department).", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            goStep(1);
        });
        p.add(next);

        return p;
    }

    private JPanel buildRouteStep() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel h = new JLabel("📍 Select Your Route");
        h.setFont(UITheme.FONT_HEADER);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(h);
        p.add(Box.createVerticalStrut(12));

        pickupBox = FormUtil.comboBox(PICKUPS);
        p.add(FormUtil.labeled("Pickup Point *", pickupBox));
        p.add(Box.createVerticalStrut(10));

        JTextField destF = FormUtil.textField();
        destF.setText("Metropolitan University Main Campus");
        destF.setEditable(false);
        p.add(FormUtil.labeled("Destination *", destF));
        p.add(Box.createVerticalStrut(10));

        routeBox = FormUtil.comboBox(ROUTES);
        busNoLabel = new JLabel(BUS_NO[0]);
        etaLabel = new JLabel(ETA[0]);
        routeBox.addActionListener(e -> {
            int idx = routeBox.getSelectedIndex();
            busNoLabel.setText(BUS_NO[idx]);
            etaLabel.setText(ETA[idx]);
        });
        p.add(FormUtil.labeled("Route *", routeBox));
        p.add(Box.createVerticalStrut(10));

        JPanel infoBox = new RoundedPanel(new Color(0xDB, 0xEA, 0xFE), 10);
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoBox.setMaximumSize(new Dimension(2000, 90));
        JLabel busLbl = new JLabel("🚌 Bus Number: ");
        busLbl.setFont(UITheme.FONT_BOLD_BODY);
        JPanel busRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        busRow.setOpaque(false);
        busRow.add(busLbl);
        busRow.add(busNoLabel);
        JLabel etaLbl = new JLabel("⏱ Estimated: ");
        etaLbl.setFont(UITheme.FONT_BOLD_BODY);
        JPanel etaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        etaRow.setOpaque(false);
        etaRow.add(etaLbl);
        etaRow.add(etaLabel);
        infoBox.add(busRow);
        infoBox.add(etaRow);
        p.add(infoBox);
        p.add(Box.createVerticalStrut(18));

        JPanel navRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navRow.setOpaque(false);
        navRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton back = new JButton("← Back");
        back.setFont(UITheme.FONT_BOLD_BODY);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> goStep(0));
        RoundedButton next = new RoundedButton("Continue → Review", UITheme.PRIMARY);
        next.addActionListener(e -> {
            populateReview();
            goStep(2);
        });
        navRow.add(back);
        navRow.add(next);
        p.add(navRow);

        return p;
    }

    private JPanel reviewContainer;

    private JPanel buildReviewStep() {
        reviewContainer = new JPanel();
        reviewContainer.setOpaque(false);
        reviewContainer.setLayout(new BoxLayout(reviewContainer, BoxLayout.Y_AXIS));
        return reviewContainer;
    }

    private void populateReview() {
        reviewContainer.removeAll();

        JLabel h = new JLabel("✅ Review & Submit");
        h.setFont(UITheme.FONT_HEADER);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewContainer.add(h);
        reviewContainer.add(Box.createVerticalStrut(12));

        String[][] rows = {
                {"Student Name", nameF.getText().trim() + " (" + idF.getText().trim() + ")"},
                {"Department", deptF.getText().trim()},
                {"Shift", (String) shiftBox.getSelectedItem()},
                {"Pickup Point", (String) pickupBox.getSelectedItem()},
                {"Destination", "Metropolitan University Main Campus"},
                {"Route", (String) routeBox.getSelectedItem()},
                {"Bus Number", busNoLabel.getText()},
                {"Documents", photoPath == null ? "Not uploaded" : "Uploaded ✅"}
        };
        for (String[] r : rows) {
            JPanel line = new JPanel(new BorderLayout());
            line.setOpaque(false);
            line.setAlignmentX(Component.LEFT_ALIGNMENT);
            line.setMaximumSize(new Dimension(2000, 26));
            JLabel k = new JLabel(r[0]);
            k.setFont(UITheme.FONT_BODY);
            k.setForeground(UITheme.TEXT_MUTED);
            JLabel v = new JLabel(r[1]);
            v.setFont(UITheme.FONT_BOLD_BODY);
            line.add(k, BorderLayout.WEST);
            line.add(v, BorderLayout.EAST);
            reviewContainer.add(line);
            reviewContainer.add(Box.createVerticalStrut(4));
        }
        reviewContainer.add(Box.createVerticalStrut(14));

        JPanel navRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navRow.setOpaque(false);
        navRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton back = new JButton("← Back");
        back.setFont(UITheme.FONT_BOLD_BODY);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> goStep(1));
        RoundedButton submit = new RoundedButton("✅ Submit Application", UITheme.GREEN);
        submit.addActionListener(e -> submitApplication());
        navRow.add(back);
        navRow.add(submit);
        reviewContainer.add(navRow);

        reviewContainer.revalidate();
        reviewContainer.repaint();
    }

    private void submitApplication() {
        Student s = DataStore.getCurrentStudent();
        int idx = routeBox.getSelectedIndex();
        BusApplication app = new BusApplication(
                DataStore.nextApplicationId(),
                (String) shiftBox.getSelectedItem(),
                (String) pickupBox.getSelectedItem(),
                "Metropolitan University Main Campus",
                ROUTES[idx],
                BUS_NO[idx]
        );
        app.setVerifiedAt(LocalDateTime.now());
        app.setStatus("Approved");
        app.setApprovedAt(LocalDateTime.now());
        app.setPassNumber(DataStore.nextPassNumber());
        app.setExpiryDate(LocalDateTime.now().plusYears(4));

        if (s != null) {
            s.setFullName(nameF.getText().trim());
            s.setDepartment(deptF.getText().trim());
            s.setApplication(app);
            if (photoPath != null) s.setPhotoPath(photoPath);
            DataStore.updateCurrentStudent();
        }

        JOptionPane.showMessageDialog(this,
                "We are pleased to inform you that the application has been successfully submitted and approved.\nPass Number: " + app.getPassNumber(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
        navigate.accept("mypass");
    }
}