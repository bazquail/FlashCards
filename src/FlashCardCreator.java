import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FlashCardCreator {
    JFrame frame = new JFrame("Flash Cards");
    JTextArea questionBox;
    JTextArea answerBox;
    ArrayList<FlashCard> cardList = new ArrayList<>();

    public static void main(String[] args) {
        new FlashCardCreator().go();
    }
    public void go() {
        createGUI();
    }
    public void createGUI() {
        //set main frame and content panel parameters
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(300,100,500,600);

        JPanel panel = new JPanel();
        panel.setBackground(Color.gray);

        //create question label and text area
        Font font = new Font("sanserif",Font.BOLD,24);
        String startUpMessage = "After creating all your cards, make sure to save the set using File > " +
                "Save before trying to play it!";
        questionBox = createTextArea(font, startUpMessage);
        JScrollPane questionScrollPane = createScrollPane(questionBox);
        panel.add(new JLabel("Question:"));
        panel.add(questionScrollPane);

        //create answer label and text area
        startUpMessage = "";
        answerBox = createTextArea(font, startUpMessage);
        JScrollPane answerScrollPane = createScrollPane(answerBox);
        panel.add(new JLabel("Answer:"));
        panel.add(answerScrollPane);

        //create navigation bar and associated drop down elements
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> clearAll());
        fileMenu.add(newMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> saveCards());
        fileMenu.add(saveMenuItem);

        JMenuItem playMenuItem = new JMenuItem("Play");
        playMenuItem.addActionListener(e -> new FlashCardPlayer());
        fileMenu.add(playMenuItem);

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        //create card button
        JButton createButton = new JButton("Create card!");
        createButton.addActionListener(a -> createCard());
        panel.add(createButton);

        //add content panel to frame, set layout style, and make all elements visible
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.setVisible(true);
    }
    public void clearCard() { //used after adding card to card list
        questionBox.setText("");
        answerBox.setText("");
        questionBox.requestFocus();
    }
    public void clearAll() { //used to abandon current, unsaved set of cards and start new
        clearCard();
        cardList.clear();
    }
    public void createCard() {
        if (!answerBox.getText().equals("") && !questionBox.getText().equals("")) {
            FlashCard card = new FlashCard();
            card.setQuestion(questionBox.getText());
            card.setAnswer(answerBox.getText());
            cardList.add(card);
            clearCard();
        } else {
            System.out.println("Type something in both boxes!");
        }
    }
    public void saveCards() { //creates dialog menu for saving files
        if (!cardList.isEmpty()) { //skip if there is nothing to save
            JFileChooser fileSave = new JFileChooser();
            FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Text files", "txt");
            fileSave.setFileFilter(textFileFilter);

            if (fileSave.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) { //only executes if save button is clicked in dialog pop up
                saveFile(fileSave.getSelectedFile());
            } else {
                System.out.println("Save Failed: save dialog aborted.");
            }
        } else {
            System.out.println("Save Failed: no cards created for current set.");
        }
    }
    public void saveFile(File file) { //saves card set as a text file with questions and associated answers separated by /
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (FlashCard card : cardList) {
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to file: " + e.getMessage());
        }
    }
    public JTextArea createTextArea(Font font, String text) { //sets text area parameters
        JTextArea textArea = new JTextArea(6,20);
        textArea.setFont(font);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(text);
        return textArea;
    }
    public JScrollPane createScrollPane(JTextArea textArea) { //takes text area and assigns it to a scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }
}