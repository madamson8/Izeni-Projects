/**
 * Created by darkp_000 on 11/2/2015.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("serial")
public class Game extends JPanel implements KeyListener, MouseListener{

    public boolean exit = false; // Booleans
    public boolean wallCollision = false;
    public boolean secHasPassed = false;
    public boolean options = false;
    public boolean buttonOne = false;
    public boolean levelInGame = false;
    public boolean gravity = true;

    public boolean canMoveL = false;
    public boolean canMoveR = false;

    public int charX = 300; // char int stats
    public int charY = 250;
    public int charW = 20;
    public int charH = 30;
    public int charVX = 0;
    public int charVY = 0;
    public int numOfJumps = 0;

    public int buttOneX = 20; // button int stats
    public int buttOneY = 20;

    public int platOneX = 160;//Platform one stats
    public int platOneY = 289;
    public int platOneW = 50;
    public int platOneH = 10;

    public int platTwoX = 443;

    public int wallX = 0; // wallX = 0 because it needs to spawn at the side
    public int wallY = 380;
    public int secondsPassed = 0;

    // Long
    long time = System.currentTimeMillis();

    public static void main(String[] args) throws InterruptedException{
        Game game = new Game();

        JFrame window = new JFrame("Game.");
        JLabel timer = new JLabel();
        timer.setLocation(300,20);

        window.add(game);
        window.setSize(800,700);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        game.add(timer);

        window.setFocusable(true);
        window.setVisible(true);
        game.addKeyListener(game);
        game.addMouseListener(game);
        game.requestFocusInWindow();

        window.setLocationRelativeTo(null);

        while(true) {   // Main Loop !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            game.repaint();
            game.customUpdate();
            timer.setText(String.valueOf(game.secondsPassed));
            if(System.currentTimeMillis() > game.time + 1000 && !game.secHasPassed) {
                game.time = System.currentTimeMillis();
            }

            Thread.sleep(1000 / 60);
            if(game.exit) { // Exiting Loop
                System.out.println("Exiting");
                window.dispose();
                break;
            }
        }
    }

    public void paint(Graphics g) { // Remember, only graphics painting goes in here   ,  if you want to change anything like window size
        super.paint(g); // Repaints the window                             you need to change it here too.
        if(levelInGame || !options) {
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, 800, 700); // Window

            g.setColor(Color.BLUE);
            g.fillRect(charX, charY, 20, 30); // Character

            g.setColor(Color.BLACK); // wall
            g.fillRect(wallX, wallY, 800, 10);

            g.setColor(Color.BLACK); // Platforms
            g.fillRect(platOneX, platOneY, 50, 10);
        }

        if(options) {
            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,700); // Window
            g.setColor(Color.RED);
            g.fillRect(buttOneX,buttOneY,50,50);
        }
    }

    public void customUpdate() {  // Remember, all logic goes in here
        if(charX > 780) {
            charX = 0;
        }
        if(charX < 0) {
            charX = 780;
        }
        if(gravity) {
            charVY += 1;
            charY += charVY;
        }
        if(charY > 670-15) {
            charY = 0;
        }
        if(buttonOne) {
            exit = true;
        }

        if(charY+30 >= wallY) { // TODO:: Create a range for charX, maybe set it to x > x x < x .  obviously an idea
            charY = wallY-30;
            charVY = 0;
            wallCollision = true; // numOfJumps set to 0 because it increases by 1 every time you jump, allowing as many jumps as the programmer desires
            numOfJumps = 0;
            secHasPassed = false;
        }

        if(charX < platOneX + platOneW &&
                charX + charW > platOneX &&
                charY < platOneY + platOneH &&
                charH + charY  > platOneY) { //TODO  Get it so that it stops him when he hits the bottom
            charY = platOneY-30;
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
        if(charVY < -15) {
            charVY = -15;
        }

    }

    public void options() {
        //TODO  get options running and make it happen on escape

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {  // Logic can be used in these
//        System.out.println(e); // Enable to see what a keycode is.  Remember to disable
        switch (e.getKeyCode()) {
            case 37:
                gravity = true;
                canMoveL = true;
                break;
            case 38:
                gravity = true;
                if (numOfJumps < 2) {
                    charVY -= 15;
                    if (charVY < -15) {
                        charVY = -15;
                    }
                    numOfJumps += 1;
                }
                break;
            case 39:
                gravity = true;
                canMoveR = true;
                break;
            case 40:
                charY += 5;
                break;
            case 27: // Escape
                options = true;
                if (options) {
                    levelInGame = true;
                    options = false;
                }
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
        int mouseX = e.getX();
        int mouseY = e.getY();
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
