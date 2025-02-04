package org.imperial;

import org.junit.Test;

import static org.imperial.util.GetRequestUtil.getAllTransactions;

public class TestApplication {
//    @Test
//    public void testGetTransactionForSingleAddress() {
//        // Example address: Ethereum Fund Address
//        String exampleAddress = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
//
//        try {
//            String txResponse = getAccountTransactions(exampleAddress);
//            List<Transaction> transactions = parseTransactions(txResponse);
//
//            for (Transaction transaction : transactions) {
//                saveTransactionToDatabase(exampleAddress, transaction);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @Test
//    public void testGetTrasactionSize() {
//        // Address: 0x68b3465833fb72a70ecdf485e0e4c7bd8665fc45 (the first one)
//        String address = "0x68b3465833fb72a70ecdf485e0e4c7bd8665fc45";
//        String txResponse = getAccountTransactions(address);
//        List<Transaction> transactions = parseTransactions(txResponse);
//
//        int size = transactions.size();
//        assert size > 0;
//
//        System.out.println(size); // The output should be 10000
//    }

//    @Test
//    public void testGetAllTransactions() {
//        // Address: 0x68b3465833fb72a70ecdf485e0e4c7bd8665fc45 (the first one)
//        String address = "0x68b3465833fb72a70ecdf485e0e4c7bd8665fc45";
//        List<Transaction> allTransactions = getAllTransactions(address);
//
//        int size = allTransactions.size();
//        assert size > 0;
//
//        System.out.println(size); // Java heap out of memory
//    }

    @Test
    public void testGetAllTransactionsByManuallyManagingJVMMemory() {
        // Address: 0x000000fee13a103a10d593b9ae06b3e05f2e7e1c
        String address = "0x000000fee13a103a10d593b9ae06b3e05f2e7e1c";

        getAllTransactions(address); // Should get around 41642 entries
        System.out.printf("Transactions in %s restored successfully\n", address);
    }
}
