package org.imperial.application;

import java.math.BigDecimal;
import java.util.List;

import static org.imperial.config.DatabaseConfig.saveToDatabase;
import static org.imperial.properties.SystemConstant.FILE_PATH;
import static org.imperial.util.GetRequestUtil.getAccountBalance;
import static org.imperial.util.JsonParseUtil.parseBalance;
import static org.imperial.util.ReadCsvUtil.readAddressesFromCSV;

public class GetBalanceApplication {
    public static void main(String[] args) {
        List<String> addresses = readAddressesFromCSV(FILE_PATH);

        for (String address : addresses) {
            String response = getAccountBalance(address);

            // Get the balance in indicated address
            BigDecimal balance = parseBalance(response);
            if (balance != null) {
                saveToDatabase(address, balance);
            }
        }

//        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae"; // Just an example address
//        String response = getAccountBalance(address);
//
//        // Get the balance in indicated address
//        BigDecimal balance = parseBalance(response);
//        if (balance != null) {
//            saveToDatabase(address, balance);
//        }
    }
}
