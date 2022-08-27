package src;
import javax.swing.JFrame;

import java.awt.EventQueue;

public class Application extends JFrame {

    private Board board;

    static private int tileSize = 10;
    
    public Application() {
        initUI();
    }

    private void initUI() {
        // Pass the current application to the board so the board can use the render function
        board = new Board(tileSize);
        
        add(board);

        setSize(board.settings.getWidth() * tileSize, board.settings.getHeight() * tileSize);

        setTitle("Fronteirscape");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Generate the agents and tiles based on board state
        // board.render();
    }

    public static void main(String[] args) {
        System.out.print("Starting\n");

        EventQueue.invokeLater(() -> {
            Application ex = new Application();
            ex.setVisible(true);
        });
    }
 }
