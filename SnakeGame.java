import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SnakeGame extends JPanel implements ActionListener {
    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 100;

    private int x[] = new int[ALL_DOTS];
    private int y[] = new int[ALL_DOTS];
    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public SnakeGame() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon(getClass().getResource("dot.png"));
        ball = iid.getImage();

        iid = new ImageIcon(this.getClass().getResource("dot.png"));
        apple = iid.getImage();

        iid = new ImageIcon(this.getClass().getResource("dot.png"));
        head = iid.getImage();
    }

    private void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int z = 0; z < dots; z++) {
                if (z == 0)
                    g.drawImage(head, x[0], y[0], this);
                else
                    g.drawImage(ball, x[z], y[z], this);
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollisions() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
        if (y[0] > B_HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] > B_WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));
        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2,
                B_HEIGHT / 2);
    }

    private class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                right = false;
                left = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                right = false;
                left = false;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.add(new SnakeGame());
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}