import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JFrame {
    static Boolean isControl = true;
    static Boolean isClick = false;
    static int times = 0;
    Timer timer;

    public Main() {
        setTitle("连点器 - 就绪");
        setSize(700, 400);
        setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icon/run.png"))).getImage());
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        String[] img = new String[]{"bg1.jpg", "bg2.jpg", "bg3.jpg", "bg4.png", "bg5.jpg", "bg6.jpg", "bg7.jpg", "bg8.jpg", "bg9.png", "bg10.png"};

        JLabel label = new JLabel(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/image/" + img[next()]))));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label, BorderLayout.CENTER);

        mainPanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (isControl) {
                    isControl = false;
                    isClick = !isClick;
                    label.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/image/" + img[next()]))));
                    refreshUI();
                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
            }

            public void windowLostFocus(WindowEvent e) {
                setTitle("连点器 - 冷却中");
                setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icon/refresh.png"))).getImage());
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isControl = true;
                        refreshUI();
                        timer.cancel();
                    }
                }, 1000);
            }
        });

        setContentPane(mainPanel);
        validate();
    }

    private void refreshUI() {
        if (isClick) {
            setTitle("连点器 - 正在运行");
            setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icon/stop.png"))).getImage());
        } else {
            setTitle("连点器 - 就绪");
            setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/icon/run.png"))).getImage());
        }
    }

    private int next() {
        times += 1;
        if (times >= 10) {
            times = 0;
        }
        return times;
    }

    public static void main(String[] args) {
        new Main();
        ClickThread clickThread = new ClickThread();
        clickThread.start();
    }

    static class ClickThread extends Thread {
        @Override
        public void run() {
            Robot robot;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                try {
                    Thread.sleep((int) (Math.random() * 500 + 100));
                    if (isClick) {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
