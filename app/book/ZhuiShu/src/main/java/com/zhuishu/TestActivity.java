package com.zhuishu;

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
    final Zs qd = new Zs();
    new Thread(new Runnable() {
      @Override public void run() {
        JSONArray result = qd.getNovelInfoByAuthor("天蚕土豆");
        //JSONArray result = qd.getNavigateItem("http://www.zhuishushenqi.com/ranking/5645482405b052fe70aeb1b5?type=female");
        //JSONArray result = qd.getNavigateList();
        //JSONArray result = qd.getRecommendNovels();
        //String chapterContent = qd.getChapterContent(
        //    "http://www.zhuishushenqi.com/book/50864deb9dacd30e3a00001d/1.html").toString();
        //String novelInfoByNovelName = qd.getNovelInfoByName("武动乾坤").toString();
        Log.e("<Novel>", "-->> : " + result);
      }
    }).start();
  }
}
