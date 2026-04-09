package org.lab2;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FlappyBirdGame extends JFrame {
    private final Image background;
    private final Bird bird;
    private final GamePanel panel;
    private boolean gameOver = false;
    private int score = 0;

    // Pipes
    private final List<Pipe> pipes = new ArrayList<>();
    private final Random rand = new Random();
    private int spawnCounter = 0;
    private double gameSpeed = 3.5;
    private double scoreLevel = 5;

    public FlappyBirdGame(){
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        URL bgUrl = getClass().getResource("/org/lab2/flappybirdbg.png");
        if (bgUrl != null) {
            background = new ImageIcon(bgUrl).getImage();
        } else {
            System.err.println("Lỗi: Không thể tìm thấy resource /org/lab2/flappybirdbg.png");
            background = null;
        }

        bird = new Bird(50, 300);

        panel = new GamePanel();
        setContentPane(panel);
        pack();
        setResizable(false);

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "jump");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "jump");
        panel.getActionMap().put("jump", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (gameOver) {
                    resetGame(); // Nếu đang thua thì bấm phím để chơi lại
                } else {
                    bird.jump(); // Nếu đang chơi thì bấm phím để nhảy
                }
            }
        });

        Timer timer = new Timer(20, ev -> {
            update();
            panel.repaint();
        });
        timer.start();

        setVisible(true);
    }

    // Keep logic updates separated from painting
    public void update() {
        if(gameOver) return;
        bird.update();
        // Logic tăng tốc độ theo điểm số để game kịch tính hơn
        if (score > scoreLevel) {
            gameSpeed += 0.2;
            scoreLevel += 10;
        }
        // clamp bird inside panel vertically
        // panel may not be realized yet, guard with getHeight() > 0
        int h = panel.getHeight();
        if (h > 0) {
            bird.clampY(0, h - Bird.HEIGHT);
            if(bird.y >= h - Bird.HEIGHT) gameOver = true; // hit the ground
        }
        checkAndSpawnPipes();

        Iterator<Pipe> it = pipes.iterator();
        while (it.hasNext()) {
            Pipe p = it.next();
            p.setSpeed(gameSpeed);
            p.update();

            if(checkCollision(bird, p))
                gameOver = true;

            if(!p.isPassed() && p.getX() + p.getWidth() < bird.x) {
                score++;
                p.setPassed(true);
            }

            if (p.isOffScreen()) it.remove();
        }
    }

    private boolean checkCollision(Bird b, Pipe p) {
        Rectangle birdRect = b.getBounds();
        // Hình chữ nhật ống trên
        Rectangle topPipeRect = new Rectangle(p.getX(), 0, p.getWidth(), p.getGapY());
        // Hình chữ nhật ống dưới
        Rectangle bottomPipeRect = new Rectangle(p.getX(), p.getGapY() + p.getGapHeight(), p.getWidth(), 800);

        return birdRect.intersects(topPipeRect) || birdRect.intersects(bottomPipeRect);
    }

    private void checkAndSpawnPipes() {
        if (pipes.isEmpty()) {
            spawnPipe();
            return;
        }

        // Lấy ống cuối cùng bên phải màn hình
        Pipe lastPipe = pipes.get(pipes.size() - 1);

        if (lastPipe.getX() < panel.getWidth() - 200) {
            spawnPipe();
        }
    }

    private void spawnPipe() {
        int panelW = panel.getWidth();
        int panelH = (panel.getHeight() > 0) ? panel.getHeight() : 640;

        int gapHeight = 150; // Cố định hoặc rand nhẹ để game công bằng
        int minPipeHeight = 50;
        int maxPipeHeight = panelH - gapHeight - minPipeHeight;
        int gapY = minPipeHeight + rand.nextInt(maxPipeHeight);

        // Tạo ống với tốc độ game hiện tại luôn
        pipes.add(new Pipe(panelW, gapY, gapHeight, (int)gameSpeed));
    }

    private void resetGame() {
        // 1. Đưa chim về vị trí cũ
        bird.x = 50;
        bird.y = 300;
        bird.resetVelocity();

        // 2. Xóa danh sách ống cũ và reset điểm
        pipes.clear();
        score = 0;
        spawnCounter = 0;

        // 3. Đặt lại trạng thái game
        gameOver = false;
    }

    // JPanel to handle drawing
    private class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(360, 640));
            setFocusable(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth();
            int h = getHeight();

            if (background != null) {
                g.drawImage(background, 0, 0, w, h, this);
            } else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, w, h);
            }

            for (Pipe p : pipes) {
                p.paint(g, h);
            }

            bird.paint(g);
            // Vẽ Score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Score: " + score, 20, 40);

            if (gameOver) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.RED);
                g.drawString("GAME OVER", getWidth()/2 - 90, getHeight()/2);
                g.setFont(new Font("Arial", Font.PLAIN, 15));
                g.setColor(Color.WHITE);
                g.drawString("Press F5 to Restart (Example)", getWidth()/2 - 80, getHeight()/2 + 40);
            }
        }
    }
    public static void main(String[] args) {
        // Create the GUI on the EDT
        SwingUtilities.invokeLater(FlappyBirdGame::new);
    }
}
