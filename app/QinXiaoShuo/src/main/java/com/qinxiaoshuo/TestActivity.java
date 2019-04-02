package com.qinxiaoshuo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2019/1/14
 */
public class TestActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final QinXs qd = new QinXs();
    new Thread(new Runnable() {
      @Override public void run() {
        //JSONObject chapterCatalogue =
        //    qd.getNovelInfoByUrl("http://www.qinxiaoshuo.com/book/%E5%A2%83%E7%95%8C%E7%BA%BF%E4%B8%8A%E7%9A%84%E5%9C%B0%E5%B9%B3%E7%BA%BF");
       // String request = qd.getChapterContent("https://www.qinxiaoshuo.com/book/%E5%A2%83%E7%95%8C%E7%BA%BF%E4%B8%8A%E7%9A%84%E5%9C%B0%E5%B9%B3%E7%BA%BF/#12").toString();
       // JSONObject request = qd.getNovelInfoByName("境界线上的地平线");
        //JSONArray result = qd.getRecommendNovels();
        //JSONArray result = qd.getNovelInfoByAuthor("川上稔");
         //JSONArray result = qd.getNavigateList();
        //JSONArray result =  qd.getNavigateItem("http://www.qinxiaoshuo.com/tag/%E5%90%8E%E5%AE%AB%E5%B0%8F%E8%AF%B4");
        JSONArray result = qd.getNavigateItem("http://www.qinxiaoshuo.com/tag/%E5%90%8E%E5%AE%AB%E5%B0%8F%E8%AF%B4",1);
        Log.e("<Novel>", "-->> : " + result.toString());
      }
    }).start();
  }

  public static void main(String args[]){
      Pattern pattern = Pattern.compile("\\d+");
      Matcher matcher = pattern.matcher("https://static.qinxiaoshuo.com/book/bookimg/901.jpg");
      while (matcher.find()) {
        String novelId= matcher.group(0);
        System.out.print(novelId);
      }
    try {
        Connection connect = Jsoup.connect("https://static.qinxiaoshuo.com/book/bookdata/901/14.txt").validateTLSCertificates(false);
        HttpConnection.Response response = (HttpConnection.Response) connect.execute();
        response.body();
    }catch (Exception e){

    }
  }
}
