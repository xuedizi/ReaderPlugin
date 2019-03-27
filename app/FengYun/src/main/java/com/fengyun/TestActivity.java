package com.fengyun;

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
    final Fy qd = new Fy();
    new Thread(new Runnable() {
      @Override public void run() {
        //JSONArray result = qd.getNovelInfoByAuthor("天蚕土豆");
        JSONArray result = qd.getNavigateItem("https://www.fengyunla.com/sort_1_1.html");
        //JSONArray result = qd.getNavigateList();
        //JSONArray result = qd.getRecommendNovels();
        //String s = qd.getChapterContent("http://www.fengyunok.com/142484/48752087.html").toString();
        //String novelInfoByNovelName = qd.getNovelInfoByName("无疆").toString();
        Log.e("<Novel>", "-->> : " + result.toString());
      }
    }).start();
  }
}
