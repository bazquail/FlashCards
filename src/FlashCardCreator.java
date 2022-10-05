import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FlashCardCreator {
    static JFrame frame = new JFrame("Flash Cards");
    static JTextArea questionBox;
    static JTextArea answerBox;
    static ArrayList<FlashCard> cardList = new ArrayList<>();

    public static void main(String[] args) {
        FlashCardCreator engine = new FlashCardCreator();
        engine.go();
    }
    public static void go() {
        createGUI();
    }
    public static void createGUI() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(600,100,500,600);

        JPanel panel = new JPanel();
        panel.setBackground(Color.gray);

        Font font = new Font("sanserif",Font.BOLD,24);
        String startUpMessage = "After creating all your cards, make sure to save the set using File > " +
                "Save before trying to play it!";
        questionBox = createTextArea(font, startUpMessage);
        JScrollPane qScrollPane = createScrollPane(questionBox);
        panel.add(new JLabel("Question:"));
        panel.add(qScrollPane);
        startUpMessage = "";
        answerBox = createTextArea(font, startUpMessage);
        JScrollPane aScrollPane = createScrollPane(answerBox);
        panel.add(new JLabel("Answer:"));
        panel.add(aScrollPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> clearAll());
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> saveCards());
        JMenuItem playMenuItem = new JMenuItem("Play");
        playMenuItem.addActionListener(e -> new FlashCardPlayer());
        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(playMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        JButton createButton = new JButton("Create card!");
        createButton.addActionListener(a -> createCard());
        panel.add(createButton);

        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.setVisible(true);
    }
    public static void clearCard() {
        questionBox.setText("");
        answerBox.setText("");
        questionBox.requestFocus();
    }
    public static void clearAll() {
        clearCard();
        cardList.clear();
    }
    public static void createCard() {
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
    public static void saveCards() {
        JFileChooser fileSave = new JFileChooser();
        fileSave.showSaveDialog(frame);
        saveFile(fileSave.getSelectedFile());
    }
    public static void saveFile(File file) {
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
    public static JTextArea createTextArea(Font font, String text) {
        JTextArea textArea = new JTextArea(6,20);
        textArea.setFont(font);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(text);
        return textArea;
    }
    public static JScrollPane createScrollPane(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }
}