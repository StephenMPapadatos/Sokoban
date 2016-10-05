 /**
 * My Sokoban Project Game
 * Stephen Papadatos
 * November 28th, 2012
 */

import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

 /**
  * The Sokoban class initializes the games graphics and deals with the controls
  * and win conditions of the game.
  */

public class Sokoban extends JPanel implements KeyListener{
    LevelReader lr;
    
    private Rectangle2D.Double wall, empty, goal, box, boxongoal, player, playerongoal;
    private int lvlwidth, lvlheight, dx = 0, dy = 0, px, py, pressed;
    private JLabel info;
    public static JLabel timerLabel;
    
    public static int level, time;
    public static String file;
    public static JFrame f, o;
    public static java.util.Timer timer;
    
    Contents[][] current;
    Contents z;
    
    boolean isReady = false;
    
    public Sokoban(String fileName){
        lr = new LevelReader();
        lr.readLevels(fileName);
        
        addKeyListener(this);
        setFocusable(true);
        
        info = new JLabel("Level: "+(level+1)+" | Steps:"+pressed);
        this.add(info);
        
        timerLabel = new JLabel("TimerLabel");
        this.add(timerLabel);
        
        timer = new java.util.Timer("Timer");
        class myTask extends TimerTask{
            public void run(){
                time+=1;
                Sokoban.timerLabel.setText(" | Timer:"+time);
            }
        }
        myTask t = new myTask();
        timer.schedule(t, 1, 1000);
        
        isReady = true;
    }
    public void initLevel(int level, Frame o){
        o = f;
        lvlwidth = lr.getWidth(level);
        lvlheight = lr.getHeight(level);
        
        this.setPreferredSize(new Dimension(lvlwidth*30, lvlheight*30+30));
        o.pack();
        
        this.info.setText("Level: "+(level+1)+" | Steps: 0");
        this.timerLabel.setText(" | Timer: 0");
        pressed = 0;
        time = 0;
        
        current = new Contents[lvlwidth][lvlheight];
        for (int x = 0; x < lvlwidth; x++){
            for (int y = 0; y < lvlheight; y++){
                z = lr.getTile(level, x, y);
                current[x][y] = z;
            }
        }
        repaint();
    }

    // displays and updates the images based on the game state
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3.0f));
        if (isReady == true){
            for (int x = 0; x < lvlwidth; x++){
                for (int y = 0; y < lvlheight; y++){
                    int row = x*30;
                    int col = y*30+30;
                    if (current[x][y] == Contents.WALL){
                        BufferedImage img =null;
                        try {
                            img =ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\Assets\\Wall.png"));
                        } catch (IOException e) {
                        }
                        g2.drawImage(img, row, col, null);
                    }
                    if (current[x][y] == Contents.EMPTY){
                        BufferedImage img =null;
                        try {
                            img =ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\Assets\\Empty.png"));
                        } catch (IOException e) {
                        }
                        g2.drawImage(img, row, col, null);
                    }
                    if (current[x][y] == Contents.GOAL){
                        BufferedImage img =null;
                        try {
                            img =ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\Assets\\Goal.png"));
                        } catch (IOException e) {
                        }
                        g2.drawImage(img, row, col, null);
                    }
                    if (current[x][y] == Contents.BOX){
                        BufferedImage img =null;
                        try {
                            img =ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\Assets\\Box.png"));
                        } catch (IOException e) {
                        }
                        g2.drawImage(img, row, col, null);
                    }
                    if (current[x][y] == Contents.BOXONGOAL){
                         BufferedImage img =null;
                        try {
                            img =ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\Assets\\Boxongoal.png"));
                        } catch (IOException e) {
                        }
                        g2.drawImage(img, row, col, null);
                    }
                    if (current[x][y] == Contents.PLAYER){
                       BufferedImage img =null;
                        try {
                            img =ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\Assets\\Player.png"));
                        } catch (IOException e) {
                        }
                        g2.drawImage(img, row, col, null);
                    }
                    if (current[x][y] == Contents.PLAYERONGOAL){
                       BufferedImage img =null;
                        try {
                            img =ImageIO.read(new File(System.getProperty("user.dir") + "\\src\\Assets\\Playerongoal.png"));
                        } catch (IOException e) {
                        }
                        g2.drawImage(img, row, col, null);
                    }
                }
            }
        }
    }
    private boolean checkWin(){
        for (int x = 0; x < lvlwidth; x++){
            for (int y = 0; y < lvlheight; y++){
                if (current[x][y] == Contents.BOX || current[x][y] == Contents.GOAL){
                    return false;
                }
            }
        }
        return true;
        }
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode(); 
        switch(keyCode){
            case KeyEvent.VK_UP:
            dx = 0;
            dy = -1;
            break;
            case KeyEvent.VK_DOWN:
            dx = 0;
            dy = 1;
            break;
            case KeyEvent.VK_LEFT:
            dx = -1;
            dy = 0;
            break;
            case KeyEvent.VK_RIGHT:
            dx = 1;
            dy = 0;
            break;
            case KeyEvent.VK_N:
            if(level != 154) level++;
            else level = 0;
            initLevel(level, o);
            break;
            case KeyEvent.VK_B:
            if(level != 0) level--;
            else level = 154;
            initLevel(level, o);
            case KeyEvent.VK_R:
            initLevel(level, o);
            break;
        }
        for (int x = 0; x < lvlwidth; x++){
                for (int y = 0; y < lvlheight; y++){
                if (current[x][y] == Contents.PLAYER || current[x][y] == Contents.PLAYERONGOAL){
                    px = x;
                    py = y;
                    break;
                }
            }
        }
        if (current[px][py] == Contents.PLAYER || current[px][py] == Contents.PLAYERONGOAL ){
            if ((px+2*dx >= 0 && px+2*dx < lvlwidth) && (py+2*dy >= 0 && py+2*dy < lvlheight)){   
                if (current[px+2*dx][py+2*dy] == Contents.EMPTY || current[px+2*dx][py+2*dy] == Contents.GOAL){
                    if (current[px+dx][py+dy] == Contents.BOX){
                        current[px+dx][py+dy] = Contents.PLAYER;
                        if (current[px+2*dx][py+2*dy] == Contents.EMPTY){
                            current[px+2*dx][py+2*dy] = Contents.BOX;
                            eitherOr();
                        }
                        if (current[px+2*dx][py+2*dy] == Contents.GOAL){
                            current[px+2*dx][py+2*dy] = Contents.BOXONGOAL;
                            eitherOr();
                        }
                    }
                    if (current[px+dx][py+dy] == Contents.BOXONGOAL){
                        current[px+dx][py+dy] = Contents.PLAYERONGOAL;
                        if (current[px+2*dx][py+2*dy] == Contents.EMPTY){
                            current[px+2*dx][py+2*dy] = Contents.BOX;
                            eitherOr();
                        }
                        if (current[px+2*dx][py+2*dy] == Contents.GOAL){
                            current[px+2*dx][py+2*dy] = Contents.BOXONGOAL;
                            eitherOr();
                        }
                    }
                }
            }
            if (current[px+dx][py+dy] == Contents.EMPTY){
                current[px+dx][py+dy] = Contents.PLAYER;
                eitherOr();
            }
            if (current[px+dx][py+dy] == Contents.GOAL){
                current[px+dx][py+dy] = Contents.PLAYERONGOAL;
                eitherOr();
            }
            this.info.setText("Level: "+(level+1)+" | Steps:"+pressed);
            repaint();
            if (checkWin() == true){
                level += 1;
                initLevel(level, o);
            }
        }
    }
    public void eitherOr(){
        pressed += 1;
        if (current[px][py] == Contents.PLAYER){
            current[px][py] = Contents.EMPTY;
        }
        else{current[px][py] = Contents.GOAL;}
    }
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    public static void main(String[] args) {
        file = System.getProperty("user.dir") + "\\src\\m1.txt";
        level = 0;
        time= 0;

        File fi = new File(file);
        System.out.println(file);
        
        f = new JFrame("Sokoban");
        
        Sokoban sokoban = new Sokoban(file);
        sokoban.initLevel(level, o);
         
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setLayout(new FlowLayout());
        f.add(sokoban);
        f.pack();
        f.setVisible(true);
    }
}