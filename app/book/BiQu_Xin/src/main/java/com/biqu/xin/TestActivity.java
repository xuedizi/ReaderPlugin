package com.biqu.xin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

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
    final Bq_Xin qd = new Bq_Xin();
    new Thread(new Runnable() {
      @Override public void run() {
        //JSONArray navigateList = qd.getNovelInfoByAuthor("柳下挥");
        //JSONArray navigateList = qd.getRecommendNovels();
        //JSONArray result = qd.getNavigateList();
        JSONArray result = qd.getNavigateItem("https://www.xbiquge6.com/xclass/2/1.html");
        //JSONObject chapterCatalogue =
        ///    qd.getNovelInfoByUrl("https://www.xbiquge6.com/20_20331/");
         //String result = qd.getChapterContent("http://www.xbiquge6.com/20_20331/1135933.html").toString();
        //JSONObject result = qd.getNovelInfoByName("万古神帝");
         Log.e("<Novel>", "--j>> : " + result.toString());
      }
    }).start();
  }
}
