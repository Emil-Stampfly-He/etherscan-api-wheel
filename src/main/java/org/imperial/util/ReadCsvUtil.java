package org.imperial.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ReadCsvUtil {
    public static List<String> readAddressesFromCSV(String filePath) {
        List<String> addresses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // No need to skip the first line
            while ((line = br.readLine()) != null) {
                String address = line.trim();
                if (!address.isEmpty()) {
                    addresses.add(address);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to read csv file: ");
            e.printStackTrace();
        }

        return addresses;
    }
}
