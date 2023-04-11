package com.paytm.integration.paytmintegration;

import com.paytm.pg.merchant.PaytmChecksum;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

public class UPIWithoutPaytmAccount {
    public static void main(String[] args) throws Exception {
        Random random = new Random();
        JSONObject paytmParams = new JSONObject();
        String orderId = "ORDER" + random.nextInt(10000000);
        JSONObject body = new JSONObject();
        body.put("requestType", "Payment");
        body.put("mid", "JBEzpy42288253468787");
//        body.put("key","fO2vwcNCUf9PJTsS");
        body.put("websiteName", "DEFAULT");
        body.put("orderId", orderId);
        body.put("callbackUrl", "localhost");

        JSONObject txnAmount = new JSONObject();
        txnAmount.put("value", "1.00");
        txnAmount.put("currency", "INR");

        JSONObject userInfo = new JSONObject();
        userInfo.put("custId", "CUST_001");
        body.put("txnAmount", txnAmount);
        body.put("userInfo", userInfo);

        /*
         * Generate checksum by parameters we have in body
         * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */


        String checksum = PaytmChecksum.generateSignature(body.toString(), "fO2vwcNCUf9PJTsS");

        JSONObject head = new JSONObject();
        head.put("signature", checksum);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
//        URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=JBEzpy42288253468787&orderId=ORDERID_98928");

        /* for Production */
        URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=JBEzpy42288253468787&orderId=" + orderId);

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("\nInit transaction Response: " + responseData);
            }
            responseReader.close();

            //read the txnToken
            JSONObject responseJSON = new JSONObject(responseData);
            JSONObject responseBody = new JSONObject(responseJSON.get("body").toString());
            String txnToken = responseBody.get("txnToken").toString();

            // validate VPA api
            paytmParams = new JSONObject();

            body = new JSONObject();
            body.put("vpa", "rutujaadsul15@okicici");

            head = new JSONObject();
            head.put("tokenType", "TXN_TOKEN");
            head.put("txnToken", txnToken);

            paytmParams.put("body", body);
            paytmParams.put("head", head);

            post_data = paytmParams.toString();

            /* for Staging */
//            url = new URL("https://securegw-stage.paytm.in/theia/api/v1/vpa/validate?mid=JBEzpy42288253468787&orderId=ORDERID_98928");

            /* for Production */
            url = new URL("https://securegw.paytm.in/theia/api/v1/vpa/validate?mid=JBEzpy42288253468787&orderId=" + orderId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            responseData = "";
            is = connection.getInputStream();
            responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("\nValidate VPA API Response: " + responseData);
            }
            responseReader.close();


            // process transaction
            paytmParams = new JSONObject();

            body = new JSONObject();
            body.put("requestType", "NATIVE");
            body.put("mid", "JBEzpy42288253468787");
            body.put("orderId", orderId);
            body.put("paymentMode", "UPI");
            body.put("channelCode", "collect");
            body.put("payerAccount", "rutujaadsul15@okicici");

            head = new JSONObject();
            head.put("txnToken", txnToken);

            paytmParams.put("body", body);
            paytmParams.put("head", head);

            post_data = paytmParams.toString();


            /* for Staging */
//            url = new URL("https://securegw-stage.paytm.in/theia/api/v1/processTransaction?mid=JBEzpy42288253468787&orderId=ORDERID_98928");

            /* for Production */
            url = new URL("https://securegw.paytm.in/theia/api/v1/processTransaction?mid=JBEzpy42288253468787&orderId=" + orderId);


            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            responseData = "";
            is = connection.getInputStream();
            responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("\nProcess transaction Response: " + responseData);
            }
            responseReader.close();


            System.out.print("\nEnter any key-");
            Scanner sc = new Scanner(System.in);
            sc.nextLine();

            // validate transaction status
            /* initialize an object */
            paytmParams = new JSONObject();

            /* body parameters */
            body = new JSONObject();

            /* Find your MID in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys */
            body.put("mid", "JBEzpy42288253468787");

            /* Enter your order id which needs to be check status for */
            body.put("orderId", orderId
            );

/**
 * Generate checksum by parameters we have in body
 * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
 * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
 */
            checksum = PaytmChecksum.generateSignature(body.toString(), "fO2vwcNCUf9PJTsS");
            /* head parameters */
            head = new JSONObject();

            /* put generated checksum value here */
            head.put("signature", checksum);

            /* prepare JSON string for request */
            paytmParams.put("body", body);
            paytmParams.put("head", head);
            post_data = paytmParams.toString();

            /* for Staging */
//             url = new URL("https://securegw-stage.paytm.in/v3/order/status");

            /* for Production */
            url = new URL("https://securegw.paytm.in/v3/order/status");


            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            responseData = "";
            is = connection.getInputStream();
            responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {

                System.out.append("\nTransaction Status Response: " + responseData);

            }
            // System.out.append("Request: " + post_data);
            responseReader.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
