import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Team 04
 * 640, 480, size of window
 * 
 * -----Image Library-----
 * Folder: \images
 * 
 * Character Standing: char_stand.jpg
 * 
 * Image sources:
 * 
 * BUGS:
 * http://opengameart.org/content/old-bugs
 * "But if you wish to give me credit, then you can mention "Master484" as the original author, and maybe also put a link to my website: http://m484games.ucoz.com/"
 * 
 * FLOOR:
 * http://opengameart.org/content/printed-circuit-board-texture
 * 
 * CHARACTER:
 * http://opengameart.org/content/man-walking-animation
 * 
 * WEAPON:
 * http://opengameart.org/content/floppy-disk
 *
 */
public class gamePanel extends JPanel implements KeyListener, ActionListener
{
    
    mainPanel mainPin; //mainPanel class
    player testP; // test player class

    //////Player//////////
    
    JLabel testL; // label for player name
    int bX; // X coordinate of player
    int bY; // Y coordinate of player
    int bWidth; // value of width of player
    int bHeight; // value of height of player
    int increaseX; // value of increase in movment on x coor
    int decreaseX; // value of decrease in movement on x coor
    int pDirect; // set direction of character based on movement - 0: right, 1: left
    Timer moveR; //right movement timer
    int moveDelay;
    Timer moveL; //left movement timer
    

    /////Jumping/////
    int jumpY; // value of jump height
    Timer jump; // timer for jump sequence
    int jumpDelay; // delay value for jump sequence
    int jumpIncrement; // increment of jump
    int jumpItotal; // add up increments to compare to min value
    int jumpNumber; //jump up
    int jumpNumberDown;//jump down
    int jumpInProgress; // 0 is no, 1 is yes
    
    ////Sword////
    int sX; // X coor for a sword button
    int sY; // Y coor for a sword button
    int sW; // widght of sword button
    int sH; // height of sword button
    JButton swordB; //sword button
    ImageIcon sordL; //sword visual
    
    ////Border////
    int maxY; // max y value to check edge of screen
    int minY; // min y value to check edge of screen
    int maxX; // max x value to check edge of screen
    int minX; // min x value to check edge of screen
    ImageIcon floorI;//floor visual
    JButton floorB;//floor button
    
    ////Enemy////
    int eX; // x coordinate of enemy
    int eY; // y coordinate of enemy
    int enemyDelay; // delay value for enemy move sequence
    int eCount; // determine if additional enemies are added. 1 = one, 2 = two.   
    Timer enemyMove; //
    enemy testE2; // testing enemy class
    enemy testE3; // another enemy
    
    ////Player Data & Score////
    String pName;// Holds player name from mainPanel
    String dText; // Holds difficulty from mainPanel
    String scoreFile ; // string to hold score xml file name
    String scoreResult; //final score
    JLabel diffL; // label for difficulty level
    XML_240 scoreStore; // xml reader and writer class object to store score data
    scoreBoard scoreB; // create scoreboard class to store scores and other data
    JLabel yourScore; // to display score after game is done
    
    ////Game State////
    boolean gameFinished; // is the game over?
    boolean gamePaused; // is the game paused? game screen starts paused so that enemies are not moving. 
    JButton gameOver; // game over button
    String gameOverText; // text for game over button
    
    gamePanel(mainPanel informedMain)
    {
        super();
        setBackground(Color.white);
        setLayout(null);
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();
        mainPin = informedMain;
        mainPin.save.addActionListener(this);

        // set min and max of y and x values
        maxY = 20;
        minY = 360;
        maxX = 560;
        minX = 20;
        
        ////Player values and movement////
        increaseX = 10;
        decreaseX = -10;
        jumpY = -60;
        jumpDelay = 100;
        jumpIncrement = jumpY/3;
        jumpItotal = 0;
        jumpNumber = 0;
        jumpNumberDown = 0;
        moveDelay = 75;
        moveR = new Timer(moveDelay,this);
        moveL = new Timer(moveDelay,this);
        bX = maxX/2; //player x coord
        bY = minY;//player y coord
        bWidth = 30;//player button size
        bHeight = 32;//player button size
        
        ////Creates player////
        if(testP == null){
            testP= new player(); 
        }
        add(testP);
        testP.setBounds(bX,bY ,bWidth ,bHeight ); // set location of button  setBounds(x cor, y cor , width , height 
        
        ////sword////
        sW = 20;//sword width
        sH = 15;//sword height
        sordL = new ImageIcon("images/sword.jpg");
        swordB = new JButton();
        add(swordB);
        swordB.setIcon(sordL);
        swordB.setVisible(false);
        
        ////enemy values and movement////
        enemyDelay = 50;
        pDirect = 0;
        eCount = 1;
        eX = maxX;// x coord
        eY = minY;//y coord
        enemyMove = new Timer(enemyDelay, this);//enemt movement timer
        enemyMove.start();
        System.out.println(eCount);//displays enemy count

        ///creates enemy////
        testE3 = new enemy(maxY, minY, maxX, minX, decreaseX, enemyDelay);
        testE2 = new enemy(maxY, minY, maxX, minX, decreaseX, enemyDelay);
        
        ////scoreboard////
        scoreB = new scoreBoard();
        scoreFile = "scoresheet.xml";
        scoreStore = new XML_240();
        scoreResult = "Game Over. Your score is: ";
        yourScore = new JLabel("No score yet.");// final score jlabel
        
        ////game state////
        gameFinished = false;
        gamePaused = true;
        gameOverText = "Main Menu";
        gameOver = new JButton();
        
        ////Floor////
        floorI = new ImageIcon("images/floor.jpg");
        floorB = new JButton();
        floorB.setBorderPainted(false);
        floorB.setBounds(minX-30, 390, 670, 61);
        floorB.setIcon(floorI);
        add(floorB);
 
        ////Difficulty settings////
        if(testP.getDifficulty().equals("Easy")){
            enemyDelay = 250;
            eCount = 1;
            testE2.setDelayEasy();
            add(testE2);
            validate();
            repaint();
            
            if(testE3 != null){
                remove(testE3);
                validate();
                repaint();
            }
        }
        
        if(testP.getDifficulty().equals("Normal")){
            enemyDelay = 200;
            eCount = 1;
            testE2.setDelayNormal();
            add(testE2);
            validate();
            repaint();
            
            if(testE3 != null){
                remove(testE3);
                validate();
                repaint();
            }
        }
        
        if(testP.getDifficulty().equals("Hard")){
            enemyDelay = 100;
            eCount = 2;
            testE3.setDelayHard();
            add(testE3);
            testE2.setDelayNormal();
            add(testE2);
            validate();
            repaint();  
        }
        
        if(testP.getDifficulty().equals("Hardest")){
            enemyDelay = 50;
            eCount = 2;
            testE3.setDelayHardest();
            add(testE3);
            testE2.setDelayHard();
            add(testE2);
            validate();
            repaint(); 
        }
        
        ////Displaying player name and diff////
        pName = "Not Set";
        dText = "Not Set";
        testL = new JLabel();//displays player name
        diffL = new JLabel();//display diff
        testL.setText("Player: " + testP.getName());
        diffL.setText("Difficulty: " + testP.getDifficulty());
        diffL.setLocation(minX + 105, 30);
        testL.setLocation(minX, 30);
        testL.setSize(100, 40);
        diffL.setSize(100, 40);
        add(testL);
        add(diffL);
    }
    
    public void keyPressed(KeyEvent e)
    {
        int k = e.getKeyCode();
        
        ////Key movement////
        if(k==e.VK_RIGHT){
            testP.setIconR(); // use method to set icon right facing
            pDirect = 0;
            moveR.start();
        }
        
        if(k==e.VK_LEFT){
            testP.setIconL(); // use method to set icon left facing
            pDirect = 1;
            moveL.start();
        }
        
        if(k==e.VK_DOWN){
            if (pDirect == 0){
               sX = bX+40;
               sY = bY+10;
               swordB.setBounds(sX,sY , sW, sH);
                
            }if(pDirect == 1){
                
                sX = bX-20;
                sY = bY+10;
                swordB.setBounds(sX,sY , sW, sH);
            }
            
            swordB.setIcon(sordL);
            swordB.setVisible(true);
            checkAttack();  
        }
        
        ////Jumping////
        if(k==e.VK_SPACE){
            if (jumpInProgress == 0)
            {
                jumpInProgress = 1;
                bY=bY+(jumpY/3);
                testP.setBounds(bX,bY ,bWidth ,bHeight );       
                jumpNumber++;  
                jump = new Timer(jumpDelay,this);
                jump.start();
            }
        }
    }
    
    public void keyReleased(KeyEvent e){
        
        int k = e.getKeyCode();
        
        if(k==e.VK_RIGHT || k==e.VK_LEFT ){
            if(pDirect == 0){
                testP.setIconStand();
            }
            
            if(pDirect == 1){
                testP.setIconStandL();
            }
            
            moveR.stop();
            moveL.stop();
            checkCollision();
        }
        
        if(k==e.VK_DOWN){
            swordB.setVisible(false);  
        }
    }
    
    public void keyTyped(KeyEvent e){}
    
    public void actionPerformed(ActionEvent e)
    {
        Object obj = e.getSource();
        
        if(obj == enemyMove){
            if(testE2.geteX() < (minX-60) || ((testE2.geteX()) > (maxX + 60))){
                remove(testE2);
                validate();
                repaint();
                addE2();
            }
            
            if(eCount == 2){
                if(testE3.geteX() < (minX-60) || ((testE3.geteX()) > (maxX + 60))){
                    remove(testE3);
                    validate();
                    repaint();
                    addE3();
                }               
            }
           checkCollision();
        }
        
        if(obj == jump){
            if(jumpNumber <= 3){
                bY=bY+(jumpY/3);
                testP.setBounds(bX,bY ,bWidth ,bHeight );
                checkCollision();
                jumpNumber++;
            }
            
            if(jumpNumber == 4){
                if(jumpNumberDown <= 3){   
                    bY=bY-(jumpY/3);
                    testP.setBounds(bX,bY ,bWidth ,bHeight );
                    checkCollision();
                    jumpNumberDown++;
                }
            }
                    
            if(jumpNumberDown == 4){
               stopJump();
            }           
        }
        
        if(obj == moveR){
            if(bX+increaseX < maxX){ 
                bX=bX+increaseX; 
            }
            testP.setBounds(bX,bY ,bWidth ,bHeight );
            checkCollision();
        }
        
        if(obj == moveL){
            if(bX+increaseX > minX){
                bX=bX+decreaseX;
            }
            testP.setBounds(bX,bY ,bWidth ,bHeight );
            checkCollision();
        }
        
        if(obj == mainPin.save){
            pName = mainPin.player.getText();
            testP.setName(pName);
            dText = (String)mainPin.difficulty.getSelectedItem();
            testP.setDifficulty(dText);
            testL.setText("Player: " + testP.getName());
            diffL.setText("Difficulty: " + testP.getDifficulty());
            
            //----------Set Difficulty Speed------------
            
            if(testP.getDifficulty().equals("Easy")){
            
                enemyDelay = 250;
                eCount = 1;
                testE2.setDelayEasy();
                add(testE2);
                validate();
                repaint();
            
                if(testE3 != null){
                    remove(testE3);
                    validate();
                    repaint();
                }
            }
        
            if(testP.getDifficulty().equals("Normal")){
            
                enemyDelay = 200;
                eCount = 1;
                testE2.setDelayNormal();
                add(testE2);
                validate();
                repaint();
            
                if(testE3 != null){
                    remove(testE3);
                    validate();
                    repaint();
                }
            }
        
            if(testP.getDifficulty().equals("Hard")){
            
                enemyDelay = 100;
                eCount = 2;
                testE3.setDelayHard();
                add(testE3);
                testE2.setDelayNormal();
                add(testE2);
                validate();
                repaint();   
            }
        
            if(testP.getDifficulty().equals("Hardest")){
            
                enemyDelay = 50;
                eCount = 2;
                testE3.setDelayHardest();
                add(testE3);
                testE2.setDelayHard();
                add(testE2);
                validate();
                repaint();
            }
        }     
    }
    
    void checkCollision(){
        
        if(gameFinished == false){
        
            testE2.setupCollision();
            
            if((bX == testE2.geteX() || ( bX <= testE2.collisionXmax && bX >= testE2.collisionXmin))&& bY == testE2.geteY()){
                System.out.println("Collision!");
                scoreB.setBoard(testP.getScore());
                scoreStore.openWriterXML(scoreFile);
                scoreStore.writeObject(scoreB);
                scoreStore.closeWriterXML();
                stopGame();
            }
        
            if(eCount == 2){
                testE3.setupCollision();
  
                if((bX == testE3.geteX() || ( bX <= testE3.collisionXmax && bX >= testE3.collisionXmin))&& bY == testE3.geteY()){
                    System.out.println("Collision!");
                    scoreB.setBoard(testP.getScore());
                    scoreStore.openWriterXML(scoreFile);
                    scoreStore.writeObject(scoreB);
                    scoreStore.closeWriterXML();
                    stopGame();
                }
            }
        }
    }
    
    void checkAttack(){
        if(gameFinished == false){
      
            if(sX < (testE2.geteX() + bWidth/2) && sX > (testE2.geteX() - bHeight/2)){
                System.out.print("Successful Hit!");
                testP.setScore(testP.getScore()+1);
                remove(testE2); // Remove enemy
                validate();
                repaint();
                System.out.print(testP.getScore());
                addE2();
            }
        
            if(sX < (testE3.geteX() + bWidth/2) && sX > (testE3.geteX() - bHeight/2)){
                System.out.print("Successful Hit!");
                testP.setScore(testP.getScore()+1);
                remove(testE3); // Remove enemy
                validate();
                repaint();
                System.out.print(testP.getScore());
                addE3();
            }
        }
    }
    
    void stopJump(){
        jump.stop();
        jumpNumberDown = 0;
        jumpNumber = 0;
        jumpInProgress = 0;
    }
    
    void stopGame(){
        enemyMove.stop(); // Stop addition of new enemies
        gameFinished = true; // set game finished to true
        remove(testE2); // Remove enemy
        remove(testE3); // Remove enemy
        yourScore.setText(scoreResult + " " + testP.getScore() + ". " + "You may restart in the Main menu."); // set label text
        yourScore.setLocation(maxX/2-165, minY/2); // set label location
        yourScore.setSize(400, 40); // set lable size
        add(yourScore); // add label
        
        gameOver.setLocation(maxX/2-35, minY/2+30);
        gameOver.setSize(95, 35);
        gameOver.setText(gameOverText);
        add(gameOver);
        
        validate();
        repaint();
    }
    
    void unPause(){
        if(gamePaused == true){
            testE2.startE();
            
            if(eCount == 2){
                testE3.startE();
            }
            
            gamePaused = false;
        }
    }
    
    // ---- Add a new E2 enemy
    void addE2(){
        testE2 = new enemy(maxY, minY, maxX, minX, decreaseX, enemyDelay);
                
        if(testP.getDifficulty().equals("Easy")){
            testE2.setDelayEasy();
        }
                
        if(testP.getDifficulty().equals("Hard")){
            testE2.setDelayNormal();
        }
                
        if(testP.getDifficulty().equals("Hardest")){
            testE2.setDelayHard();
        }
                
        add(testE2);
                
        if(gamePaused == false){
            testE2.startE();
        }
    }
    
    //--------- Add a new E3 enemy
    void addE3(){
        if(testP.getDifficulty().equals("Hard")){
                
            testE3 = new enemy(maxY, minY, maxX, minX, decreaseX, enemyDelay);
            add(testE3);
            testE3.setDelayHard();
                
            if(gamePaused == false){
                testE3.startE();
            }
            
        }
                
        if(testP.getDifficulty().equals("Hardest")){
                
            testE3 = new enemy(maxY, minY, maxX, minX, decreaseX, enemyDelay);
            add(testE3);
            testE3.setDelayHardest();
                
            if(gamePaused == false){
                testE3.startE();
            }  
        }
    }
    
    public void resetGame(){
        
        requestFocusInWindow();
        testP.setScore(0);
        gameFinished = false;
        gamePaused = true;
        pDirect = 0;
        eCount = 1;
        
        if(testE3 != null){
            remove(testE3);
            validate();
            repaint();
        }
        
        if(testE2 != null){
            remove(testE2);
            validate();
            repaint();
        }
        
        if(yourScore != null){
            remove(yourScore);
            validate();
            repaint();
        }
        
        if(gameOver != null){
            remove(gameOver);
            validate();
            repaint();
        }
        
        testE3 = new enemy(maxY, minY, maxX, minX, decreaseX, enemyDelay);
        testE2 = new enemy(maxY, minY, maxX, minX, decreaseX, enemyDelay);
        
        if(testP.getDifficulty().equals("Easy")){
            enemyDelay = 250;
            eCount = 1;
            testE2.setDelayEasy();
            add(testE2);
            validate();
            repaint();
            
            if(testE3 != null){
                remove(testE3);
                validate();
                repaint();
            }
        }
        
        if(testP.getDifficulty().equals("Normal")){
            enemyDelay = 200;
            eCount = 1;
            testE2.setDelayNormal();
            add(testE2);
            validate();
            repaint();
            
            if(testE3 != null){
                remove(testE3);
                validate();
                repaint();
            }
        }
        
        if(testP.getDifficulty().equals("Hard")){
            enemyDelay = 100;
            eCount = 2;
            testE3.setDelayHard();
            add(testE3);
            testE2.setDelayNormal();
            add(testE2);
            validate();
            repaint();  
        }
        
        if(testP.getDifficulty().equals("Hardest")){
            enemyDelay = 50;
            eCount = 2;
            testE3.setDelayHardest();
            add(testE3);
            testE2.setDelayHard();
            add(testE2);
            validate();
            repaint();  
        }
        
        enemyMove.start();
        unPause();
    }
}
