import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;             //to store segments of snakes body
import java.util.Random;                //getting random x and y values to place the apples 
import javax.swing.*;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {           
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {             // to make tiles of 25x25
            this.x = x;
            this.y = y;
        }
    }  

    int boardWidth;
    int boardHeight;
    int tileSize = 30;
    
    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;  //arrylist fo snakes body

    //food
    Tile food;
    Random random;

    //game logic
    int velocityX;
    int velocityY;
    Timer gameLoop;

    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {                 //constructor
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;                         // this is used to distingush the  parameter 
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);  //default starting place 
        snakeBody = new ArrayList<Tile>();        //to save the snake body

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;
        
		//game timer
		gameLoop = new Timer(200, this);     //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();
	}	
    
    public void paintComponent(Graphics g) {       // graphics g used to draw 
		super.paintComponent(g);          
		draw(g);
	}

	public void draw(Graphics g) {                  //Grid Lines
        //Grid Lines
        for(int i = 0; i < boardWidth/tileSize; i++) {
            //(x1, y1, x2, y2)
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); 
        }

        
        g.setColor(Color.red);         //set color of apple 
        
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);

    
        g.setColor(Color.green);        //set color of snake 
       
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);              //-genetate snake body
           
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
		}

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
	}

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
		food.y = random.nextInt(boardHeight/tileSize);
	}

    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //move snake body with head
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {                 //right before the head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        //move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || //passed left border or right border
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { //passed top border or bottom border
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();  
        if (gameOver) {
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {                         //for key response 
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}