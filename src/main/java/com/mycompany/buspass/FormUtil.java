package com.mycompany.buspass;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class FormUtil {
    public static JTextField textField() {
        JTextField f = new JTextField();
        style(f);
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        style(f);
        return f;
    }

    private static void style(JTextField f) {
        f.setFont(UITheme.FONT_BODY);
        f.setMaximumSize(new Dimension(2000, 34));
        f.setPreferredSize(new Dimension(200, 34));
        f.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(4, 10, 4, 10)));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public static <T> JComboBox<T> comboBox(T[] items) {
        JComboBox<T> box = new JComboBox<>(items);
        box.setFont(UITheme.FONT_BODY);
        box.setMaximumSize(new Dimension(2000, 34));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        return box;
    }

    public static JPanel labeled(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_LABEL);
        l.setForeground(UITheme.TEXT_DARK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(4));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(field);
        return p;
    }
}
