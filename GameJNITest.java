import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameJNITest {
    private GameJNI game;

    @BeforeEach
    public void setUp() {
        game = new GameJNI();
        game.initializeGame();
    }

    @Test
    public void testBoardInitialization() {
        int[][] boardState = game.getBoardState();

        int[][] expectedBoard = {
                {0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {2, 0, 2, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 0, 2, 0, 2},
                {2, 0, 2, 0, 2, 0, 2, 0}
        };

        assertArrayEquals(expectedBoard, boardState);
    }

    @Test
    public void testValidMove() {
        boolean moveResult = game.makeMove(2, 1, 3, 2);
        assertTrue(moveResult, "1");

        int[][] boardState = game.getBoardState();
        assertEquals(0, boardState[2][1], "2");
        assertEquals(1, boardState[3][2], "3");
    }

    @Test
    public void testInvalidMove() {
        boolean moveResult = game.makeMove(5, 0, 4, 1);
        assertFalse(moveResult, "1");

        game.makeMove(2, 1, 3, 2);
        moveResult = game.makeMove(7, 0, 6, 1);
        assertFalse(moveResult, "2");    }


    @Test
    public void testCapturing() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(2, 2, 4, 4);
        assertTrue(moveResult,"1");

        int[][] boardState = game.getBoardState();
        assertEquals(0, boardState[3][3],"2");
        assertEquals(1, boardState[4][4], "3");    }


    @Test
    public void testBackwardsCapturing() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(4, 4, 2, 2);
        assertFalse(moveResult,"1");

        int[][] boardState = game.getBoardState();
        assertEquals(2, boardState[3][3],"2");
        assertEquals(1, boardState[4][4], "3");    }

    @Test
    public void testMandatoryCapture() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(2, 2, 3, 1);
        assertFalse(moveResult, "1");

        moveResult = game.makeMove(2, 2, 4, 4);
        assertTrue(moveResult, "2");

        int[][] boardState = game.getBoardState();
        assertEquals(0, boardState[3][3], "3");
        assertEquals(1, boardState[4][4], "4");    }

    @Test
    public void testMultiCapture() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(2, 2, 4, 4);
        assertTrue(moveResult, "1");

        assertEquals(1, game.getCurrentPlayer(), "2");

        moveResult = game.makeMove(4, 4, 6, 6);
        assertTrue(moveResult, "3");

        int[][] boardState = game.getBoardState();
        assertEquals(0, boardState[3][3], "4");
        assertEquals(0, boardState[5][5], "5");
        assertEquals(1, boardState[6][6], "6");    }

    @Test
    public void testPromotionToKing() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(5, 0, 6, 1);
        assertTrue(moveResult,"1");

        game.makeMove(5,5,4,4);

        moveResult = game.makeMove(6, 1, 7, 0);
        assertTrue(moveResult,"2");

        int[][] boardState = game.getBoardState();
        assertEquals(3, boardState[7][0],"3");    }

    @Test
    public void testKingMovementBackward() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 3},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(0, 7, 1, 6);
        assertTrue(moveResult,"1");

        int[][] boardState = game.getBoardState();
        assertEquals(0, boardState[0][7],"2");
        assertEquals(3, boardState[1][6],"3");
    }

    @Test
    public void testKingBackwardCapturing() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(7, 0, 5, 2);
        assertTrue(moveResult,"1");

        int[][] boardState = game.getBoardState();
        assertEquals(0, boardState[7][0],"2");
        assertEquals(0, boardState[6][1],"3");
        assertEquals(3, boardState[5][2],"4");
    }

    @Test
    public void testGameOverByNoPiecesLeft() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(2, 2, 3, 3);
        assertTrue(moveResult, "1");

        assertTrue(game.isGameOver(), "2");
        }




    @Test
    public void testGameOverByNoMovesLeft() {
        int[][] testBoard = {
                {0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 1, 0, 1, 0, 1, 2},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        game.setBoardState(testBoard);

        boolean moveResult = game.makeMove(2, 2, 3, 3);
        assertTrue(moveResult, "1");

        assertTrue(game.isGameOver(), "2");
    }
}