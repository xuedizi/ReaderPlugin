package com.biantu;

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
    final Bat bat = new Bat();
    new Thread(new Runnable() {
      @Override public void run() {
        JSONArray result = bat.getNavigateItem("http://pic.netbian.com/4kmeinv/",0);
        //JSONArray result = bat.getNavigateList();
        //JSONArray result = bat.getRecommendPictures();
        Log.e("<Picture>", "-->> : " + result.toString());
      }
    }).start();
  }
}
