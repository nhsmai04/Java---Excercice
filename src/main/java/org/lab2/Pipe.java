package org.lab2;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class Pipe {
    private int x;
    private final int gapY;
    private final int gapHeight;
    private double speed;
    private boolean passed = false;

    private final Image topImage;
    private final Image bottomImage;
    private int imageWidth;

    public Pipe(int startX, int gapY, int gapHeight, int speed) {
        this.x = startX;
        this.gapY = gapY;
        this.gapHeight = gapHeight;
        this.speed = speed;

        URL topUrl = getClass().getResource("/org/lab2/toppipe.png");
        URL bottomUrl = getClass().getResource("/org/lab2/bottompipe.png");
        if (topUrl != null) {
            topImage = new ImageIcon(topUrl).getImage();
        } else {
            System.err.println("Lỗi: Không thể tìm thấy resource /org/lab2/toppipe.png");
            topImage = null;
        }
        if (bottomUrl != null) {
            bottomImage = new ImageIcon(bottomUrl).getImage();
        } else {
            System.err.println("Lỗi: Không thể tìm thấy resource /org/lab2/bottompipe.png");
            bottomImage = null;
        }

        if (topImage != null) {
            imageWidth = 30;
        } else {
            imageWidth = 30;
        }
    }

    public void update() {
        x -= speed;
    }

    public boolean isOffScreen() {
        return x + imageWidth < 0;
    }

    public void paint(Graphics g, int panelHeight) {
        if (topImage != null) {
            // Draw scaled to gapY
            g.drawImage(topImage, x, 0, imageWidth, gapY, null);
        } else {
            g.setColor(new Color(0, 153, 0));
            g.fillRect(x, 0, imageWidth, gapY);
        }

        int bottomY = gapY + gapHeight;
        int bottomHeight = panelHeight - bottomY;
        if (bottomImage != null) {
            g.drawImage(bottomImage, x, bottomY, imageWidth, bottomHeight, null);
        } else {
            g.setColor(new Color(0, 153, 0));
            g.fillRect(x, bottomY, imageWidth, bottomHeight);
        }
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return imageWidth;
    }

    public int getGapY() { return gapY; }
    public int getGapHeight() { return gapHeight; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
    public void setSpeed(double speed) {
        this.speed = speed;
    }d
    public Rectangle getTopBounds() {
        return new Rectangle(x, 0, imageWidth, gapY);
    }

    public Rectangle getBottomBounds(int panelHeight) {
        return new Rectangle(x, gapY + gapHeight, imageWidth, panelHeight - (gapY + gapHeight));
    }
}
