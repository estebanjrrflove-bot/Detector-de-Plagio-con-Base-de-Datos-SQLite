package DetectorPlagio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeteccionPlagio extends JFrame {
    private JTextField inputTextField;
    private JButton checkButton;
    private JTextArea resultTextArea;
    private PlagiarismChecker plagiarismChecker;

    public DeteccionPlagio() {
        plagiarismChecker = new PlagiarismChecker();

        setTitle("Detector de plagio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        createUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputTextField = new JTextField();
        checkButton = new JButton("Comprobar plagio");
        resultTextArea = new JTextArea(6, 30);
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);

        panel.add(inputTextField, BorderLayout.NORTH);
        panel.add(checkButton, BorderLayout.CENTER);
        panel.add(new JScrollPane(resultTextArea), BorderLayout.SOUTH);

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPlagiarism();
            }
        });

        add(panel);
    }

    private void checkPlagiarism() {
        String inputText = inputTextField.getText();
        ResultChecker result = plagiarismChecker.verifyPlagiarism(inputText);

        if (result != null) {
            resultTextArea.setText(result.toString());
        } else {
            resultTextArea.setText("Error al comprobar el plagio.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeteccionPlagio();
            }
        });
    }
}