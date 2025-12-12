package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.architecture.Canvas;
import software.ulpgc.imageviewer.architecture.ImageDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private Shift shift;
    private Released released;
    private Paint[] paints;
    private double zoomFactor = 1.0;
    private final double zoomStep = 0.1;
    private final Map<Integer, BufferedImage> images = new HashMap<>();

    public SwingImageDisplay() {
        this.addMouseListener(new MouseAdapter());
        this.addMouseMotionListener(new MouseAdapter());
        this.addMouseWheelListener(new ZoomWheelListener());
    }

    @Override
    public void paint(Paint... paints) {
        this.paints = paints;
        this.repaint();
    }

    @Override
    public int width() {
        return this.getWidth();
    }

    private BufferedImage toBufferedImage(byte[] bitmap) {
        return images.computeIfAbsent(Arrays.hashCode(bitmap), _ -> read(bitmap));
    }

    private BufferedImage read(byte[] bitmap) {
        try (InputStream is = new ByteArrayInputStream(bitmap)) {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.GRAY);
        g.fillRect(0,0,this.getWidth(), this.getHeight());

        if (paints == null) return;

        for (Paint paint : paints) {
            BufferedImage bitmap = toBufferedImage(paint.bitmap());

            Canvas canvas = Canvas.ofSize(this.getWidth(), this.getHeight())
                    .fit(bitmap.getWidth(), bitmap.getHeight());

            int scaledWidth = (int) (canvas.width() * zoomFactor);
            int scaledHeight = (int) (canvas.height() * zoomFactor);

            int x = (this.getWidth() - scaledWidth) / 2;
            int y = (this.getHeight() - scaledHeight) / 2;

            g.drawImage(bitmap, x + paint.offset(), y, scaledWidth, scaledHeight, null);
        }
    }

    public void resetZoom() {
        this.zoomFactor = 1.0;
        this.repaint();
    }

    private class MouseAdapter implements MouseListener, MouseMotionListener {
        private int x;

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {x = e.getX();}

        @Override
        public void mouseReleased(MouseEvent e) {SwingImageDisplay.this.released.offset(e.getX() - x);}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {SwingImageDisplay.this.shift.offset(e.getX() - x);}

        @Override
        public void mouseMoved(MouseEvent e) {}
    }

    private class ZoomWheelListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() < 0) {
                zoomFactor += (zoomFactor < 4.95) ? zoomStep : 0.0;
            } else zoomFactor -= (zoomFactor > 0.15) ? zoomStep : 0.0;
            SwingImageDisplay.this.repaint();
        }
    }

    @Override
    public void on(Shift shift) {
        this.shift = shift;
    }

    @Override
    public void on(Released released) {
        this.released = released;
    }
}