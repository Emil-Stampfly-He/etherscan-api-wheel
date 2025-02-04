package org.imperial.properties;

public class EtherScanAPIConstant {
    public static final int MAXIMUM_NUM_IN_ONE_CALL = 10000;
    public static final String API_KEY = ""; // Change it
    public static final String BASE_URL = "https://api.etherscan.io/api";

    public static final String ACCOUNT_BALANCE_URL = "%s?module=account" +
            "&action=balance" +
            "&address=%s" +
            "&tag=latest" +
            "&apikey=%s";
    public static final String ACCOUNT_TRANSACTIONS_URL =
            "%s?module=account&action=txlist&address=%s&startblock=0&endblock=99999999&sort=asc&apikey=%s";

    // startblock will be dynamically changed
    public static final String DYNAMICAL_ACCOUNT_TRANSACTIONS_URL =
            "%s?module=account&action=txlist&address=%s&startblock=%d&endblock=99999999&sort=asc&apikey=%s";
}
