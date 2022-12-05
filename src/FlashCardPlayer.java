import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FlashCardPlayer {
    JFrame frame = new JFrame("Quiz Card Player");
    JTextArea textArea = new JTextArea(6,20);
    JButton cardButton = new JButton("Check answer");
    JButton restartButton = new JButton("Reset deck");
    boolean isQuestion = true;
    ArrayList<FlashCard> cardList = new ArrayList<>();
    int cardNumber = 0;
    public FlashCardPlayer() {
        this.go();
    }
    public void go() {
        createGUI();
    }
    public void createGUI() {
        //set main frame and content panel parameters
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(400, 150,500,600);

        JPanel panel = new JPanel();
        panel.setBackground(Color.gray);

        //create text area & scroll pane for questions and answers
        textArea.setFont(new Font("sanserif", Font.BOLD,24));
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setText("Choose a set to play using File > Open.");
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        //create navigation bar and associated drop down elements
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> open());
        fileMenu.add(openMenuItem);

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        //create card button
        cardButton.addActionListener(e -> {
            if (!cardButton.getText().equals("No more cards!")) { //skip if all cards are used
                nextCard();
            }
        });
        panel.add(cardButton);

        //create restart button
        restartButton.addActionListener(e -> setFirstQuestion());
        restartButton.setVisible(false);
        panel.add(restartButton);

        //add content panel to frame, set layout style, and make all elements visible
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.setVisible(true);
    }
    public void open() { //creates dialog menu for opening files
        JFileChooser fileOpen = new JFileChooser();
        FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Text files", "txt");
        fileOpen.setFileFilter(textFileFilter);

        if (fileOpen.showOpenDialog(frame) == JFileChooser.OPEN_DIALOG) { //only executes if open button is clicked in dialog pop up
            loadFile(fileOpen.getSelectedFile());
        } else {
           System.out.println("No file selected");
        }
    }
    public void loadFile(File file) { //loads text file and sends each line to be remade into a card
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                createCard(line);
            }
            reader.close();
            setFirstQuestion();
            restartButton.setVisible(true);
        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        }
    }
    public void setFirstQuestion() { //displays first question from loaded set
        isQuestion = true;
        cardNumber = 0;
        textArea.setText(cardList.get(cardNumber).getQuestion());
        cardButton.setText("Check answer");
    }
    public void createCard(String line) { //separates line from file into question and answer & assigns to a new card
        String[] result = line.split("/");
        FlashCard card = new FlashCard();
        card.setQuestion(result[0]);
        card.setAnswer(result[1]);
        cardList.add(card);
    }
    public void nextCard() {
        if (isQuestion) { //will display answer portion of card if currently displaying the question
            isQuestion = false;
            textArea.setText(cardList.get(cardNumber).getAnswer());
            cardNumber++;
            if (cardNumber < cardList.size()) {
                cardButton.setText("Next question");
            } else { cardButton.setText("No more cards!"); }
        } else { //will display next card's question if currently displaying an answer
            isQuestion = true;
            cardButton.setText("Check answer");
            textArea.setText(cardList.get(cardNumber).getQuestion());
        }
    }
}
