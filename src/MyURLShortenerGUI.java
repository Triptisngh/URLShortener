import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import com.google.gson.*;
import com.google.gson.Gson;

public class MyURLShortenerGUI {
    private JFrame frame;
    private JLabel label;
    private JTextField textField;
    private JButton shortenButton;
    private JPanel panel;
    private Gson gson;

    private static final String URL_FILE = "urls.json";
    private static final String BASE_URL = "https://shrt.co/";

    public MyURLShortenerGUI() {
        frame = new JFrame("My URL Shortener");
        label = new JLabel("Enter a URL to shorten:");
        textField = new JTextField(30);
        shortenButton = new JButton("Shorten URL");
        panel = new JPanel();
        gson = new Gson();

        // Load saved URLs from file
        JsonObject jsonObject = loadUrlsFromFile();
        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }

        // Set up GUI components
        JsonObject finalJsonObject = jsonObject;
        shortenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String longUrl = textField.getText();
                String shortUrl = BASE_URL + generateShortUrl();

                // Save URL to JSON object
                finalJsonObject.addProperty(shortUrl, longUrl);

                // Save updated JSON object to file
                saveUrlsToFile(finalJsonObject);

                // Show short URL to user
                JOptionPane.showMessageDialog(frame, "Your shortened URL is: " + shortUrl);
            }
        });
        panel.add(label);
        panel.add(textField);
        panel.add(shortenButton);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200);
        frame.setVisible(true);
    }

    // Generate a short URL based on current time in milliseconds
    private String generateShortUrl() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    // Load URLs from file and return JSON object
    private JsonObject loadUrlsFromFile() {
        try {
            FileReader fileReader = new FileReader(URL_FILE);
            JsonElement jsonElement = gson.fromJson(fileReader, JsonElement.class);
            return jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    // Save URLs to file
    private void saveUrlsToFile(JsonObject jsonObject) {
        try {
            FileWriter fileWriter = new FileWriter(URL_FILE);
            gson.toJson(jsonObject, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyURLShortenerGUI();
    }
}
