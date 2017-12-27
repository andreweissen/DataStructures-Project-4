/**
 * P4GUI.java - Constructs the main interface
 * Begun 12/04/17
 * @author Andrew Eissen
 */

package commandlinecompiler;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main GUI class, extends <code>JFrame</code>
 * @see javax.swing.JFrame
 */
final class P4GUI extends JFrame {

    // Window-related variables
    private String title;
    private int width;
    private int height;

    // GUI-related variables
    private JFrame mainFrame;
    private JPanel mainPanel, userInputPanel, recompOrderPanel, firstLine, secondLine;
    private JLabel inputFileLabel, recompileLabel;
    private JTextField inputFileField, recompileField;
    private JButton buildGraphButton, topologicalButton;
    private JTextArea recompOrderTextArea;

    // DirectedGraph
    private DirectedGraph directedGraph;

    /**
     * Default constructor
     */
    public P4GUI() {
        super("Class Dependency Graph");
        this.setWindowTitle("Class Dependency Graph");
        this.setWindowWidth(600);
        this.setWindowHeight(300);
    }

    /**
     * Fully parameterized constructor
     * @param title
     * @param width
     * @param height
     */
    public P4GUI(String title, int width, int height) {
        super(title);
        this.setWindowTitle(title);
        this.setWindowWidth(width);
        this.setWindowHeight(height);
    }

    /**
     * Setter for window <code>title</code>
     * @param title
     * @return void
     */
    private void setWindowTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for window <code>width</code>
     * @param width
     * @return void
     */
    private void setWindowWidth(int width) {
        if (width < 600) {
            this.width = 600;
        } else {
            this.width = width;
        }
    }

    /**
     * Setter for window <code>height</code>
     * @param height
     * @return void
     */
    private void setWindowHeight(int height) {
        if (height < 300) {
            this.height = 300;
        } else {
            this.height = height;
        }
    }

    /**
     * Getter for <code>title</code>
     * @return this.title
     */
    private String getWindowTitle() {
        return this.title;
    }

    /**
     * Getter for <code>width</code>
     * @return this.width
     */
    private int getWindowWidth() {
        return this.width;
    }

    /**
     * Getter for <code>height</code>
     * @return this.height
     */
    private int getWindowHeight() {
        return this.height;
    }

    /**
     * Method assembles the GUI. Took some time to properly develop, as the author was initially too
     * obsessed with turning out a design that exactly matched the example provided in the rubric.
     * In the end, he elected to pursue a simpler design that mostly matched the intended look of
     * the interface.
     * @return void
     */
    private void constructGUI() {

        // Main panel setup
        this.mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Mini-panels
        this.userInputPanel = new JPanel(new GridLayout(2, 1));
        this.recompOrderPanel = new JPanel(new FlowLayout());
        this.firstLine = new JPanel(new GridLayout(1, 3, 20, 0));
        this.secondLine = new JPanel(new GridLayout(1, 3, 20, 0));

        // Mini-panel borders
        this.userInputPanel.setBorder(BorderFactory.createTitledBorder(""));
        this.recompOrderPanel.setBorder(BorderFactory.createTitledBorder("Recompilation Order"));
        this.firstLine.setBorder(BorderFactory.createEmptyBorder(20, 10, 15, 10));
        this.secondLine.setBorder(BorderFactory.createEmptyBorder(15, 10, 20, 10));

        // First line
        this.inputFileLabel = new JLabel("Input file name:", SwingConstants.CENTER);
        this.inputFileField = new JTextField();
        this.buildGraphButton = new JButton("Build Directed Graph");

        // Second line
        this.recompileLabel = new JLabel("Class to recompile:", SwingConstants.CENTER);
        this.recompileField = new JTextField();
        this.topologicalButton = new JButton("Topological Order");

        // Text Area, kinda janked
        this.recompOrderTextArea = new JTextArea(5, 50);
        this.recompOrderTextArea.setEditable(false);

        // Add elements/other mini-panels to mini-panels
        this.firstLine.add(this.inputFileLabel);
        this.firstLine.add(this.inputFileField);
        this.firstLine.add(this.buildGraphButton);

        this.secondLine.add(this.recompileLabel);
        this.secondLine.add(this.recompileField);
        this.secondLine.add(this.topologicalButton);

        this.userInputPanel.add(this.firstLine);
        this.userInputPanel.add(this.secondLine);
        this.recompOrderPanel.add(this.recompOrderTextArea);

        // Add mini-panels to mainFrame
        this.mainPanel.add(this.userInputPanel);
        this.mainPanel.add(this.recompOrderPanel);

        // Mouse handlers
        this.buildGraphButton.addMouseListener(new GUIMouseAdapter("build"));
        this.topologicalButton.addMouseListener(new GUIMouseAdapter("topological"));

        // Assemble Frame
        this.mainFrame = new JFrame(this.getWindowTitle());
        this.mainFrame.setSize(this.getWindowWidth(), this.getWindowHeight());
        this.mainFrame.setContentPane(this.mainPanel);
        this.mainFrame.setResizable(false);
        this.mainFrame.setVisible(true);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        P4GUI newGUI = new P4GUI();
        newGUI.constructGUI();
    }

    /**
     * Class for handling button clicks, extends <code>MouseAdapter</code>
     * @see java.awt.event.MouseAdapter
     */
    final class GUIMouseAdapter extends MouseAdapter {

        // Field to help distinguish between button clicks
        private String action;

        /**
         * Parameterized constructor
         * @param action
         */
        public GUIMouseAdapter(String action) {
            this.setAction(action);
        }

        /**
         * Setter for <code>action</code> field
         * @param action
         * @return void
         */
        private void setAction(String action) {
            this.action = action;
        }

        /**
         * Messy perhaps, but this method determines which click handler method to use depending on
         * the button pressed by the user.
         * @param e
         * @return void
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (this.action.equals("build")) {
                this.buildDirectedGraph();
            } else {
                this.compileInTopologicalOrder();
            }
        }

        /**
         * This method handles clicks of the "Build Directed Graph" button. The implementation
         * changed a few times during development, but the basic extraction of the user-input file
         * from the field and subsequent scanning of its contents has remained constant. To aid in
         * visualization, the contents are read and split into a series of <code>String</code>
         * arrays and added to an <code>ArrayList</code>. The resultant is then passed to the
         * <code>DirectedGraph</code> generic class to be constructed by the
         * <code>buildDirectedGraph</code> method.
         * <br />
         * <br />
         * The method catches certain improper input, such as a nonexistent file and an empty
         * <code>inputFileField</code> field. It is important to note that the graph will be built
         * regardless of any potential cycles. The cycles are detected and caught by the handlers
         * pertaining to the <code>compileInTopologicalOrder</code> handler. This does not violate
         * the design rubric, which makes no explicit indication as to which handler should catch
         * cycles, especially given the fact that some directed graphs may have cycles.
         * @return void
         */
        private void buildDirectedGraph() {
            Scanner fileScanner = null;
            String[] newStringArray;
            ArrayList<String[]> stringArrays = new ArrayList<>();
            String fileName = inputFileField.getText();

            try {
                // Catches empty input
                if (fileName.isEmpty()) {
                    this.displayStatusPopup("Error: File name required.", "Error");
                } else {
                    fileScanner = new Scanner(new BufferedReader(new FileReader(fileName)));

                    /**
                     * Splits file input into a series of <code>String</code> arrays, each of which
                     * represents a line of the input file and thus a dependency relationship
                     * between classes. The reason for this was to preserve the generic nature of
                     * the <code>DirectedGraph</code> class. Rather than have that class's methods
                     * mess around with <code>String</code>s, it made more sense for this handler to
                     * pass an <code>ArrayList</code> of arrays of the proper desired type.
                     * <br />
                     * <br />
                     * To prove the point, the author originally included a test method that passed
                     * the graph class an <code>ArrayList</code> of <code>Integer</code> arrays to
                     * test the operations thereof, a fact that would not have been possible if the
                     * class had to mess with input <code>String</code>s.
                     */
                    while (fileScanner.hasNextLine()) {
                        newStringArray = fileScanner.nextLine().split("\\s+");
                        stringArrays.add(newStringArray);
                    }

                    // Define the <code>DirectedGraph</code> and build the graph
                    directedGraph = new DirectedGraph();
                    directedGraph.buildDirectedGraph(stringArrays);
                    this.displayStatusPopup("Success: Graph built successfully.", "Success");
                }
            } catch (IOException e) {
                this.displayStatusPopup("Error: File not found.", "Error");
            } finally { // Close the reader
                if (fileScanner != null) {
                    fileScanner.close();
                }
            }
        }

        /**
         * This method is the other click handler corresponding to clicks of the "Topological
         * Order" button. This handler is notable for catching the exceptions thrown by the generic
         * <code>DirectedGraph</code> class. The reason for this organizational arrangement was per
         * the rubric's requirement regarding code containment and organization. It made logical
         * sense to have the generic methods throw exceptions that would be handled by the GUI class
         * in the form of <code>JOptionPane</code> windows. It made little sense to import Swing
         * elements to the generic class simply to display modals when the GUI class can handle such
         * cases itself.
         * <br />
         * <br />
         * In terms of specifics, the handler catches exception cases related to illegitimate button
         * presses, undefined <code>DirectedGraph</code> objects, unentered class names, nonexistent
         * class names, and cycles present in the graph itself. It takes the returned graph
         * <code>ArrayList</code> of topologically ordered nodes and assembles them into a
         * whitespace-delimited <code>String</code> to be displayed in the GUI's
         * <code>recompOrderTextArea</code>.
         * @return void
         */
        private void compileInTopologicalOrder() {
            ArrayList<String> vertices;
            String className = recompileField.getText();

            try {
                /**
                 * Included in case the user attempts to click the "Topological Order" button
                 * before actually constructing the graph.
                 */
                if (directedGraph == null) {
                    this.displayStatusPopup("Error: Directed graph not found.", "Error");

                // Empty field input handling
                } else if (className.isEmpty()) {
                    this.displayStatusPopup("Error: Select class to compile.", "Error");
                } else {
                    vertices = directedGraph.inTopologicalOrder(className);
                    recompOrderTextArea.setText(String.join(" ", vertices));
                }

            // As stated above, the use of popups necessitated the exception handling be kept here
            } catch(InvalidClassNameException e) {
                this.displayStatusPopup("Error: No class by that name found.", "Error");
                recompOrderTextArea.setText("");
            } catch(CycleOccurrenceException e) {
                this.displayStatusPopup("Error: Cycle found.", "Error");
                recompOrderTextArea.setText("");
            }
        }

        /**
         * Method simply displays a status pop-up modal depending on the nature of the operation in
         * question. The method was originally a bit more complex, but the author decided that the
         * reduction of complexity was the better course of action in this case.
         * @param message
         * @param title
         * @return void
         */
        private void displayStatusPopup(String message, String title) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}