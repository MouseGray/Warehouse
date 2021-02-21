package com.company;

import java.io.*;
import java.util.*;

public class Config {
    private static final String delimiter = "#";
    private static final String newLine = "\r\n";

    private static final String property_path = "path";
    private static final String property_filters = "filters";

    private String dataFilePath = System.getProperty("user.dir") + "\\Data.wh";
    private String[] itemFilters;

    private void create(String filename) {
        File file = new File(filename);

        try {
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                try {
                    bufferedWriter.write(delimiter + property_path);
                    bufferedWriter.newLine();
                    bufferedWriter.write(dataFilePath);
                    bufferedWriter.newLine();
                    bufferedWriter.write(delimiter + property_filters);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    // nothing do
                } finally {
                    writer.close();
                }
            }
        } catch (IOException e) {
            // nothing do
        }
    }

    private void read(String filename) throws FileNotFoundException {
        HashMap<String, String> properties = getProperties(filename);
        dataFilePath = properties.get(property_path);
        String filters = properties.get(property_filters);
        if (filters == null)
            itemFilters = new String[0];
        else
            itemFilters = properties.get(property_filters).split(newLine);
    }

    private HashMap<String, String> getProperties(String filename) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(filename));
        scan.useDelimiter(delimiter);

        HashMap<String, String> properties = new HashMap<>();

        while (scan.hasNext()) {
            String line = scan.next().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(newLine, 2);
            if (parts.length != 2) continue;
            properties.put(parts[0].trim(), parts[1].trim());
        }
        scan.close();
        return properties;
    }

    public void load(String filename) {
        try {
            read(filename);
        } catch (FileNotFoundException e) {
            create(filename);
        }
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public String[] getItemFilters() {
        return this.itemFilters;
    }
}
