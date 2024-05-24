import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpeechSynthesizer {

    private static final String input = "src/input";
    private static String outputDirectory = System.getProperty("user.home");


    public static List<String> getFilesFromText(String text){
        List<String> output = new ArrayList<>();
        String[] splitText = text.split(" ");

        Path inputPath = Paths.get(input);

        try {
            List<Path> filePaths = Files.walk(inputPath)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            for (String word : splitText) {
                for (Path filePath : filePaths) {
                    String fileNameWithoutExtension = filePath.getFileName().toString().replaceFirst("[.][^.]+$", "");
                    if (word.equals(fileNameWithoutExtension)) {
                        output.add(filePath.toString());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading files from input package: " + e);
        }

        return output;
    }

    public static void combine(List<String> files){
        List<AudioInputStream> streamList = new ArrayList<>();

        files.forEach(file -> {
            try {
                streamList.add(AudioSystem.getAudioInputStream(new File(file)));
            } catch (UnsupportedAudioFileException | IOException e) {
                System.err.println("Error reading file: " + file  + ", details: " + e);
            }
        });

        AudioInputStream outputStream = new AudioInputStream(
                new SequenceInputStream(Collections.enumeration(streamList)),
                streamList.get(0).getFormat(),
                streamList.stream().mapToLong(AudioInputStream::getFrameLength).sum()
        );

        try {
            AudioSystem.write(outputStream, AudioFileFormat.Type.WAVE, new File(outputDirectory, "output.wav"));
        } catch (IOException e) {
            System.err.println("Error writing file: " + e);
        }
    }

    public static void setOutputDirectory(String outputDirectory) {
        SpeechSynthesizer.outputDirectory = outputDirectory;
    }
}
