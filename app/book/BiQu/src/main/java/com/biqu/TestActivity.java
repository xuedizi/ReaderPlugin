package com.biqu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;

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
    final Bq qd = new Bq();
    new Thread(new Runnable() {
      @Override public void run() {
        //JSONArray result = qd.getNavigateItem("http://www.biquyun.com/quanben/");
        //JSONArray result = qd.getNavigateList();
        JSONArray result = qd.getRecommendNovels();
        //JSONObject chapterCatalogue =
        //    qd.getNovelInfoByUrl("http://www.biquyun.com/16_16634/");
        //String s = qd.getChapterContent("http://www.biquyun.com/19_19621/9255008.html").toString();
        //JSONObject novelInfoByNovelName = qd.getNovelInfoByName("明朝败家子");
        Log.e("<Novel>", "-->> : " + result.toString());
      }
    }).start();
  }
}
