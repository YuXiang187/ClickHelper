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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(Main.this, "确定退出程序？", "退出程序", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel label = new JLabel(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/bg/" + next() + ".jpg"))));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label, BorderLayout.CENTER);

        mainPanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (isControl) {
                    isControl = false;
                    isClick = !isClick;
                    label.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/bg/" + next() + ".jpg"))));
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
                }, 1200);
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
            times = 1;
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
                    Thread.sleep((int) (Math.random() * 200 + 100));
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
