package com.example.climbtogether.tool;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

public class NationalParkWeatherAndNewsManager {

    private ArrayList<String> titleArrayList;
    private ArrayList<String> locationArrayList;
    private ArrayList<String> timeArrayList;


    private ArrayList<String> newsUrlArrayList;

    public void getDataFromHTML(final String weatherUrl, final OnWeatherListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<String> timeArray = new ArrayList<>();
                    ArrayList<String> imgUrlArray = new ArrayList<>();
                    ArrayList<String> tempArray = new ArrayList<>();
                    ArrayList<String> rainArray = new ArrayList<>();

                    URL url = new URL(weatherUrl);
                    Document doc = Jsoup.parse(url, 20000);
                    Elements title = doc.select("td[colspan]");
                    for (int i = 0; i < title.size(); i++) {
                        if (i >= 0 & i <= 6) {
                            Elements title_select = title.get(i).select("td[colspan]");
                            String name = title_select.get(0).text();
                            timeArray.add(name);
                        }
                    }
                    Elements img = doc.select("img[src]");
                    for (int i = 0; i < img.size(); i++) {
                        if (i == 0 || (i % 2 == 0 & i >= 0 & i <= 13)) {
                            Elements img_select = img.get(i).select("img[src]");
                            String imgurl = img_select.attr("abs:src");
                            imgUrlArray.add(imgurl);
                        }
                    }
                    Elements maxtemp = doc.select("td");
                    for (int i = 0; i < maxtemp.size(); i++) {
                        if ((i % 2 == 1 & i >= 39 & i <= 51)) {
                            Elements temp_select = maxtemp.get(i).select("td");
                            String temp = temp_select.get(0).text();
                            tempArray.add(temp);

                        }
                        if ((i % 2 == 0 & i >= 144 & i <= 156)) {
                            Elements temp_select = maxtemp.get(i).select("td");
                            String rain = temp_select.get(0).text();
                            rainArray.add(rain);

                        }
                    }
                    Thread.sleep(100);

                    listener.onSuccess(timeArray,tempArray,rainArray,imgUrlArray);


                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFail("取得失敗"+e.toString());
                }
            }
        }).start();
    }

    public void getNewsData(final OnNewsListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url;
                    titleArrayList = new ArrayList<>();
                    locationArrayList = new ArrayList<>();
                    timeArrayList = new ArrayList<>();
                    newsUrlArrayList = new ArrayList<>();
                    url = new URL("https://npm.cpami.gov.tw/news_1.aspx?unit=c951cdcd-b75a-46b9-8002-8ef952ec95fd");
                    Document doc = Jsoup.parse(url, 3000);
                    Elements title = doc.select("td");
                    for (int i = 0; i < 30; i++) {            //用FOR個別抓取選定的Tag內容
                        Elements title_select = title.get(i).select("td");//選擇第i個後選取所有為td的Tag

                        if (i == 0 || i == 3 || i == 6 || i == 9 || i == 12 || i == 15 || i == 18 || i == 21 || i == 24 || i == 27) {
                            locationArrayList.add(title_select.get(0).text());
                        } else if (i == 1 || i == 4 || i == 7 || i == 10 || i == 13 || i == 16 || i == 19 || i == 22 || i == 25 || i == 28) {
                            timeArrayList.add(title_select.get(0).text());
                        } else {
                            titleArrayList.add(title_select.get(0).text());
                        }
                        //避免執行緒跑太快而UI執行續顯示太慢,覆蓋掉te01~03內容所以設個延遲,也可以使用AsyncTask-異步任務
                    }
                    Elements news=doc.select("a[href]");
                    for(int i=0;i<news.size();i++){
                        if(i>=27 & i<=36){
                            Elements title_select=news.get(i).select("a[href]");
                            newsUrlArrayList.add(title_select.attr("abs:href"));
                        }
                    }

                    listener.onSuccess(titleArrayList,locationArrayList,timeArrayList,newsUrlArrayList);

                } catch (Exception e) {
                    e.printStackTrace();
                    String message = "news取得失敗 : "+e.toString();
                    Log.i("Michael",message);
                    listener.onFail(message);
                }

            }
        }).start();


    }



    public interface OnWeatherListener{
        void onSuccess(ArrayList<String> timeArray , ArrayList<String> tempArray , ArrayList<String> rainArray , ArrayList<String> imgUrlArray);
        void onFail(String message);
    }

    public interface  OnNewsListener{
        void onSuccess(ArrayList<String> titleArrayList,
                ArrayList<String> locationArrayList,
                ArrayList<String> timeArrayList,
                ArrayList<String> newsUrlArrayList);
        void onFail(String message);
    }


}
