package org.imperial.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.imperial.pojo.Transaction;
import org.imperial.properties.EtherScanAPIConstant;

import java.util.List;

import static org.imperial.config.DatabaseConfig.saveBatchTransactionsToDatabase;
import static org.imperial.util.JsonParseUtil.parseTransactions;

public class GetRequestUtil {
    // Send HTTP GET request
    private static String sendGetRequest(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get account balance
    public static String getAccountBalance(String address) {
        String url = String.format(EtherScanAPIConstant.ACCOUNT_BALANCE_URL,
                EtherScanAPIConstant.BASE_URL, address, EtherScanAPIConstant.API_KEY);

        return sendGetRequest(url);
    }

//    // Get account transactions with the number limitation of 10000 of one time GET
//    public static String getAccountTransactions(String address) {
//        String url = String.format(
//                EtherScanAPIConstant.ACCOUNT_TRANSACTIONS_URL,
//                EtherScanAPIConstant.BASE_URL,
//                address,
//                EtherScanAPIConstant.API_KEY);
//
//        return sendGetRequest(url);
//    }

    // Get all transactions with no limitation
    // Manually manage JVM memory to prevent OOM exception
    public static void getAllTransactions(String address) {
        long startBlock = 0; // start from block 0
        int batchIndex = 0;
        int batchSize;

        // If the batch size >= 10000, keep adding batches to the database
        do {
            // 1. Get current batch of transactions
            String url = String.format(
                    EtherScanAPIConstant.DYNAMICAL_ACCOUNT_TRANSACTIONS_URL,
                    EtherScanAPIConstant.BASE_URL,
                    address,
                    startBlock,
                    EtherScanAPIConstant.API_KEY
            );

            String response = sendGetRequest(url);
            List<Transaction> batch = parseTransactions(response);

            // 2. Save to database asap
            saveBatchTransactionsToDatabase(address, batch);

            if (!batch.isEmpty()) {
                // 3. Update the number of start block
                startBlock = Long.parseLong(batch.get(batch.size() - 1).getBlockNumber()) + 1;
            }

            batchSize = batch.size();

            // Control JVM memory
            batch.clear();

            // Avoid breaking API calling limitation per second
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("Current address: %s, batch %d transactions have been saved successfully.\n", address, batchIndex);
            batchIndex++;
        } while (batchSize >= EtherScanAPIConstant.MAXIMUM_NUM_IN_ONE_CALL);
    }
}
