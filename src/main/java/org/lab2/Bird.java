package org.lab2;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@SuppressWarnings({"unused"})
public class Bird {
    public int x, y;
    public static final int WIDTH = 34;
    public static final int HEIGHT = 24;

    private double velocity = 0;
    private final double gravity = 0.5; // Tốc độ rơi tăng dần
    private final double jumpStrength = -7;
    private final Image birdImage;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        this.velocity = 0;
        // Load the bird image
        birdImage = getBirdImage();
    }
    private Image getBirdImage() {
        URL bgUrl = getClass().getResource("/org/lab2/flappybird.png");
        if (bgUrl != null) {
            return new ImageIcon(bgUrl).getImage();
        } else {
            System.err.println("Lỗi: Không thể tìm thấy resource /org/lab2/flappybird.png");
            return null;
        }
    }

    public void jump() {
        velocity = jumpStrength; // Jump strength
    }

    public void update() {
        velocity += gravity; // Gravity
        y += velocity;
    }

    public void clampY(int min, int max) {
        if (y < min) {
            y = min;
            velocity = 0;
        } else if (y > max) {
            y = max;
            velocity = 0;
        }
    }

    public void paint(Graphics g) {
        if (birdImage != null) {
            g.drawImage(birdImage, x, y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }
    }

    public void resetVelocity() {
        this.velocity = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
