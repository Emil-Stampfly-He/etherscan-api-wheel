package org.imperial.util;

import org.imperial.pojo.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class JsonParseUtil {
    // Parse JSON response from account balance
    public static BigDecimal parseBalance(String jsonResponse) {
        if (jsonResponse == null) {
            System.out.println("Request failed");
            return null;
        }

        JSONObject json = new JSONObject(jsonResponse);
        if (json.getString("status").equals("1")) {
            String balanceWei = json.getString("result");

            // wei to ETH (1 ETH = 10^18 Wei)
            BigDecimal wei = new BigDecimal(balanceWei);
            BigDecimal eth = wei.divide(new BigDecimal("1e18"), RoundingMode.HALF_UP)
                    .round(MathContext.DECIMAL32);

            System.out.printf("Balance: %.18f ETH\n", eth);
            return eth;
        } else {
            System.out.println("Error: " + json.getString("message"));
            return null;
        }
    }

    public static List<Transaction> parseTransactions(String jsonResponse) {
        List<Transaction> transactions = new ArrayList<>();
        if (jsonResponse == null) {
            System.out.println("No API response");
            return transactions;
        }

        try {
            JSONObject json = new JSONObject(jsonResponse);
            if (!"1".equals(json.getString("status"))) {
                System.err.println("API error: " + json.optString("message", "unknown error"));
                return transactions;
            }

            JSONArray results = json.getJSONArray("result");
            for (int i = 0; i < results.length(); i++) {
                JSONObject tx = results.getJSONObject(i);
                try {
                    Transaction transaction = new Transaction(tx);
                    transactions.add(transaction);
                } catch (Exception e) {
                    System.err.println("Parse the transaction[" + tx.optString("hash", "unknown hash") + "]failed: " + e.getMessage());
                }
            }
        } catch (JSONException e) {
            System.err.println("JSON parse error: " + e.getMessage());
        }
        return transactions;
    }
}
