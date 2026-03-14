import javax.swing.SwingUtilities;

public class DrawingEditor {
    public static void main(String[] args) {
        // Start the window.
        SwingUtilities.invokeLater(() -> {
            EditorFrame frame = new EditorFrame();
            frame.setVisible(true);
        });
    }
}
