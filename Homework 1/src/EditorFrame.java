import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Main window of the program.
public class EditorFrame extends JFrame {
    public EditorFrame() {
        super("Simple Drawing Editor");

        DrawingPanel drawingPanel = new DrawingPanel();

        setLayout(new BorderLayout());
        add(createToolPanel(drawingPanel), BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(createInfoLabel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createToolPanel(DrawingPanel drawingPanel) {
        // Top tool buttons.
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        panel.add(createToolButton("Select / Move", Tool.SELECT, drawingPanel));
        panel.add(createToolButton("Square", Tool.SQUARE, drawingPanel));
        panel.add(createToolButton("Circle", Tool.CIRCLE, drawingPanel));
        panel.add(createToolButton("Line", Tool.LINE, drawingPanel));
        panel.add(createToolButton("Erase", Tool.ERASE, drawingPanel));

        return panel;
    }

    private JButton createToolButton(String label, Tool tool, DrawingPanel drawingPanel) {
        JButton button = new JButton(label);
        button.addActionListener(event -> drawingPanel.setCurrentTool(tool));
        return button;
    }

    private JLabel createInfoLabel() {
        // Small help text.
        JLabel label = new JLabel(
                "Choose a tool, click to create shapes, drag to move, and use line tool."
        );
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return label;
    }
}
