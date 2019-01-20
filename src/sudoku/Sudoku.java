package sudoku;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Sudoku extends Canvas implements Runnable {

    public static final int WIDTH = 600, HEIGHT = 600;
    private JFrame frame;
    private MouseInput mouseInput;

    private Thread thread;
    private boolean running;
    
    private Solver solver;

    private int yOffset;
    private boolean solving;
    private boolean canSet;

    public Sudoku() {
        this.setMinimumSize(new Dimension(WIDTH,HEIGHT));
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH,HEIGHT));

        this.initFrame();

        this.mouseInput = new MouseInput();
        this.frame.addMouseListener(this.mouseInput);
        this.addMouseListener(this.mouseInput);
        this.frame.addMouseMotionListener(this.mouseInput);
        this.addMouseMotionListener(this.mouseInput);

        Gameplan.setCurrentGameplan(new Gameplan(null));
        this.solver = new Solver();
        this.yOffset = 25;
        this.solving = false;
        this.canSet = true;

        this.start();
    }

    private void initFrame() {
        this.frame = new JFrame();

        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setTitle("SudokuSolverV2");

        this.frame.add(this);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);

        this.frame.setVisible(true);
    }

    public synchronized void start() {
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public synchronized void stop() {
        this.running = false;
        try {
            this.thread.join();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    @Override
    public void run() {
        double timePerTick = 1000000000/120;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int frames = 0;

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta = 0;
            }

            if (timer >= 1000000000) {
                timer = 0;
                frames = 0;
            }
        }
    }

    public void tick() {
        if (this.solving) {
            this.solver.solve();
            /*if (Gameplan.getCurrentGameplan().isSolved()) {
                this.yOffset = 25;
                this.solving = false;
            }*/
        } else {
            if (this.mouseInput.isLmbDown()) {
                if (this.canSet) {
                    boolean set = false;
                    for (int row = 0; row < 9 && !set; row++) {
                        for (int column = 0; column < 9 && !set; column++) {
                            Rectangle r = new Rectangle(75 + column * 50, this.yOffset + row * 50, 50, 50);
                            if (r.contains(this.mouseInput.getX(), this.mouseInput.getY())) {
                                Gameplan.getCurrentGameplan().getTile(row, column).nextVal();
                                set = true;
                                break;
                            }
                        }
                    }
                    if (!set) {
                        Rectangle resetButton = new Rectangle(125, 510, 50, 50);
                        Rectangle importButton = new Rectangle(275, 510, 50, 50);
                        Rectangle solveButton = new Rectangle(425, 510, 50, 50);

                        if (resetButton.contains(this.mouseInput.getX(), this.mouseInput.getY())) {
                            for (int row = 0; row < 9; row++) {
                                for (int column = 0; column < 9; column++) {
                                    Gameplan.getCurrentGameplan().getTile(row, column).setValue(0);
                                }
                            }
                        } else if (importButton.contains(this.mouseInput.getX(), this.mouseInput.getY())) {
                            try {
                                Gameplan.getCurrentGameplan().init("/sudoku2.txt");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else if (solveButton.contains(this.mouseInput.getX(), this.mouseInput.getY())) {
                            this.yOffset = 75;
                            this.solving = true;
                        }
                    }
                }
                this.canSet = false;
            } else if (this.mouseInput.isRmbDown()) {
                if (this.canSet) {
                    boolean set = false;
                    for (int row = 0; row < 9 && !set; row++) {
                        for (int column = 0; column < 9 && !set; column++) {
                            Rectangle r = new Rectangle(75 + column * 50, this.yOffset + row * 50, 50, 50);
                            if (r.contains(this.mouseInput.getX(), this.mouseInput.getY())) {
                                Gameplan.getCurrentGameplan().getTile(row, column).prevVal();
                                set = true;
                                break;
                            }
                        }
                    }
                }
                this.canSet = false;
            } else {
                this.canSet = true;
            }
        }
        //Gameplan.getCurrentGameplan().print();
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(new Color(220,220,220));
        g.fillRect(0,0,WIDTH,HEIGHT);

        for (int row = 0; row < 9; row++) {
            g.setColor(Color.BLACK);
            g.drawLine(75+row*50, this.yOffset,75+row*50, this.yOffset +450);
            for (int column = 0; column < 9; column++) {
                g.setColor(Color.BLACK);
                g.drawLine(75, this.yOffset +column*50,75+450, this.yOffset +column*50);
            }
        }

        for (int squareX = 0; squareX < 3; squareX++) {
            for (int squareY = 0; squareY < 3; squareY++) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(3));
                g.setColor(Color.BLACK);
                g.drawRect(75+squareX*150, this.yOffset+squareY*150,150,150);
                g2d.setStroke(new BasicStroke(1));
            }
        }

        g.setFont(g.getFont().deriveFont(24f));
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (!Gameplan.getCurrentGameplan().getTile(row,column).isFree()) {
                    String text = String.valueOf(Gameplan.getCurrentGameplan().getTile(row, column).getValue());

                    g.setColor(Color.BLACK);
                    g.setFont(g.getFont().deriveFont(24f));
                    FontMetrics fm = g.getFontMetrics();
                    int x = 75+column*50 + (50 - fm.stringWidth(text)) / 2;
                    int y = this.yOffset +row*50 + ((50 - fm.getHeight()) / 2) + fm.getAscent();
                    g.drawString(text, x, y);
                }
            }
        }

        if (!solving) {
            try {
                g.drawImage(ImageIO.read(Sudoku.class.getResourceAsStream("/reset.png")), 125, 510, null);
                g.drawImage(ImageIO.read(Sudoku.class.getResourceAsStream("/import.png")), 275, 510, null);
                g.drawImage(ImageIO.read(Sudoku.class.getResourceAsStream("/solve.png")), 425, 510, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        g.dispose();
        bs.show();
    }

}
