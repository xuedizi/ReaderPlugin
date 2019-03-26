package com.qidian;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

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
    final Qd qd = new Qd();
    new Thread(new Runnable() {
      @Override public void run() {
        //JSONArray result = qd.getNovelInfoByAuthor("老鹰吃小鸡");
        //JSONArray result = qd.getNavigateItem("http://www.qidian.com/rank/signnewbook?style=1");
        //JSONArray result = qd.getNavigateList();
        //JSONArray result = qd.getRecommendNovels();
        //String novelInfoByUrl =
        //    qd.getNovelInfoByUrl("https://book.qidian.com/info/1013573542").toString();
        String result = qd.getNovelInfoByName("全球高武").toString();
        //Log.e("<Novel>", "-->> : " + novelInfoByNovelName);
        //JSONObject chapterContent = qd.getChapterContent(
        //    "https://read.qidian.com/chapter/ZRxfg5emqbE1/BRfIfMsWlcUex0RJOkJclQ2");
        Log.e("<Novel>", "-->> : " + result);
      }
    }).start();
  }
}
