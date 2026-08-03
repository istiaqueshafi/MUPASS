package com.mycompany.buspass; import javax.swing.*;
import java.awt.*;

public class StatCard extends RoundedPanel {
    public StatCard(String icon, String label, String value, String sub, Color bg, Color fg) {
        super(bg);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);
        JLabel iconL = new JLabel(icon);
        iconL.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JLabel labelL = new JLabel(label);
        labelL.setFont(UITheme.FONT_BODY);
        labelL.setForeground(fg);
        row.add(iconL);
        row.add(labelL);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueL = new JLabel(value);
        valueL.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueL.setForeground(fg);
        valueL.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subL = new JLabel(sub);
        subL.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subL.setForeground(fg);
        subL.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(row);
        add(Box.createVerticalStrut(6));
        add(valueL);
        add(Box.createVerticalStrut(2));
        add(subL);
    }
}
