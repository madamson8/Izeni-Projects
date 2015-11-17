package com.thefallenpaladin.mother;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JPanel implements KeyListener, MouseListener {

    //Gamestate stats
    int level = 0; // 0 is menu, 1 is options, 2 is win, 3 is death, 4 is level 1

    final String WINDOW_TITLE = "Platformer a.v.0.1"; // window stats
    final int WINDOW_WIDTH = 500;
    final int WINDOW_HEIGHT = 500;

    int mouseX; // stats for mouse, keyboard, ect
    int mouseY;

    // Level 0 buttons, parameters, ect
    int lv0ButtonOneX = WINDOW_WIDTH/4;
    int lv0ButtonOneY = WINDOW_WIDTH/2;
    int lv0ButtonOneS = 50;

    public static void main(String[] args) {
        Game game = new Game();

        JFrame window = new JFrame(game.WINDOW_TITLE);
        window.setSize(game.WINDOW_WIDTH,game.WINDOW_HEIGHT);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        window.add(game);
        game.addKeyListener(game);
        game.addMouseListener(game);
        game.requestFocusInWindow();

        window.setLocationRelativeTo(null);
        while(true) {
            game.repaint();
            game.customUpdate();
        }
    }

    public void customUpdate() {
        if(level == 0) {
            if(mouseX > lv0ButtonOneX &&
                    mouseX < lv0ButtonOneX + lv0ButtonOneS &&
                    mouseY < lv0ButtonOneY + lv0ButtonOneS &&
                    mouseY > lv0ButtonOneY) {
                level = 4;
            }
        }
    }

    public void paint(Graphics g) {
        if(level == 0) {
            g.setColor(Color.BLACK);
            g.fillRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT); // draw window          needs to be first in order to be drawn over

            g.setColor(Color.ORANGE);
            g.fillRect(lv0ButtonOneX,lv0ButtonOneY,lv0ButtonOneS,lv0ButtonOneS); // draw buttons

            g.setColor(Color.BLUE);
            g.drawString("Start",lv0ButtonOneX + lv0ButtonOneS/4,lv0ButtonOneY + lv0ButtonOneS/2);
        }
        if(level == 4) {
            g.setColor(Color.GREEN);
            g.fillRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT); // draw window          needs to be first in order to be drawn over

            g.setColor(Color.ORANGE);
            g.fillRect(0,WINDOW_HEIGHT-20,WINDOW_WIDTH,20); // creates floor at bottom - 20 for standing.
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
