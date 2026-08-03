package com.mycompany.buspass;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class MyPassPanel extends JPanel {

    private static final Color BAND_BG = new Color(0xEA, 0xF2, 0xFE); 
    private static final Color PHOTO_BORDER = new Color(0xC7, 0xD9, 0xFB);
    private static final Color PHOTO_BG = new Color(0xF3, 0xF7, 0xFF);

    public MyPassPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        add(new HeaderBar("My Pass", "Your official Metropolitan University bus pass"), BorderLayout.NORTH);

        Student s = DataStore.getCurrentStudent();
        BusApplication app = s == null ? null : s.getApplication();

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        if (app == null || !"Approved".equals(app.getStatus())) {
            JLabel l = new JLabel("No active pass found. Please apply for a pass from the 'Apply Pass' section.");
            l.setFont(UITheme.FONT_BODY);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(l);
            add(body, BorderLayout.CENTER);
            return;
        }

        RoundedPanel card = new RoundedPanel(Color.WHITE, 16);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1, true));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(2000, 420));

       
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel topLeft = new JPanel();
        topLeft.setOpaque(false);
        topLeft.setLayout(new BoxLayout(topLeft, BoxLayout.Y_AXIS));
        JLabel uni = new JLabel("\uD83C\uDF93 SYLHET METROPOLITAN UNIVERSITY");
        uni.setFont(new Font("SansSerif", Font.BOLD, 17));
        JLabel official = new JLabel("Official Bus Pass \u2014 Academic Year 2025-2028");
        official.setFont(UITheme.FONT_BODY);
        official.setForeground(UITheme.TEXT_MUTED);
        topLeft.add(uni);
        topLeft.add(official);

        JLabel activeChip = new JLabel("\u2705 ACTIVE");
        activeChip.setOpaque(true);
        activeChip.setBackground(new Color(0xDC, 0xF7, 0xE3));
        activeChip.setForeground(new Color(0x15, 0x80, 0x3D));
        activeChip.setFont(UITheme.FONT_BOLD_BODY);
        activeChip.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        top.add(topLeft, BorderLayout.WEST);
        top.add(activeChip, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

   
        JPanel mid = new JPanel(new BorderLayout(20, 0));
        mid.setOpaque(false);
        mid.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));

        RoundedPanel photoBox = new RoundedPanel(PHOTO_BG, 12);
        photoBox.setLayout(new BorderLayout());
        photoBox.setPreferredSize(new Dimension(110, 130));
        photoBox.setBorder(BorderFactory.createLineBorder(PHOTO_BORDER, 1, true));

        JLabel photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);

       
        boolean imageLoaded = false;
        if (s != null && s.getPhotoPath() != null && !s.getPhotoPath().isEmpty()) {
            java.io.File imgFile = new java.io.File(s.getPhotoPath());
            if (imgFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(s.getPhotoPath());
                Image scaledImg = originalIcon.getImage().getScaledInstance(110, 130, Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(scaledImg));
                imageLoaded = true;
            }
        }

     
        if (!imageLoaded) {
            photoLabel.setText("👤");
            photoLabel.setFont(new Font("SansSerif", Font.PLAIN, 40));
            photoLabel.setForeground(UITheme.PRIMARY);
        }

        photoBox.add(photoLabel, BorderLayout.CENTER);
        
        if (!imageLoaded) {
            JLabel photoTxt = new JLabel("Photo", SwingConstants.CENTER);
            photoTxt.setFont(new Font("SansSerif", Font.PLAIN, 10));
            photoTxt.setForeground(UITheme.TEXT_MUTED);
            photoBox.add(photoTxt, BorderLayout.SOUTH);
        }

        JPanel details = new JPanel(new GridLayout(5, 2, 20, 8));
        details.setOpaque(false);
        details.add(kv("Pass Number:", app.getPassNumber()));
        details.add(kv("Bus Number:", app.getBusNumber()));
        details.add(kv("Student Name:", s.getFullName()));
        details.add(kv("Shift:", app.getShift()));
        details.add(kv("Student ID:", s.getStudentId()));
        details.add(kv("Issue Date:", app.fmtApproved()));
        details.add(kv("Department:", s.getDepartment()));
        details.add(kv("Expiry Date:", app.fmtExpiry()));
        details.add(kv("Bus Route:", app.getRoute()));
        details.add(kv("Pickup:", app.getPickupPoint()));

        mid.add(photoBox, BorderLayout.WEST);
        mid.add(details, BorderLayout.CENTER);
        card.add(mid, BorderLayout.CENTER);

        
        RoundedPanel band = new RoundedPanel(BAND_BG, 0);
        band.setLayout(new BorderLayout(14, 0));
        band.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));

      

        VerifiedBadge badge = new VerifiedBadge();
        badge.setPreferredSize(new Dimension(34, 34));

        
        

        card.add(band, BorderLayout.SOUTH);
        body.add(card);
        body.add(Box.createVerticalStrut(16));

      
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton dl = new RoundedButton("\u2B07 Download Pass (Image)", UITheme.PRIMARY);
        dl.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Bus Pass");
            fileChooser.setSelectedFile(new java.io.File("Bus_Pass_" + (s != null ? s.getStudentId() : "pass") + ".png"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".png")) {
                    fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".png");
                }

                try {
                    java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
                            card.getWidth(), card.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
                    
                    Graphics2D g2 = image.createGraphics();
                    card.paint(g2);
                    g2.dispose();

                    javax.imageio.ImageIO.write(image, "png", fileToSave);
                    JOptionPane.showMessageDialog(this, "Pass downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

       
        RoundedButton print = new RoundedButton("\uD83D\uDDA8 Print Pass", new Color(0xF3, 0xF4, 0xF6));
        print.setForeground(UITheme.TEXT_DARK);
        print.addActionListener(e -> {
            try {
                java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
                job.setPrintable((g, pf, page) -> {
                    if (page > 0) return java.awt.print.Printable.NO_SUCH_PAGE;
                    Graphics2D g2 = (Graphics2D) g;
                    g2.translate(pf.getImageableX(), pf.getImageableY());
                    card.printAll(g2);
                    return java.awt.print.Printable.PAGE_EXISTS;
                });
                if (job.printDialog()) job.print();
            } catch (Exception ex) { /* ignore */ }
        });

        btnRow.add(dl);
        btnRow.add(print);
        body.add(btnRow);

        add(body, BorderLayout.CENTER);
    }

    private JPanel kv(String k, String v) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel kl = new JLabel(k);
        kl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        kl.setForeground(UITheme.TEXT_MUTED);
        JLabel vl = new JLabel(v);
        vl.setFont(UITheme.FONT_BOLD_BODY);
        p.add(kl);
        p.add(vl);
        return p;
    }

 

 
    static class VerifiedBadge extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int d = Math.min(getWidth(), getHeight());
            g2.setColor(UITheme.PRIMARY);
            g2.fill(new Ellipse2D.Float(0, 0, d, d));

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = d / 2, cy = d / 2;
            Polygon check = new Polygon();
            g2.drawLine((int) (cx - d * 0.22), cy, (int) (cx - d * 0.05), (int) (cy + d * 0.18));
            g2.drawLine((int) (cx - d * 0.05), (int) (cy + d * 0.18), (int) (cx + d * 0.25), (int) (cy - d * 0.18));
            g2.dispose();
        }
    }
}