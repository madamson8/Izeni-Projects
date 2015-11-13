/**
 * Created by Matthew on 11/2/2015.
 * credit to Me, Jaylen, and Izeni.
 * Feel free to use if you want, just
 * credit us.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("serial")
class Game extends JPanel implements KeyListener, MouseListener{

    boolean exit = false; // Booleans
    boolean wallCollision = false;
    boolean secHasPassed = false;
    boolean options = false;
    boolean buttonOne = false;
    boolean levelInGame = true;
    boolean gravity = true;
    boolean running = true;
    boolean secondLevelInGame = false;
    boolean invincibleMode = false;
    boolean hasWon = false;

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
    final int WIN_WIDTH = 800;
    final int WIN_HEIGHT = 700;

    int goomX = 525; // Enemy stats
    int goomY = wallY-30;
    int goomSize = 20;

    int cEnemySize = 15;
    int cEnemyX = 0;
    int cEnemyY = 0;

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
            game.backAndForthEnemy();
            game.complexEnemy();

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
            g.fillRect(wallX, wallY, WIN_WIDTH, 10);

            // Platforms
            g.fillRect(platOneX, platOneY, platOneW, platOneH);
            g.fillRect(platTwoX, platTwoY, platTwoW,platTwoH);
            g.fillRect(platThreeX, platThreeY, platThreeW,platThreeH);

            // Enemies
            g.fillRect(goomX,goomY,goomSize,goomSize);
        }

        if(secondLevelInGame && !levelInGame) {
            g.setColor(Color.RED);
            g.fillRect(0,0,WIN_WIDTH,WIN_HEIGHT); // Fills window

            g.setColor(Color.BLACK); // Draws floor
            g.fillRect(wallX, wallY, WIN_WIDTH, 10);

            g.setColor(Color.BLACK);
            g.fillRect(platOneX,platOneY,platOneW,platOneH); // draws platforms

            g.setColor(Color.GREEN); // Character
            g.fillRect(charX,charY,charW,charH);

            g.setColor(Color.BLUE);
            g.fillRect(cEnemyX,cEnemyY,cEnemySize,cEnemySize);
        }

        if(options) {
            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,700); // Window
            g.setColor(Color.RED);
            g.fillRect(buttOneX,buttOneY,50,50);
        }
        if(!running) {
            char[] gameOver = {'G','a','m','e',' ','O','v','e','r'};
            g.setColor(Color.RED);
            g.fillRect(0,0,WIN_WIDTH,WIN_HEIGHT);

            g.setColor(Color.BLACK);
            g.drawChars(gameOver, 0, gameOver.length,WIN_WIDTH/2,WIN_HEIGHT/2);
//            g.drawChars(char[] data, int offset, int length, int x, int y) ex of text
        }
        char[] quit = {'Q','u','i','t','?'};
        char[] restart = {'R','e','s','t','a','r','t','?'};

        if(hasWon) {
            char[] youWin = {'Y','o','u',' ','H','a','v','e',' ','W','o','n','!'};
            g.setColor(Color.WHITE);
            g.fillRect(0,0,WIN_WIDTH,WIN_HEIGHT);

            g.setColor(Color.CYAN);
            g.fillRect(WIN_WIDTH/3,WIN_HEIGHT/2+30,50,50);
            g.fillRect(WIN_WIDTH/3+WIN_WIDTH/3,WIN_HEIGHT/2+30,50,50);

            g.setColor(Color.BLACK); // strings last because they need to override everything else
            g.drawChars(youWin, 0, youWin.length, WIN_WIDTH/2-20, WIN_HEIGHT/2);
            g.drawChars(quit, 0, quit.length, WIN_WIDTH/3+12,WIN_HEIGHT/2+55);
            g.drawChars(restart, 0, restart.length, WIN_WIDTH/3+WIN_WIDTH/3-restart.length+10, WIN_HEIGHT/2+55);

        }
    }

    //TODO copy to top
    int quitButtonX = WIN_WIDTH/3;
    int quitButtonY = WIN_HEIGHT/2+30;
    int quitButtonSize = 50;

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

        if(charY+30 >= wallY) {
            charY = wallY-30;
            charVY = 0;
            wallCollision = true; // numOfJumps set to 0 because it increases by 1 every time you jump, allowing as many jumps as the programmer desires
            numOfJumps = 0;
            secHasPassed = false;
        }
        if(levelInGame) {
            if (charY < platOneY &&
                    charX < platOneX + platOneW && // Collision for plat 1 and so on
                    charX + charW > platOneX &&
                    charY < platOneY + platOneH &&
                    charH + charY > platOneY) { //TODO  Get it so that it stops him when he hits the bottom
                charY = platOneY - 30;
                numOfJumps = 0;
                charVY = 0;
                wallCollision = true; // Gets top of the platform collision
            }

            if (charY > platOneY &&
                    charX < platOneX + platOneW && // Collision for plat 1 and so on
                    charX + charW > platOneX &&
                    charY < platOneY + platOneH &&
                    charH + charY > platOneY) {
                charY = platOneY + platOneH + 1;
                numOfJumps = maxJumps;
                charVY = 0;
                wallCollision = true; // Gets bottom of platform collision
            }

            if (charY > platTwoY - 15 &&
                    charX < platTwoX + platTwoW &&
                    charX + charW > platTwoX &&
                    charY < platTwoY + platTwoH &&
                    charH + charY > platTwoY) {
                charY = platTwoY + platTwoH + 1;
                charVY = 0;
                numOfJumps = maxJumps;
                wallCollision = true;
            }

            if (charY < platTwoY &&
                    charX < platTwoX + platTwoW &&
                    charX + charW > platTwoX &&
                    charY < platTwoY + platTwoH &&
                    charH + charY > platTwoY) {
                charY = platTwoY - 30;
                numOfJumps = 0;
                charVY = 0;
                wallCollision = true;
            }

            if (charX < platThreeX + platThreeW &&
                    charX + charW > platThreeX &&
                    charY < platThreeY + platThreeH &&
                    charH + charY > platThreeY) {
                charY = platThreeY - 30;
                numOfJumps = 0;
                charVY = 0;
                wallCollision = true;
                secondLevelInGame = true; // Level 2
                levelInGame = false; // Level 1
            }

            if (charX < goomX + goomSize &&
                    charX + charW > goomX &&
                    charY < goomY + goomSize &&
                    charH + charY > goomY) {
                gameOver();
            }
        }
        if(secondLevelInGame) { // Level 2 logic
            if(charX < platOneX + platOneW &&
                    charX + charW > platOneX &&
                    charY < platOneY + platOneH &&
                    charH + charY > platOneY) {
//                charY = platOneY - charH;
//                numOfJumps = 0;
//                charVY = 0;
//                wallCollision = true;
                hasWon = true;
            }
        }
        if (canMoveL) { // Movement Logic
            charX -= 5;
        }
        if (canMoveR) {
            charX += 5;
        }
        if (charVY < -15) {
            charVY = -15;
        }
    }

    public void gameOver() {
        if(running) {
            System.out.println("Game Over");
        }
        running = false;
    }

    public void options() {
        //TODO  get options running and make it happen on escape

    }

    private void backAndForthEnemy() {
        boolean goomR = false;
        boolean goomL = true;
        int second = 0;
        if(running && !invincibleMode) {
            goomX += 3;
            if(goomX > WIN_WIDTH) {
                goomX = 0;
            }
        }
        if(!running) {
            goomX = charX+charW+50;
        }

    }

    private void complexEnemy() {
        int cEnemySpeed = 2;
        if(secondLevelInGame) {
            if(running && !invincibleMode) {
                if (charX > cEnemyX) { // If you are to the right of the enemy, it moves to the right
                    cEnemyX += cEnemySpeed;
                }
                if (charX < cEnemyX) { // If you are to the left of the enemy, it moves to the left.
                    cEnemyX -= cEnemySpeed;
                }
                if(charY < cEnemyY) {
                    cEnemyY -= cEnemySpeed;
                }
                if(charY > cEnemyY) {
                    cEnemyY += cEnemySpeed;
                }
            }
            if(running && // Only activates if you are in the second level and you are running.
                    charX < cEnemyX + cEnemySize &&
                    charX + charW > cEnemyX &&
                    charY < cEnemyY + cEnemySize &&
                    charH + charY > cEnemyY) {
                gameOver(); // Calls game over
            }
            if(!running) {
                cEnemyX = WIN_WIDTH/2;
                cEnemyY = 0;
            }
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {  // Logic can be used in these but try to shy away from it
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
                running = false;
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
                invincibleMode = true;
                charY -= 1;
                break;
            case 65: // A
                charX -= 1;
                gravity = false;
                invincibleMode = true;
                break;
            case 68: // D
                charX += 1;
                gravity = false;
                invincibleMode = true;
                break;
            case 82: // R
                running = true;
                hasWon = false;
                gravity = true;
                levelInGame = true;
                secondLevelInGame = false;
                options = false;
                invincibleMode = false;
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
