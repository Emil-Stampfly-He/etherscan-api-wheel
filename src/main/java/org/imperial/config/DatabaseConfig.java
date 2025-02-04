package org.imperial.config;

import org.imperial.pojo.Transaction;
import org.imperial.properties.DatabaseConstant;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

public class DatabaseConfig {
    // Set parameters
    private static void setParameters(String address, PreparedStatement pstmt, Transaction tx) throws SQLException {
        pstmt.setString(1, address);
        pstmt.setString(2, tx.getBlockNumber());
        pstmt.setString(3, String.valueOf(tx.getTimestamp().atZone(ZoneId.systemDefault()).toEpochSecond()));
        pstmt.setString(4, tx.getHash());
        pstmt.setString(5, tx.getNonce());
        pstmt.setString(6, tx.getBlockHash());
        pstmt.setLong(7, tx.getTransactionIndex());
        pstmt.setString(8, tx.getFromAddress());
        pstmt.setString(9, tx.getToAddress()); // Might be null
        pstmt.setDouble(10, tx.getValueEth().doubleValue());
        pstmt.setLong(11, tx.getGas().longValueExact());
        pstmt.setLong(12, tx.getGasPrice().longValueExact());
        pstmt.setLong(13, tx.getIsError());
        pstmt.setLong(14, tx.getTxReceiptStatus());
        pstmt.setString(15, tx.getContractAddress()); // Might be null
        pstmt.setInt(16, tx.getCumulativeGasUsed().intValueExact());
        pstmt.setInt(17, tx.getGasUsed().intValueExact());
        pstmt.setLong(18, tx.getConfirmations());
        pstmt.setString(19, tx.getMethodId());
        pstmt.setString(20, tx.getFunctionName());
    }

    public static void saveToDatabase(String address, BigDecimal balance) {
        String sql = "INSERT INTO account_balance (address, balance_eth) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(
                DatabaseConstant.DATABASE_URL,
                DatabaseConstant.DATABASE_USER,
                DatabaseConstant.DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, address);
            preparedStatement.setBigDecimal(2, balance);
            preparedStatement.executeUpdate();

            System.out.println("Data has been saved to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void saveTransactionToDatabase(String address, Transaction tx) {
//        String sql = "INSERT INTO account_transaction (" +
//                "address, block_number, time_stamp, hash, nonce, block_hash, " +
//                "transaction_index, from_address, to_address, value, gas, gas_price, " +
//                "is_error, txreceipt_status,  contract_address, cumulative_gas_used, " +
//                "gas_used, confirmations, method_id, function_name" +
//                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        try (Connection conn = DriverManager.getConnection(
//                DatabaseConstant.DATABASE_URL,
//                DatabaseConstant.DATABASE_USER,
//                DatabaseConstant.DATABASE_PASSWORD);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            // Set parameters
//            setParameters(address, pstmt, tx);
//
//            pstmt.executeUpdate();
//            System.out.println("Transaction " + tx.getHash() + " is saved successfully.");
//
//        } catch (SQLException e) {
//            System.err.println("Failed to save transaction: ");
//            e.printStackTrace();
//        }
//    }

    public static void saveBatchTransactionsToDatabase(String address, List<Transaction> batch) {
        String sql = "INSERT INTO account_transaction_without_limitation (" +
                "address, block_number, time_stamp, hash, nonce, block_hash, " +
                "transaction_index, from_address, to_address, value, gas, gas_price, " +
                "is_error, txreceipt_status,  contract_address, cumulative_gas_used, " +
                "gas_used, confirmations, method_id, function_name" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConstant.DATABASE_URL,
                DatabaseConstant.DATABASE_USER,
                DatabaseConstant.DATABASE_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Close auto commit, get higher efficiency
            conn.setAutoCommit(false);

            for (Transaction tx : batch) {
                // Set parameters
                setParameters(address, pstmt, tx);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Failed to save transaction: ");
            e.printStackTrace();
        }
    }
}
