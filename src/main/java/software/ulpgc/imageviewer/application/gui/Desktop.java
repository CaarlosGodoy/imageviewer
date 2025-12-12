package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.architecture.Command;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Desktop extends JFrame implements KeyListener {
    private final Map<String, Command> commands;
    private final JButton leftBtn;
    private final JButton rightBtn;
    private final JButton blackAndWhiteBtn;

    private Desktop(SwingImageDisplay imageDisplay) throws HeadlessException {
        this.commands = new HashMap<>();
        this.setTitle("Image Viewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setFocusable(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        int width = 800;
        int height = 600;
        panel.setPreferredSize(new Dimension(width, height));

        blackAndWhiteBtn = btn("B/N", "blackAndWhite");
        blackAndWhiteBtn.setBounds(350, 20, 100, 30);
        panel.add(blackAndWhiteBtn);

        leftBtn = btn("<", "prev");
        leftBtn.setBounds(20, (height - 100) / 2, 60, 100);
        panel.add(leftBtn);

        rightBtn = btn(">", "next");
        rightBtn.setBounds(width - 80, (height - 100) / 2, 60, 100);
        panel.add(rightBtn);

        imageDisplay.setBounds(0, 0, width, height);
        panel.add(imageDisplay);

        panel.addMouseListener(mouseListener(panel));

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private MouseListener mouseListener(JPanel panel) {
        return (new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                leftBtn.setVisible(true);
                rightBtn.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (e.getX() < 0 || e.getX() >= panel.getWidth() || e.getY() < 0 || e.getY() >= panel.getHeight()) {
                    leftBtn.setVisible(false);
                    rightBtn.setVisible(false);
                }
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
        btn.setFocusable(false);
        btn.addActionListener(e -> commands.get(name).execute());

        return btn;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            commands.get("prev").execute();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            commands.get("next").execute();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}