package org.imperial.pojo;

import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class Transaction {
    private String hash;
    private String fromAddress;
    private String toAddress;
    private BigDecimal valueEth;
    private LocalDateTime timestamp;
    private String blockNumber;
    private String nonce;
    private String blockHash;
    private long transactionIndex;
    private BigInteger gas;
    private BigInteger gasPrice;
    private long isError;
    private long txReceiptStatus;
    private String contractAddress;
    private BigInteger cumulativeGasUsed;
    private BigInteger gasUsed;
    private long confirmations;
    private String methodId;
    private String functionName;

    public Transaction(JSONObject tx) throws JSONException {
        this.hash = tx.getString("hash");
        this.blockNumber = tx.getString("blockNumber");
        this.nonce = tx.getString("nonce");
        this.blockHash = tx.getString("blockHash");
        this.transactionIndex = Integer.parseInt(tx.getString("transactionIndex"));
        this.fromAddress = tx.getString("from");
        this.toAddress = tx.optString("to", null); // Might be null

        this.valueEth = new BigDecimal(tx.getString("value"))
                .divide(new BigDecimal("1e18"), 8, RoundingMode.HALF_UP);
        this.gas = new BigInteger(tx.getString("gas"));
        this.gasPrice = new BigInteger(tx.getString("gasPrice"));
        this.cumulativeGasUsed = new BigInteger(tx.getString("cumulativeGasUsed"));
        this.gasUsed = new BigInteger(tx.getString("gasUsed"));

        this.isError = Integer.parseInt(tx.getString("isError"));
        this.txReceiptStatus = tx.optInt("txreceipt_status", 1);

        long timestampSeconds = Long.parseLong(tx.getString("timeStamp"));
        this.timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestampSeconds),
                ZoneId.systemDefault()
        );

        this.contractAddress = tx.optString("contractAddress", null);
        this.confirmations = Integer.parseInt(tx.getString("confirmations"));
        this.methodId = tx.getString("methodId"); // New helper method needed
    }

    // Helper method: parse methodId from input
    private String parseMethodId(String input) {
        return (input != null && input.length() >= 10) ? input.substring(0, 10) : "";
    }
}
