public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameGUI gui = new GameGUI();
            gui.createAndShowGUI();
        });    }
}