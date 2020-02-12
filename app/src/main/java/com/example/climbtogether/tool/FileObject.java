package com.example.climbtogether.tool;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileObject {


    public void downLoadFromUrl(final String urlStr, final String fileName, final String savePath , final OnDownLoadFileListener loadFileListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //設定超時間為3秒
                    conn.setConnectTimeout(3 * 1000);
                    //防止遮蔽程式抓取而返回403錯誤
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                    //得到輸入流
                    InputStream inputStream = conn.getInputStream();
                    //獲取自己陣列
                    byte[] getData = readInputStream(inputStream);

                    //檔案儲存位置
                    File saveDir = new File(savePath);
                    if (!saveDir.exists()) {
                        saveDir.mkdir();
                    }
                    File file = new File(saveDir + File.separator + fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(getData);

                    if (fos != null) {
                        fos.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    String message = "Download Success";
                    loadFileListener.onSuccess(message);

                    Log.i("Michael","下載成功");
                }catch (Exception e){
                    e.printStackTrace();
                    loadFileListener.onFaile(e.toString());
                }
            }
        }).start();


    }

    /**
     * 從輸入流中獲取位元組陣列
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 讀取本地檔案
     */
    public String readFile() {
        String path = "/data/data/com.example.climbtogether/download/nationPark.txt";
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        if (file.isDirectory()) {
            Log.e("TestFile", "The File doesn't not exist.");
            return "";
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    while ((line = buffreader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.e("TestFile", "The File doesn't not exist.");
                return "";
            } catch (IOException e) {
                Log.e("TestFile", e.getMessage());
                return "";
            }
        }
        return stringBuilder.toString();//對讀到的裝置ID解密
    }

    public interface OnDownLoadFileListener{
        void onSuccess(String message);
        void onFaile(String message);
    }
}
