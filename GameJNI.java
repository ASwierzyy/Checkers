public class GameJNI {
    static {
        System.loadLibrary("ProjectJNI1");
    }

    public native void initializeGame();

    public native boolean makeMove(int fromRow, int fromCol, int toRow, int toCol);

    public native int[][] getBoardState();

    public native boolean isGameOver();

    public native int getCurrentPlayer();

    public native void resetGame();

    public native void setBoardState(int[][] boardState);
}
