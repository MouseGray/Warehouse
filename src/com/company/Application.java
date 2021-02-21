package com.company;

import Storage.*;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Application {
    Storage storage = new Storage();
    Config config;
    boolean isInitialized;

    public Application(Config config) {
        this.config = config;
        this.isInitialized = false;
    }

    public void load() {
        if (config.getDataFilePath() == null) {
            System.out.println("[ERROR://config] Property path not found");
            return;
        }
        try {
            Scanner scan = new Scanner(new File(config.getDataFilePath()));
            while (scan.hasNext()) {
                String line = scan.nextLine();
                String[] parts = line.split("[|]");
                if (parts.length < 3) continue;
                try {
                    int count = Integer.parseInt(parts[2]);
                    storage.add(config.getItemFilters(),new Item(parts[0], parts[1]), count);
                } catch (NumberFormatException e) {
                    // nothing do
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR://data] File not found: " + config.getDataFilePath());
        }
        this.isInitialized = true;
    }

    private void printListOfItemStack(List<ItemStack> list) {
        if (list.isEmpty())
            System.out.println("--Empty--");
        else
            for(ItemStack i: list) System.out.println(i.toString());
    }

    private void add(String line) {
        String[] parts = line.split("\\s");
        if(storage.add(config.getItemFilters(), new Item(parts[1], parts[2])))
            System.out.println("Item successfully added.");
        else
            System.out.println("Incorrectly item data.");
    }

    private void remove(String line) {
        String[] parts = line.split("\\s");
        if (storage.remove(parts[1]))
            System.out.println("Item successfully removed.");
        else
            System.out.println("Item not found.");
    }

    private void findRegex(String regex) {
        List<ItemStack> res = storage.filter(regex.substring(0, regex.length() - 1));
        printListOfItemStack(res);
    }

    private void findStd(String name) {
        ItemStack res = storage.find(name);
        if (res == null) System.out.println("Item not found");
        else System.out.println(res);
    }

    private void find(String line) {
        String[] parts = line.split("\\s");
        if (parts[1].endsWith("*"))
            findRegex(parts[1]);
        else
            findStd(parts[1]);
    }

    private void all() {
        System.out.println("Items: ");
        List<ItemStack> res = storage.getData();
        printListOfItemStack(res);
    }

    private void save() {
        try {
            FileWriter writer = new FileWriter(config.getDataFilePath());
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (ItemStack is: storage.getData()) {
                bufferedWriter.write(is.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            writer.close();
            System.out.println("Data successfully saved.");
        } catch (IOException e) {
            System.out.println("Writing to file failed: " + config.getDataFilePath());
        }
    }

    private void help() {
        System.out.println("List of commands: ");
        System.out.println("add [name] [category] - add new item");
        System.out.println("remove [name] - remove item");
        System.out.println("find [name] - find item by name or part name (use *)");
        System.out.println("all - show all list of item");
        System.out.println("save - save changes");
        System.out.println("quit - close program");
    }

    public void exec() {
        if (!this.isInitialized) return;
        help();
        Scanner scan = new Scanner(System.in);
        while (true) {
            String line = scan.nextLine();
            if (line.equals("quit")) return;
            if (line.equals("help")) {
                help();
                continue;
            }
            if (line.startsWith("add")) {
                if (line.matches("add\\s.+\\s.+")) add(line);
                else System.out.println("Enter: add [name] [category]");
                continue;
            }
            if (line.startsWith("remove")) {
                if (line.matches("remove\\s.+")) remove(line);
                else System.out.println("Enter: remove [name]");
                continue;
            }
            if (line.startsWith("find")) {
                if (line.matches("find\\s.+")) find(line);
                else System.out.println("Enter: find [name] or [name*] for list");
                continue;
            }
            if (line.equals("all")) {
                all();
                continue;
            }
            if (line.equals("save")) {
                save();
                continue;
            }
            System.out.println("Unknown command");
        }
    }
}
