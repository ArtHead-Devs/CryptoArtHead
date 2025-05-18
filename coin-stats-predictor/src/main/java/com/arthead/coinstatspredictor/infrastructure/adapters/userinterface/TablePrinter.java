package com.arthead.coinstatspredictor.infrastructure.adapters.userinterface;

import java.util.*;

public class TablePrinter {

    public static void filterAndPrint(Scanner scanner, CSVLoader loader) {
        Set<String> cryptos = askAndValidate(scanner, "Coin", loader);
        Set<String> targets = askAndValidate(scanner, "Target", loader);
        Set<String> models  = askAndValidate(scanner, "Model",  loader);

        Map<String, List<String[]>> grouped = new TreeMap<>();
        for (String[] row : loader.getRows()) {
            String crypto = row[loader.getColumnIndex("Coin")].toLowerCase();
            String target = row[loader.getColumnIndex("Target")].toLowerCase();
            String model  = row[loader.getColumnIndex("Model")].toLowerCase();
            boolean match = (cryptos.isEmpty() || cryptos.contains(crypto)) &&
                    (targets.isEmpty() || targets.contains(target)) &&
                    (models.isEmpty() || models.contains(model));
            if (match) {
                grouped.computeIfAbsent(crypto, k -> new ArrayList<>()).add(row);
            }
        }

        if (grouped.isEmpty()) {
            System.out.println("No results found for the given criteria.");
            return;
        }

        String line   = "+---------------------------------+---------------------------+----------+------------+------------+--------------+------------+------------+";
        String format = "| %-31s | %-25s | %8s | %10s | %10s | %12s | %10s | %10s |%n";

        for (String crypto : grouped.keySet()) {
            System.out.println("\nCOIN: " + crypto.toUpperCase());
            System.out.println(line);
            System.out.format(format, "Target", "Model", "R²", "MSE", "MAE", "Prediction", "Std Dev", "Variance");
            System.out.println(line);
            for (String[] row : grouped.get(crypto)) {
                System.out.format(format,
                        row[loader.getColumnIndex("target")],
                        row[loader.getColumnIndex("model")],
                        row[loader.getColumnIndex("R2")],
                        row[loader.getColumnIndex("MSE")],
                        row[loader.getColumnIndex("MAE")],
                        row[loader.getColumnIndex("mean")],
                        row[loader.getColumnIndex("std")],
                        row[loader.getColumnIndex("var")]
                );
            }
            System.out.println(line);
        }

        waitForEnter(scanner);
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.print("\n... Press Enter to return to menu ...\n");
        scanner.nextLine();
    }

    private static Set<String> askAndValidate(Scanner scanner, String column, CSVLoader loader) {
        Set<String> valid = loader.getUniqueValues(column);
        Set<String> result = new HashSet<>();

        while (true) {
            System.out.print(column + " (comma separated, empty for all): ");
            String input = scanner.nextLine().trim();
            if (input.isBlank()) return Collections.emptySet();

            String[] items = input.split(",");
            boolean allValid = true;
            for (String item : items) {
                String cleaned = item.trim().toLowerCase();
                if (valid.contains(cleaned)) result.add(cleaned);
                else {
                    System.out.println("Invalid value: " + item);
                    allValid = false;
                }
            }
            if (allValid) break;
            result.clear();
        }
        return result;
    }
}
