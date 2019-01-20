package sudoku;

import java.awt.event.*;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int x,y;
    private boolean lmb, rmb, mmb;
    private int scroll;

    public MouseInput() {
        this.x = 0;
        this.y = 0;
        this.lmb = false;
        this.rmb = false;
        this.mmb = false;
        this.scroll = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            this.lmb = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            this.rmb = true;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            this.mmb = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            this.lmb = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            this.rmb = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            this.mmb = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isLmbDown() {
        return lmb;
    }

    public boolean isRmbDown() {
        return rmb;
    }

    public boolean isMmbDown() {
        return mmb;
    }

    public int getScroll() {
        return scroll;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.scroll += Math.abs(e.getWheelRotation());
        if (this.scroll > 1000) this.scroll = 1;
    }
}

