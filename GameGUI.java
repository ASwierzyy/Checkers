import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameGUI {

    private GameJNI gameJNI;
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] boardButtons;

    private int cursorRow = 0;
    private int cursorCol = 1;

    private int selectedPieceRow = -1;
    private int selectedPieceCol = -1;

    private Icon whitePieceIcon;
    private Icon blackPieceIcon;
    private Icon whiteKingIcon;
    private Icon blackKingIcon;

    public GameGUI() {
        gameJNI = new GameJNI();
        gameJNI.initializeGame();

        initializeIcons();
    }

    public void createAndShowGUI() {
        frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardPanel = new JPanel(new GridLayout(8, 8));
        boardButtons = new JButton[8][8];

        initializeBoardButtons();

        frame.add(boardPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        updateDisplay();

        setupKeyBindings();

        frame.requestFocusInWindow();
    }

    private void initializeIcons() {
        try {
            BufferedImage whitePieceImage = ImageIO.read(getClass().getResource("/icons/WhitePawn.png"));
            BufferedImage blackPieceImage = ImageIO.read(getClass().getResource("/icons/BlackPawn.png"));
            BufferedImage whiteKingImage = ImageIO.read(getClass().getResource("/icons/WhiteKing.png"));
            BufferedImage blackKingImage = ImageIO.read(getClass().getResource("/icons/BlackKing.png"));

            whitePieceIcon = scaleIcon(whitePieceImage, 50, 50);
            blackPieceIcon = scaleIcon(blackPieceImage, 50, 50);
            whiteKingIcon = scaleIcon(whiteKingImage, 50, 50);
            blackKingIcon = scaleIcon(blackKingImage, 50, 50);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading piece images.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Icon scaleIcon(BufferedImage image, int width, int height) {
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void initializeBoardButtons() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(60, 60));

                if ((row + col) % 2 == 0) {
                    button.setBackground(Color.WHITE);
                    button.setEnabled(false);
                } else {
                    button.setBackground(Color.GRAY);
                }

                button.setOpaque(true);
                button.setBorder(null);
                button.setFocusable(false);

                final int currentRow = row;
                final int currentCol = col;

                button.addActionListener(e -> handleCellClick(currentRow, currentCol));

                boardButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
    }

    private void setupKeyBindings() {
        InputMap inputMap = boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = boardPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "selectOrMove");

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCursor(0, -1);
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCursor(0, 1);
            }
        });
        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCursor(-1, 0);
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCursor(1, 0);
            }
        });
        actionMap.put("selectOrMove", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCellClick(cursorRow, cursorCol);
            }
        });
    }

    private void moveCursor(int diffRow, int diffCol) {
        removeCursorHighlight(cursorRow, cursorCol);

        cursorRow += diffRow;
        cursorCol += diffCol;

        if (cursorRow < 0) cursorRow = 0;
        if (cursorRow > 7) cursorRow = 7;
        if (cursorCol < 0) cursorCol = 0;
        if (cursorCol > 7) cursorCol = 7;

        highlightCursor(cursorRow, cursorCol);
    }

    private void highlightCursor(int row, int col) {
        JButton button = boardButtons[row][col];
        button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
    }

    private void removeCursorHighlight(int row, int col) {
        JButton button = boardButtons[row][col];
        button.setBorder(null);
    }

    private void handleCellClick(int row, int col) {
        removeCursorHighlight(cursorRow, cursorCol);

        cursorRow = row;
        cursorCol = col;

        highlightCursor(cursorRow, cursorCol);

        int[][] boardState = gameJNI.getBoardState();
        int piece = boardState[row][col];
        int currentPlayer = gameJNI.getCurrentPlayer();

        if (selectedPieceRow == -1 && selectedPieceCol == -1) {
            if ((currentPlayer == 1 && (piece == 1 || piece == 3)) ||
                    (currentPlayer == 2 && (piece == 2 || piece == 4))) {
                selectedPieceRow = row;
                selectedPieceCol = col;
                highlightSelectedPiece(selectedPieceRow, selectedPieceCol);
            } else {
                JOptionPane.showMessageDialog(frame, "Select one of your own pieces.");
            }
        } else {
            boolean success = gameJNI.makeMove(selectedPieceRow, selectedPieceCol, row, col);
            if (success) {
                updateDisplay();

                if (gameJNI.isGameOver()) {
                    JOptionPane.showMessageDialog(frame, "Game Over! Player " + gameJNI.getCurrentPlayer() + " wins.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid move / Capturing is mandatory");
            }

            removeSelectedPieceHighlight(selectedPieceRow, selectedPieceCol);
            selectedPieceRow = -1;
            selectedPieceCol = -1;
        }
    }

    private void highlightSelectedPiece(int row, int col) {
        JButton button = boardButtons[row][col];
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
    }

    private void removeSelectedPieceHighlight(int row, int col) {
        JButton button = boardButtons[row][col];
        button.setBorder(null);
    }

    private void updateDisplay() {
        int[][] boardState = gameJNI.getBoardState();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = boardButtons[row][col];
                int piece = boardState[row][col];

                button.setIcon(null);

                if (piece == 1) {
                    button.setIcon(whitePieceIcon);
                } else if (piece == 2) {
                    button.setIcon(blackPieceIcon);
                } else if (piece == 3) {
                    button.setIcon(whiteKingIcon);
                } else if (piece == 4) {
                    button.setIcon(blackKingIcon);
                }

                button.setBorder(null);
            }
        }

        if (selectedPieceRow != -1 && selectedPieceCol != -1) {
            highlightSelectedPiece(selectedPieceRow, selectedPieceCol);
        }

        highlightCursor(cursorRow, cursorCol);

        frame.setTitle("Player " + gameJNI.getCurrentPlayer() + "'s Turn");
    }
}