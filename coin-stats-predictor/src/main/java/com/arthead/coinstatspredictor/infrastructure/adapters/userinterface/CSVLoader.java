package com.arthead.coinstatspredictor.infrastructure.adapters.userinterface;

import java.io.*;
import java.util.*;

public class CSVLoader {
    private final List<String[]> rows = new ArrayList<>();
    private String[] headers;

    public void loadCSV(String path) throws IOException {
        rows.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            headers = br.readLine().split(",");
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        }
    }

    public List<String[]> getRows() {
        return rows;
    }

    public int getColumnIndex(String name) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    public Set<String> getUniqueValues(String column) {
        int index = getColumnIndex(column);
        Set<String> values = new HashSet<>();
        for (String[] row : rows) {
            values.add(row[index].toLowerCase());
        }
        return values;
    }
}
