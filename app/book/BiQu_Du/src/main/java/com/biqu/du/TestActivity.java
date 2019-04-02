package com.biqu.du;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
    final Bq_Du qd = new Bq_Du();
    new Thread(new Runnable() {
      @Override public void run() {
        //JSONArray navigateList = qd.getNovelInfoByAuthor("宅猪");
        //JSONArray navigateList = qd.getRecommendNovels();
        //JSONArray navigateList = qd.getNavigateList();
        //JSONArray navigateList = qd.getNavigateItem("http://www.biqudu.com/xuanhuanxiaoshuo/");
        //JSONObject chapterCatalogue =
        //    qd.getNovelInfoByUrl("http://www.biqudu.com/110_110591/");
        //String s = qd.getChapterContent("http://www.biqudu.com/110_110591/5735167.html").toString();
        JSONObject navigateList = qd.getNovelInfoByName("琅琊榜");
        Log.e("<Novel>", "-->> : " + navigateList.toString());
      }
    }).start();
  }
}
