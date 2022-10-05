import javax.swing.*;
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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(1200, 100,500,600);

        JPanel panel = new JPanel();
        panel.setBackground(Color.gray);

        textArea.setFont(new Font("sanserif", Font.BOLD,24));
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setText("Choose a set to play using File > Open.");
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> open());
        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        cardButton.addActionListener(e -> nextCard());
        panel.add(cardButton);

        restartButton.addActionListener(e -> setFirstQuestion());
        restartButton.setVisible(false);
        panel.add(restartButton);

        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.setVisible(true);
    }
    public void open() {
        JFileChooser fileOpen = new JFileChooser();
        if (fileOpen.showOpenDialog(frame) == JFileChooser.OPEN_DIALOG) {
            loadFile(fileOpen.getSelectedFile());
        } else {
            System.out.println("No file selected");
        }
    }
    public void loadFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                createCards(line);
            }
            reader.close();
            setFirstQuestion();
            restartButton.setVisible(true);
        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        }
    }
    public void setFirstQuestion() {
        isQuestion = true;
        cardNumber = 0;
        textArea.setText(cardList.get(cardNumber).getQuestion());
        cardButton.setText("Check answer");
    }
    public void createCards(String line) {
        String[] result = line.split("/");
        FlashCard card = new FlashCard();
        card.setQuestion(result[0]);
        card.setAnswer(result[1]);
        cardList.add(card);
    }
    public void nextCard() {
        if (cardNumber < (cardList.size())) {
            if (isQuestion) {
                isQuestion = false;
                textArea.setText(cardList.get(cardNumber).getAnswer());
                cardNumber++;
                if (cardNumber < cardList.size()) {
                    cardButton.setText("Next question");
                } else { cardButton.setText("No more cards!"); }
            } else {
                isQuestion = true;
                cardButton.setText("Check answer");
                textArea.setText(cardList.get(cardNumber).getQuestion());
            }
        }
    }
}
