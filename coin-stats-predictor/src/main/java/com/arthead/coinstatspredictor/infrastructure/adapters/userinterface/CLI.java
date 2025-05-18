package com.arthead.coinstatspredictor.infrastructure.adapters.userinterface;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CLI {
    private final String csvPath;
    private final CSVLoader loader = new CSVLoader();
    private final Scanner scanner = new Scanner(System.in);

    public CLI(String csvPath) {
        this.csvPath = csvPath;
    }

    public void run() {
        waitForCSV();
        try {
            loader.loadCSV(csvPath);
            startAutoReloadThread();
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            return;
        }
        BannerPrinter.printBanner();
        new CLIHandler(loader, scanner).startMenuLoop();
    }

    private void waitForCSV() {
        File file = new File(csvPath);
        while (!file.exists()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void startAutoReloadThread() {
        new Thread(() -> {
            try {
                Thread.sleep(60000);
                while (true) {
                    File f = new File(csvPath);
                    if (f.exists()) {
                        loader.loadCSV(csvPath);
                    }
                    Thread.sleep(60000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                System.err.println("Error actualizando CSV: " + e.getMessage());
            }
        }).start();
    }
}
