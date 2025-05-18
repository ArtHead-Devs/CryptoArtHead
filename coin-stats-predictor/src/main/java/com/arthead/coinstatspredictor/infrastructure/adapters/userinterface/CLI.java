package com.arthead.coinstatspredictor.infrastructure.adapters.userinterface;

import java.util.*;
import java.io.IOException;

public class CLI {

    private final String csvPath;
    private final CSVLoader loader = new CSVLoader();
    private final Scanner scanner = new Scanner(System.in);


    public CLI(String csvPath) {
        this.csvPath = csvPath;
    }

    public void run() {
        try {
            loader.loadCSV(csvPath);
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            return;
        }
        BannerPrinter.printBanner();
        menuLoop();
    }

    private void menuLoop() {
        boolean skipBlank = false;
            while (true) {
                if (skipBlank && scanner.hasNextLine()) {
                    scanner.nextLine();
                    skipBlank = false;
                }

            System.out.println("\nOptions:");
            System.out.println("  1. Show help (list coins, targets and models)");
            System.out.println("  2. Search results");
            System.out.println("  0. Exit");
            System.out.print("Select an option: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1" -> printHelp();
                case "2" -> {
                    TablePrinter.filterAndPrint(scanner, loader);
                    skipBlank = true;
                }
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void printHelp() {
        System.out.println("\n--- Help: possible values ---");
        for (String column : List.of("Coin", "Target", "Model")) {
            System.out.println("\nAvailable " + column + "s:");
            int idx = loader.getColumnIndex(column);
            loader.getRows().stream()
                    .map(row -> row[idx])
                    .distinct()
                    .forEach(value -> System.out.println(" - " + value));
        }
    }
}