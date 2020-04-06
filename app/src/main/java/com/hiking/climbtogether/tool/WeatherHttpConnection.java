package com.hiking.climbtogether.tool;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherHttpConnection extends AsyncTask<String, Void, String> {

    private OnConnectionListener listener;

    public void setOnConnectionListener(OnConnectionListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String resultStr;
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder response;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setReadTimeout(15 * 1000);

            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append("\r");
            }
            resultStr = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = "Error : "+e.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return resultStr;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (!result.startsWith("Error")){
            listener.onSuccessful(result);
        }else {
            listener.onFailure(result);
        }
    }

    public interface OnConnectionListener {
        void onSuccessful(String result);

        void onFailure(String errorCode);
    }
}
