package connection;

import java.io.*;
import java.util.ArrayList;

public class Connection {
    private String filePath;

    public Connection(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<String[]> readData() {
        ArrayList<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        } catch (IOException e) {
            // Handle or log the exception based on your requirements
            e.printStackTrace();
        }

        return data;
    }

    public void writeData(ArrayList<String[]> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                String line = String.join(",", row);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle or log the exception based on your requirements
            e.printStackTrace();
        }
    }
}
