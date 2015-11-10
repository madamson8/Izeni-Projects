/**
 * Created by darkp_000 on 11/2/2015.
 */

package com.thefallenpaladin.mother;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("serial")
public class Game extends JPanel implements KeyListener, MouseListener {

    boolean exit = false; // Booleans  these are mainly setting up.  you don't need to worry about most of these.
    boolean wallCollision = false;
    boolean secHasPassed = false;
    boolean options = false;
    boolean buttonOne = false;
    boolean levelInGame = false;
    boolean gravity = true;
    boolean running = true;

    boolean canMoveL = false;
    boolean canMoveR = false;

    int charX = 300; // char int stats
    int charY = 250;
    int charW = 20;
    int charH = 30;
    int charVX = 0;
    int charVY = 0;
    int numOfJumps = 0;
    int maxJumps = 2;

    int buttOneX = 20; // button int stats
    int buttOneY = 20;

    int platOneX = 160;//Platform one stats
    int platOneY = 289;
    int platOneW = 50;
    int platOneH = 10;

    int platTwoX = 305; // Platform two stats
    int platTwoY = 234;
    int platTwoW = platOneW;
    int platTwoH = platOneH;

    int platThreeX = 443;
    int platThreeY = 168;
    int platThreeW = platOneW;
    int platThreeH = platOneH;

    int wallX = 0; // wallX = 0 because it needs to spawn at the side
    int wallY = 380;
    int secondsPassed = 0;
    int mouseX;
    int mouseY;
    final int WIN_WIDTH = 800;
    final int WIN_HEIGHT = 700;

    int goomX = 525; // Enemy stats
    int goomY = wallY-30;
    int goomSize = 20;

    // Long
    long time = System.currentTimeMillis();

    public static void main(String[] args) throws InterruptedException{
        Game game = new Game();  // This is just setting up the game

        JFrame window = new JFrame("Game.");
        JLabel timer = new JLabel();  // This is being overwriten.  Worry about this later.
        timer.setLocation(300,20);

        window.add(game);
        window.setSize(800,700);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);  // false because it would mess with everything if resized.
        game.add(timer);

        window.setFocusable(true);
        window.setVisible(true);
        game.addKeyListener(game);
        game.addMouseListener(game); // Getting key listeners and mouse listeners
        game.requestFocusInWindow();

        window.setLocationRelativeTo(null); // last so it sets in middle

        while(true) {   // Main Loop !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            game.repaint(); // calling paint()
            game.customUpdate();
            game.backAndForthEnemy();  // calling respective methods
            timer.setText(String.valueOf(game.secondsPassed)); // Is overwritten.  worry about later.
            if(System.currentTimeMillis() > game.time + 1000 && !game.secHasPassed) {
                game.time = System.currentTimeMillis(); // Gets one second.
            }

            Thread.sleep(1000 / 60); // 60 fps
            if(game.exit) { // Exiting Loop
                System.out.println("Exiting"); // exit is true on button clicked when escape is pressed. opens up a menu
                window.dispose(); // closes the window
                break;
            }
        }
    }

    public void paint(Graphics g) { // Remember, only graphics painting goes in here   ,  if you want to change anything like window size
        super.paint(g); // Repaints the window                             you need to change it here too.
        if(levelInGame || !options) {
            g.setColor(Color.GREEN); // Ahead are the stats for drawing characters ect.
            g.fillRect(0, 0, 800, 700); // Window

            g.setColor(Color.BLUE);
            g.fillRect(charX, charY, 20, 30); // Character

            g.setColor(Color.BLACK); // wall
            g.fillRect(wallX, wallY, WIN_WIDTH, 10);

            // Platforms
            g.fillRect(platOneX, platOneY, platOneW, platOneH); // Draws three sequential platforms
            g.fillRect(platTwoX, platTwoY, platTwoW,platTwoH);
            g.fillRect(platThreeX, platThreeY, platThreeW,platThreeH);

            // Enemies
            g.fillRect(goomX,goomY,goomSize,goomSize);
        }

        if(options) { // activates when escape is pressed
            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,700); // Window
            g.setColor(Color.RED); // button one
            g.fillRect(buttOneX,buttOneY,50,50);
        }
    }

    public void customUpdate() {  // Remember, all logic goes in here
        if(charX > WIN_WIDTH-charW/2) { // Gets edges of screens relative to character
            charX = 0;
        }
        if(charX < 0) {
            charX = WIN_WIDTH-20;
        }
        if(gravity) { // Grav can be disabled by wad  not s because i was lazy.
            charVY += 1;
            charY += charVY;
        }
        if(charY > 670-15) {
            charY = 0;
        }
        if(buttonOne) { // will close the window.  first button on escape menu
            exit = true;
        }

        if(charY+30 >= wallY) {
            charY = wallY-30;
            charVY = 0;
            wallCollision = true; // numOfJumps set to 0 because it increases by 1 every time you jump, allowing as many jumps as the programmer desires
            numOfJumps = 0;
            secHasPassed = false;
        }

        if(charY < platOneY && // ahead are alogrithms for collision
                charX < platOneX + platOneW && // Collision for plat 1 and so on
                charX + charW > platOneX &&
                charY < platOneY + platOneH &&
                charH + charY > platOneY) {
            charY = platOneY-30;
            numOfJumps = 0;
            charVY = 0;
            wallCollision = true; // Gets top of the platform collision
        }

        if(charY > platOneY &&
                charX < platOneX + platOneW && // Collision for plat 1 and so on
                charX + charW > platOneX &&
                charY < platOneY + platOneH &&
                charH + charY > platOneY) {
            charY = platOneY+platOneH+1;
            numOfJumps = maxJumps;
            charVY = 0;
            wallCollision = true; // Gets bottom of platform collision
        }

        if(charY > platTwoY && // TODO  find out why the bottom on two works but one doesnt  (theory) there is not enough time to pass through on the first plat
                charX < platTwoX + platTwoW &&
                charX + charW > platTwoX &&
                charY < platTwoY + platTwoH &&
                charH + charY > platTwoY) {
            charY = platTwoY+platTwoH+1;
            numOfJumps = maxJumps;
            charVY = 0;
            wallCollision = true;
            System.out.println("Bottom");
        }

        if(charY < platTwoY &&
                charX < platTwoX + platTwoW &&
                charX + charW > platTwoX &&
                charY < platTwoY + platTwoH &&
                charH + charY > platTwoY) {
            charY = platTwoY-30;
            numOfJumps = 0;
            charVY = 0;
            wallCollision = true;
        }

        if(charX < platThreeX + platThreeW &&
                charX + charW > platThreeX &&
                charY < platThreeY + platThreeH &&
                charH + charY > platThreeY) {
            charY = platThreeY-30;
            numOfJumps = 0;
            charVY = 0;
            wallCollision = true;
        }

        if(canMoveL) { // Movement Logic
            charX -= 5;
        }
        if(canMoveR) {
            charX += 5;
        }
        if(charVY < -15) { // Limits speed on y axis
            charVY = -15;
        }

        if(charX < goomX + goomSize && // Initiates gameOver() when hit by a enemy that is not the name of a copyrighted character
                charX + charW > goomX &&
                charY < goomY + goomSize &&
                charH + charY > goomY) {
            gameOver();
        }

    }

    public void gameOver() {
        if(running) {
            System.out.println("Game Over");
        }
        running = false;
    }

    public void options() { // Ignored.  TODO
        //TODO  get options running and make it happen on escape

    }

    private void backAndForthEnemy() { // basic enemy on first screen
        boolean goomR = false; // Ignored
        boolean goomL = true; // Ignored
        int second = 0;
        if(running) {
            goomX += 3;
//            long time = System.currentTimeMillis();
//            if(time > time+1000) {
//                time = System.currentTimeMillis();
//                second += 1;
//
//            }
            if(goomX > WIN_WIDTH) {
                goomX = 0;
            }
        }

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {  // Logic can be used in these
//        System.out.println(e); // Enable to see what a keycode is.  Remember to disable
        switch (e.getKeyCode()) {
            case 37: // Left
                gravity = true;
                canMoveL = true;
                break;
            case 38: // Up
                gravity = true;
                if (numOfJumps < 2) {
                    charVY -= 15;
                    if (charVY < -15) {
                        charVY = -15;
                    }
                    numOfJumps += 1;
                }
                break;
            case 39: // Right
                gravity = true;
                canMoveR = true;
                break;
            case 40: // Down
                charY += 5;
                break;
            case 27: // Escape
                options = true;
                break;
            case 32: // Space
                System.out.println(charX);
                System.out.println(charY);
                System.out.println("Wall collision: " + wallCollision);
                System.out.println("secHasPassed: " + secHasPassed);
                break;
            case 87: // W
                gravity = false;
                charY -= 1;
                break;
            case 65: // A
                charX -= 1;
                break;
            case 68: // D
                charX += 1;
                break;
            case 82: // R
                running = true;
                break;
        }

    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case 37:
                canMoveL = false;
                break;
            case 39:
                canMoveR = false;
                break;
        }

    }

    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX(); // gets mouse x and y
        mouseY = e.getY();
        if(mouseX > buttOneX && mouseX < buttOneX+50 && mouseY > buttOneY && mouseY < buttOneY+50 && options) { //
            buttonOne = true;
        }
        System.out.println(mouseX);
        System.out.println(mouseY);
        System.out.println("Button One has been pressed: " + buttonOne);
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
