package com.example.climbtogether.tool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpConnection {


    public void startConnection(final String apiUrl, final String jsonStr, final OnPostNotificationListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                StringBuilder response = new StringBuilder();

                try {
                    URL url = new URL(apiUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Authorization","key=AAAAoBq2Y4E:APA91bESb9fIIjfak6Oufggjspojl-DaravquMxLvFaK3CFRbikmlFn-xW8s6KDweNxSXxZRYN0aOoAypj9tetihtktiVgHmxmPhvsLzBttDu06KalWHpIahXrDa_480HTRWJq2lYoGg");
                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setDoInput(true); //允許輸入流，即允許下載
                    conn.setDoOutput(true); //允許輸出流，即允許上傳
                    conn.setUseCaches(false); //設置是否使用緩存

                    OutputStream os = conn.getOutputStream();
                    DataOutputStream writer = new DataOutputStream(os);
                    writer.writeBytes(jsonStr);
                    writer.flush();
                    writer.close();
                    os.close();
                    //Get Response
                    InputStream is = conn.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    listener.onSuccessful(response.toString());
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    listener.onFail(ex.toString());
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();


    }


    public interface OnPostNotificationListener{
        void onSuccessful(String result);
        void onFail(String exception);
    }

}


