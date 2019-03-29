package com.biqu.xin;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.reader.support.ISupport;
import com.reader.support.K;
import com.reader.support.XpathHelper;
import com.reader.utils.DataUtils;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/17
 */
public class Bq_Xin implements ISupport {

  @Override public JSONObject getNovelInfoByUrl(String url) {
    Document dom = XpathHelper.getDom(url);
    return getNovelInfo(dom, url);
  }

  public JSONObject getNovelInfo(Document dom, String url) {
    JSONObject resultObject = new JSONObject();
    List<String> chaptersTitle = XpathHelper.getN(dom, Bq_XinXpathUri.Xpath_chapter_list);
    List<String> chaptersUrl = XpathHelper.getN(dom, Bq_XinXpathUri.Xpath_chapter_url);
    if (null == chaptersTitle || null == chaptersUrl) {
      return null;
    }
    String novelName = XpathHelper.get(dom, Bq_XinXpathUri.Xpath_novel_name);
    //获取作者名称
    String novelAuthor = null;
    List<String> ps = XpathHelper.getN(dom, Bq_XinXpathUri.Xpath_novel_author);
    if (null != ps && !ps.isEmpty() && ps.get(0).split("：").length > 1) {
      novelAuthor = ps.get(0).split("：")[1];
    }
    //获取最后更新时间
    String lastModifiedStr = XpathHelper.get(dom, Bq_XinXpathUri.Xpath_novel_last_modified);
    long lastModified = getLastModified(lastModifiedStr);
    String novelLogo = XpathHelper.get(dom, Bq_XinXpathUri.Xpath_novel_logo);
    String novelIntroduction = XpathHelper.get(dom, Bq_XinXpathUri.Xpath_novel_introduction);
    JSONArray chapterCatalogues = new JSONArray();
    int len = chaptersTitle.size() > chaptersUrl.size() ? chaptersUrl.size() : chaptersTitle.size();
    for (int index = 0; index < len; index++) {
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put(K.Catalogue_Path, Bq_XinUrl.Url_Base + chaptersUrl.get(index));
        jsonObject.put(K.Catalogue_Title, chaptersTitle.get(index));
        chapterCatalogues.put(jsonObject);
      } catch (JSONException e) {
        Log.e("<Reader>", e.getMessage());
      }
    }
    //获取最新章节
    String newestChapter = XpathHelper.get(dom, Bq_XinXpathUri.Xpath_novel_last_chapter);
    try {
      if (TextUtils.isEmpty(newestChapter)) {
        newestChapter =
            chapterCatalogues.getJSONObject(chapterCatalogues.length() - 1)
                .optString(K.Catalogue_Title);
      }
    } catch (JSONException e) {
    }
    try {
      resultObject.put(K.Novel_Path, url);
      resultObject.put(K.Novel_Name, novelName);
      resultObject.put(K.Novel_Catalogue, chapterCatalogues);
      resultObject.put(K.Novel_Author, novelAuthor);
      resultObject.put(K.Novel_last_chapter, newestChapter);
      resultObject.put(K.Novel_logo, novelLogo);
      resultObject.put(K.Novel_intro, novelIntroduction);
      resultObject.put(K.Novel_last_modified, lastModified);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return resultObject;
  }

  private long getLastModified(String lastModifiedStr) {
    if (!TextUtils.isEmpty(lastModifiedStr)) {
      if (lastModifiedStr.contains("：")) {
        String[] split = lastModifiedStr.split("：");
        if (split.length > 1) {
          return DataUtils.getString2Date(split[1], "yyyy-MM-dd");
        }
      } else {
        return DataUtils.getString2Date(lastModifiedStr, "yyyy-MM-dd");
      }
    }
    return 0;
  }

  @Override public JSONObject getNovelInfoByName(String novelname) {
    String novelnameEdcode = novelname;
    //try {
    //  novelnameEdcode  = URLEncoder.encode(novelname, "GBK");
    //} catch (UnsupportedEncodingException e) {
    //  e.printStackTrace();
    //}

    Document dom = XpathHelper.getDom(String.format(Bq_XinUrl.Url_Search, novelnameEdcode));
    String url = null;
    //判断是搜索列表页或小说结果页
    List<String> titles = XpathHelper.getN(dom, Bq_XinXpathUri.Xpath_Novel_Search_Title_List);
    List<String> urls = XpathHelper.getN(dom, Bq_XinXpathUri.Xpath_Novel_Search_Url_List);

    if (null != titles && !titles.isEmpty()) {
      //搜索结果列表类型
      if (titles.contains(novelname)) {
        if (null != urls) {
          url = urls.get(titles.indexOf(novelname));
          dom = XpathHelper.getDom(url);
        }
      }
    } else {
      //搜索结果小说详情
      String title = XpathHelper.get(dom, Bq_XinXpathUri.Xpath_novel_name);
      if (!TextUtils.isEmpty(title)) {
        String chapterUrl = XpathHelper.get(dom, Bq_XinXpathUri.Xpath_chapter_url);
        assert chapterUrl != null;
        url = Bq_XinUrl.Url_Base + chapterUrl.substring(0, chapterUrl.lastIndexOf("/"));
      }
    }
    //没有获取到链接直接返回
    if (TextUtils.isEmpty(url)) {
      return null;
    }

    return getNovelInfo(dom, url);
  }

  @Override public JSONObject getChapterContent(String chapterUrl) {
    Document dom = XpathHelper.getDom(chapterUrl);
    String title = XpathHelper.get(dom, Bq_XinXpathUri.Xpath_chapter_title);
    List<String> list = XpathHelper.getN(dom, Bq_XinXpathUri.Xpath_chapter_content);
    StringBuilder buffer = new StringBuilder();
    if (null != list) {
      for (String chapter : list) {
        //html = Html.fromHtml(html).toString();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
          chapter = Html
              .fromHtml(chapter, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
          chapter = Html.fromHtml(chapter).toString();
        }
        buffer.append("      ").append(chapter).append("\n");
      }
    }
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(K.Chapter_Title, title.trim());
      jsonObject.put(K.Chapter_Path, chapterUrl);
      jsonObject.put(K.Chapter_Content, buffer.toString());
    } catch (Exception e) {
      Log.e("<Fy>", e.getMessage());
    }
    return jsonObject;
  }

  @Override public JSONArray getNovelInfoByAuthor(String author) {
    Document dom = XpathHelper.getDom(String.format(Bq_XinUrl.Url_Search, author));
    NodeList nodeList = XpathHelper.getNodeList(dom, Bq_XinXpathUri.Xpath_Search_Item);
    if (null == nodeList) return null;
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < nodeList.getLength(); index++) {
      Node item = nodeList.item(index);
      String path = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Search_Path);
      String name = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Search_Name);
      String authorName = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Search_Author).trim();
      String logo = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Search_logo);
      String lastChapter = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Search_Last_Chapter);
      String lastModified = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Search_Last_Modified);
      long lastModifiedL = getLastModified(lastModified);
      String intro = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Search_Intro);

      try {
        jsonArray.put(new JSONObject().put(K.Novel_Author, authorName)
            .put(K.Novel_Path, path)
            .put(K.Novel_Name, name)
            .put(K.Novel_logo, logo)
            .put(K.Novel_last_chapter, lastChapter)
            .put(K.Novel_last_modified, lastModifiedL)
            .put(K.Novel_intro, intro));
      } catch (JSONException e) {
      }
    }

    return jsonArray;
  }

  @Override public JSONArray getNavigateList() {
    Document dom = XpathHelper.getDom(Bq_XinUrl.Url_Base);
    List<String> navUrls = XpathHelper.getN(dom, Bq_XinXpathUri.Xpath_Navigate_Url_List);
    List<String> navNames = XpathHelper.getN(dom, Bq_XinXpathUri.Xpath_Navigate_Name_List);
    if (null == navNames || navUrls == null) return null;
    JSONArray jsonArray = new JSONArray();
    //删除需要登录的导航栏
    for (int index = 0; index < navUrls.size(); index++) {
      //过滤无用的导航
      if (index == 0 || index == 1 || index == 11 || index == 9 || index == 10) continue;
      JSONObject jsonObject = new JSONObject();
      //首页单独处理
      try {
        jsonObject.put(K.Navigate_Name, navNames.get(index));
        String path = navUrls.get(index);
        if (null != path && !path.startsWith(Bq_XinUrl.Url_Base)) {
          path = Bq_XinUrl.Url_Base + path;
        }
        jsonObject.put(K.Navigate_Path, path);
        jsonArray.put(jsonObject);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return jsonArray;
  }

  @Override public JSONArray getRecommendNovels() {
    Document dom = XpathHelper.getDom(Bq_XinUrl.Url_Base);
    JSONArray jsonArray = getNavigateItem(dom);
    return jsonArray;
  }

  @Override public JSONArray getRecommendNovelsByName(String novelName) {
    return null;
  }

  private JSONArray getLasterUpdateItem(Document dom) {
    NodeList nodeList =
        XpathHelper.getNodeList(dom, Bq_XinXpathUri.Xpath_Navigate_Laster_Update_Novel_Item);
    if (null != nodeList) {
      JSONArray jsonArray = new JSONArray();
      for (int index = 0; index < nodeList.getLength(); index++) {
        Node node = nodeList.item(index);
        String path = XpathHelper.get(node, Bq_XinXpathUri.Xpath_Navigate_Laster_Update_Novel_Path);
        String author =
            XpathHelper.get(node, Bq_XinXpathUri.Xpath_Navigate_Laster_Update_Novel_Author);
        String name = XpathHelper.get(node, Bq_XinXpathUri.Xpath_Navigate_Laster_Update_Novel_Name);
        String lastChapter =
            XpathHelper.get(node, Bq_XinXpathUri.Xpath_Navigate_Laster_Update_Novel_Laster_Chapter);
        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put(K.Novel_Path, Bq_XinUrl.Url_Base + path);
          jsonObject.put(K.Novel_Name, name);
          jsonObject.put(K.Novel_Author, author);
          jsonObject.put(K.Novel_last_chapter, lastChapter);
          jsonArray.put(jsonObject);
        } catch (JSONException e) {
        }
      }
      return jsonArray;
    }
    return null;
  }

  public JSONArray getNavigateItem(Document dom) {
    NodeList nodeList =
        XpathHelper.getNodeList(dom, Bq_XinXpathUri.Xpath_Navigate_Novel_Item);
    if (null == nodeList) return null;
    JSONArray novels = new JSONArray();
    for (int m = 0; m < nodeList.getLength(); m++) {
      Node item = nodeList.item(m);
      List<String> introList = XpathHelper.getN(item, Bq_XinXpathUri.Xpath_Navigate_Novel_Intro);
      String intro = null;
      if (null != introList) {
        StringBuilder buffer = new StringBuilder();
        for (String in : introList) {
          buffer.append(in.replace("\n", ""));
        }
        intro = buffer.toString();
      }
      String author = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Navigate_Novel_Author);
      String logo = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Navigate_Novel_Logo);
      if (null != logo && !logo.contains(Bq_XinUrl.Url_Domain)) {
        logo = Bq_XinUrl.Url_Base + logo;
      }
      String path = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Navigate_Novel_Path);
      if (null != path && !path.contains(Bq_XinUrl.Url_Domain)) {
        path = Bq_XinUrl.Url_Base + path;
      }
      String name = XpathHelper.get(item, Bq_XinXpathUri.Xpath_Navigate_Novel_Name);

      try {
        JSONObject novelJ = new JSONObject();
        novelJ.put(K.Novel_Name, name);
        novelJ.put(K.Novel_Author, author);
        novelJ.put(K.Novel_intro, intro);
        novelJ.put(K.Novel_logo, logo);
        novelJ.put(K.Novel_Path, path);
        novels.put(novelJ);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return novels;
  }

  @Override public JSONArray getNavigateItem(String url) {
    Document dom = XpathHelper.getDom(url);
    JSONArray navigateItem = getNavigateItem(dom);
    JSONArray lasterUpdateItem = getLasterUpdateItem(dom);
    JSONArray json = new JSONArray();
    if (null != navigateItem) {
      for (int index = 0; index < navigateItem.length(); index++) {
        try {
          json.put(navigateItem.getJSONObject(index));
        } catch (JSONException e) {
        }
      }
    }
    if (null != lasterUpdateItem) {
      for (int index = 0; index < lasterUpdateItem.length(); index++) {
        try {
          json.put(lasterUpdateItem.getJSONObject(index));
        } catch (JSONException e) {
        }
      }
    }
    return json;
  }

  @Override public JSONArray getNavigateItem(String url, int page) {
    return null;
  }

  public static void main(String args[]) {
    String allChapterUrls = new Bq_Xin().getNovelInfoByName("武动乾坤").toString();
    System.out.println("Chapter --> " + allChapterUrls);
    //List<String> allChapterUrls = fy.getNovelInfoByUrl("http://www.baoliny.com/1/index.html");
    new Thread(new Runnable() {
      @Override public void run() {
        Bq_Xin fy = new Bq_Xin();
      }
    }).start();
  }
}
