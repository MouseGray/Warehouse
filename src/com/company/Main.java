package com.company;

public class Main {
    static final String configFileName = "settings.cfg";

    public static void main(String[] args) {
        Config cfg = new Config();
        cfg.load(configFileName);

        Application app = new Application(cfg);
        app.load();
        app.exec();
    }
}
