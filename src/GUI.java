import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class GUI extends JFrame {
    private JButton outputDirectoryButton;
    private JTextField textField;
    private JButton generateButton;

    public GUI() {
        outputDirectoryButton = new JButton("Choose Output Directory");
        textField = new JTextField("pociag_do_stacji bydgoszcz_glowna odjedzie_z_toru pierwszego", 50);
        generateButton = new JButton("Generate");

        outputDirectoryButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                SpeechSynthesizer.setOutputDirectory(selectedFile.getAbsolutePath());
            }
        });

        generateButton.addActionListener(e -> {
            String text = textField.getText();

            // Generate the audio file
            List<String> files = SpeechSynthesizer.getFilesFromText(text);
            SpeechSynthesizer.combine(files);

            JOptionPane.showMessageDialog(null, "Audio file generated successfully!");
        });

        setLayout(new FlowLayout());
        add(outputDirectoryButton);
        add(new JLabel("Text:"));
        add(textField);
        add(generateButton);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}