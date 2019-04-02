package com.fanqie;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2019/1/15
 */
public class TestActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Fq qd = new Fq();
    new Thread(new Runnable() {
      @Override public void run() {
        JSONArray navigateList = qd.getNovelInfoByAuthor("天蚕土豆");
        //JSONArray navigateList = qd.getRecommendNovels();
        //JSONArray navigateList = qd.getNavigateList();
        //JSONArray navigateList = qd.getNavigateItem("http://www.fqxs.org/qita/");
        //String chapterContent =
        //    qd.getChapterContent("http://www.fqxs.org/book//html/88804/23531980.html").toString();
        //String py = qd.toHanyuPinyin("无疆");
        //Log.e("<Fq>", py);
        //String novel = qd.getNovelInfoByName("无疆").toString();
        Log.e("<Fq>", navigateList.toString());
      }
    }).start();
  }
}
