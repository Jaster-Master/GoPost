package net.htlgkr.gopost.notification;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoNotification {

    public final static String AUTH_KEY_FCM = "AAAAYb-KbqE:APA91bFPNnHUlqI_OyMYc7FFDqHEKJv6_oJ0_wkHMrAeUUpXfnVJd5LuXVCSH0CAhqp-OSzguD5UeuHAMGIYXqPyun5_mZ1_c9hmcl88VdXvXNMnUPNzWeZ5at4Mt_au-rotpfg9mzlX";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static boolean sendNotification(long userId) {
        HttpURLConnection conn = getHttpConnection();
        if (conn == null) return false;
        JSONObject httpData = getJsonObject(userId, "Follower");
        sendJson(conn, httpData);
        handleResponse(conn);
        return true;
    }

    private static HttpURLConnection getHttpConnection() {
        try {
            URL url = new URL(API_URL_FCM);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
            conn.setRequestProperty("Content-Type", "application/json");
            return conn;
        } catch (IOException e) {
            return null;
        }
    }

    private static void sendJson(HttpURLConnection conn, JSONObject httpData) {
        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(httpData.toString());
            wr.flush();
            wr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleResponse(HttpURLConnection conn) {
        try {
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println(response);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONObject getJsonObject(long userId, String notificationType) {
        JSONObject message = new JSONObject();
        message.put("to", "/topics/GoPost");
        message.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", "Java");
        notification.put("body", "Notificação do Java");
        message.put("notification", notification);

        JSONObject data = new JSONObject();
        data.put("userId", userId);
        data.put("notificationType", notificationType);
        message.put("data", data);

        return message;
    }
}
