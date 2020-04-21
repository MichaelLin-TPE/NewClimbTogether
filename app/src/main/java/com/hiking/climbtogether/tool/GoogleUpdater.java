package com.hiking.climbtogether.tool;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GoogleUpdater extends AsyncTask<Void,Void,String> {

    private OnCheckUpdateListener listener;

    public void setOnCheckUpdateListener(OnCheckUpdateListener listener){
        this.listener = listener;
    }
    @Override
    protected String doInBackground(Void... voids) {

        String lastVersion = "";
        String url = "https://play.google.com/store/apps/details?id=com.hiking.climbtogether";
        try{
            Document doc = Jsoup.connect(url).get();
            Element element = doc.getElementsByClass("BgcNfc").get(3);
            lastVersion = element.parent().children().get(1).children().text();

        }catch (Exception e){
            e.printStackTrace();
            lastVersion = "錯誤 : "+e.toString();
        }


        return lastVersion;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result.startsWith("錯誤")){
            listener.onFail(result);
        }else {
            listener.onSuccess(result);
        }
    }

    public interface OnCheckUpdateListener{
        void onSuccess(String result);
        void onFail(String errorCode);
    }
}
