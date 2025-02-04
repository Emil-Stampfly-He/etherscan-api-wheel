package org.imperial.application;

import java.util.List;

import static org.imperial.properties.SystemConstant.FILE_PATH;
import static org.imperial.util.GetRequestUtil.getAllTransactions;
import static org.imperial.util.ReadCsvUtil.readAddressesFromCSV;

public class GetTransactionsApplication {
    public static void main(String[] args) {
        List<String> addresses = readAddressesFromCSV(FILE_PATH);

        for (String address : addresses) {
            try {
                getAllTransactions(address);

                // Force the process of GC
                System.gc();

                System.out.printf("Transactions in %s restored successfully\n", address);
            } catch (Exception e) {
                System.err.println("Failed: " + address);
                e.printStackTrace();
            }
        }
    }
}
