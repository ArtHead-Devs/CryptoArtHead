package com.arthead.coinstatspredictor.infrastructure.adapters.userinterface;

import java.util.List;
import java.util.Scanner;

public class CLIHandler {
    private final CSVLoader loader;
    private final Scanner scanner;

    public CLIHandler(CSVLoader loader, Scanner scanner) {
        this.loader = loader;
        this.scanner = scanner;
    }

    public void startMenuLoop() {
        while (true) {
            showOptions();
            String option = scanner.nextLine().trim();
            handleOption(option);
        }
    }

    private void showOptions() {
        System.out.println("\nOptions:");
        System.out.println("  1. Show help (list coins, targets and models)");
        System.out.println("  2. Search results");
        System.out.println("  0. Exit");
        System.out.print("Select an option: ");
    }

    private void handleOption(String option) {
        switch (option) {
            case "1" -> printHelp();
            case "2" -> TablePrinter.filterAndPrint(scanner, loader);
            case "0" -> exitProgram();
            default -> System.out.println("Invalid option.");
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

    private void exitProgram() {
        System.out.println("Exiting...");
        System.exit(0);
    }
}
