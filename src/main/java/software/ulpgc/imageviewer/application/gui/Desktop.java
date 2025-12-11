package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.architecture.Command;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Desktop extends JFrame {
    private final Map<String, Command> commands;
    private final JButton leftButton;
    private final JButton rightButton;

    private Desktop(SwingImageDisplay imageDisplay) throws HeadlessException {
        this.commands = new HashMap<>();
        this.setTitle("Image Viewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        leftButton = btn("<", "prev");
        leftButton.setBounds(20, 250, 60, 100);
        this.getContentPane().add(leftButton);

        rightButton = btn(">", "next");
        rightButton.setBounds(720, 250, 60, 100);
        this.getContentPane().add(rightButton);

        imageDisplay.setBounds(0, 0, 800, 600);
        this.getContentPane().add(imageDisplay);

        imageDisplay.addMouseListener(btnMouseListener());
    }

    private MouseListener btnMouseListener() {
        return (new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                leftButton.setVisible(true);
                rightButton.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                leftButton.setVisible(false);
                rightButton.setVisible(false);
            }
        });
    }


    public static Desktop create(SwingImageDisplay imageDisplay) throws IOException {
        return new Desktop(imageDisplay);
    }

    public Desktop add(String name, Command command) {
        commands.put(name, command);
        return this;
    }

    private JButton btn(String text, String name) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setVisible(true);
        btn.addActionListener(e -> commands.get(name).execute());
        btn.addMouseListener(btnMouseListener());
        btn.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        commands.get("prev").execute();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        commands.get("next").execute();
                }
            }
        });
        return btn;
    }
}