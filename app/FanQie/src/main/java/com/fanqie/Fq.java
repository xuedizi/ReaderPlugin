package com.fanqie;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.reader.support.ISupport;
import com.reader.support.K;
import com.reader.support.XpathHelper;
import com.reader.utils.DataUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO 最新章节，作者，最后更新时间 xpath有问题
 *
 * @author xichao
 * @version v1.0.0
 * @created 2019/1/15
 */
public class Fq implements ISupport {

  @Override public JSONObject getNovelInfoByUrl(String url) {
    Document dom = XpathHelper.getDom(url);
    return getNovelInfo(dom, url);
  }

  private JSONObject getNovelInfo(Document dom, String url) {
    String novelName = XpathHelper.get(dom, FqXpathUri.Xpath_novel_name);
    String novelLogo = XpathHelper.get(dom, FqXpathUri.Xpath_Novel_Logo);
    String novelAuthor = XpathHelper.get(dom, FqXpathUri.Xpath_novel_author);
    novelAuthor = getNovelAuthor(novelAuthor);
    String novelLastChapter = XpathHelper.get(dom, FqXpathUri.Xpath_Novel_last_Chapter);
    String novelLastModified = XpathHelper.get(dom, FqXpathUri.Xpath_Novel_Last_Modified);
    long lastModified = getLastModified(novelLastModified);
    String novelIntro = XpathHelper.get(dom, FqXpathUri.Xpath_Novel_Intro);
    JSONArray chapters = getChapters(dom);
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(K.Novel_Path, url);
      jsonObject.put(K.Novel_Name, novelName);
      jsonObject.put(K.Novel_Author, novelAuthor);
      jsonObject.put(K.Novel_intro, novelIntro);
      jsonObject.put(K.Novel_last_chapter, novelLastChapter);
      jsonObject.put(K.Novel_last_modified, lastModified);
      jsonObject.put(K.Novel_logo, novelLogo);
      jsonObject.put(K.Novel_Catalogue, chapters);
    } catch (JSONException e) {
    }
    return jsonObject;
  }

  private String getNovelAuthor(String novelAuthor) {
    if (!TextUtils.isEmpty(novelAuthor) && novelAuthor.split("：").length > 1) {
      novelAuthor = novelAuthor.split("：")[1];
    }
    return novelAuthor;
  }

  private long getLastModified(String novelLastModified) {
    if (!TextUtils.isEmpty(novelLastModified)) {
      String[] split = novelLastModified.split("：");
      if (split.length > 1) {
        return DataUtils.getString2Date(split[1], "yyyy-MM-dd HH:mm");
      }
    }
    return 0;
  }

  private long getLastModifiedFromSearch(String novelLastModified) {
    novelLastModified = "20" + novelLastModified;
    return DataUtils.getString2Date(novelLastModified, "yyyy-MM-dd");
  }

  public JSONArray getChapters(Document dom) {
    List<String> titles = XpathHelper.getN(dom, FqXpathUri.Xpath_Chapter_Title_List);
    List<String> urls = XpathHelper.getN(dom, FqXpathUri.Xpath_Chapter_Url_List);
    if (null == titles || null == urls) {
      return null;
    }
    JSONArray chapterCatalogues = new JSONArray();
    int len = titles.size() > urls.size() ? urls.size() : titles.size();
    for (int index = 0; index < len; index++) {
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put(K.Catalogue_Path, FqUrl.Url_Base_Book + urls.get(index));
        jsonObject.put(K.Catalogue_Title, titles.get(index));
        chapterCatalogues.put(jsonObject);
      } catch (JSONException e) {
        Log.e("<Reader>", e.getMessage());
      }
    }
    return chapterCatalogues;
  }

  @Override public JSONObject getNovelInfoByName(String name) {
    String pinyin = toHanyuPinyin(name);
    return getNovelInfoByUrl(String.format(FqUrl.Url_Search, pinyin));
  }

  @Override public JSONObject getChapterContent(String url) {
    Document dom = XpathHelper.getDom(url);
    String title = XpathHelper.get(dom, FqXpathUri.Xpath_Chapter_Title);
    List<String> list = XpathHelper.getN(dom, FqXpathUri.Xpath_Chapter_Content);

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
      jsonObject.put(K.Chapter_Title, title);
      jsonObject.put(K.Chapter_Path, url);
      jsonObject.put(K.Chapter_Content, buffer.toString());
    } catch (Exception e) {
      Log.e("<Fy>", e.getMessage());
    }
    return jsonObject;
  }

  @Override public JSONArray getNovelInfoByAuthor(String author) {
    String url = null;
    try {
      url = String.format(FqUrl.Url_Search_By_Author, URLEncoder.encode(author, "gbk"));
    } catch (UnsupportedEncodingException e) {
    }
    Document dom = XpathHelper.getDom(url);
    NodeList nodeList = XpathHelper.getNodeList(dom, FqXpathUri.Xpath_Search_item);
    if (null == nodeList) return null;
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < nodeList.getLength(); index++) {
      if (index == 0) continue;
      Node item = nodeList.item(index);
      String path = FqUrl.Url_Base + XpathHelper.get(item, FqXpathUri.Xpath_Search_Novel_Path);
      String name = XpathHelper.get(item, FqXpathUri.Xpath_Search_Novel_Name);
      String lastChapter = XpathHelper.get(item, FqXpathUri.Xpath_Search_Novel_Last_Chapter);
      String lastModified = XpathHelper.get(item, FqXpathUri.Xpath_Search_Novel_Last_Modified);
      long lastModifiedL = getLastModifiedFromSearch(lastModified);
      String authorName = XpathHelper.get(item, FqXpathUri.Xpath_Search_Novel_Author);
      try {
        jsonArray.put(new JSONObject().put(K.Novel_Path, path)
            .put(K.Novel_last_modified, lastModifiedL)
            .put(K.Novel_Name, name)
            .put(K.Novel_last_chapter, lastChapter)
            .put(K.Novel_Author, authorName));
      } catch (JSONException e) {
      }
    }
    return jsonArray;
  }

  @Override public JSONArray getNavigateList() {
    Document dom = XpathHelper.getDom(FqUrl.Url_Base);
    List<String> navUrls = XpathHelper.getN(dom, FqXpathUri.Xpath_Navigate_Url_List);
    List<String> navNames = XpathHelper.getN(dom, FqXpathUri.Xpath_Navigate_Name_List);
    if (null == navNames || navUrls == null) return null;
    JSONArray jsonArray = new JSONArray();
    //删除需要登录的导航栏
    for (int index = 0; index < navUrls.size(); index++) {
      //过滤无用的导航
      if (index == 0 || index == 9 || index == 10) continue;
      JSONObject jsonObject = new JSONObject();
      //首页单独处理
      try {
        jsonObject.put(K.Navigate_Name, navNames.get(index));
        String path = navUrls.get(index);
        if (null != path && !path.startsWith(FqUrl.Url_Base)) {
          path = FqUrl.Url_Base + path;
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
    Document dom = XpathHelper.getDom(FqUrl.Url_Base);
    JSONArray jsonArray = getNavigateItem(dom);
    return jsonArray;
  }

  @Override public JSONArray getRecommendNovelsByName() {
    return null;
  }

  private JSONArray getLasterUpdateItem(Document dom) {
    NodeList nodeList =
        XpathHelper.getNodeList(dom, FqXpathUri.Xpath_Navigate_Laster_Update_Novel_Item);
    if (null != nodeList) {
      JSONArray jsonArray = new JSONArray();
      for (int index = 0; index < nodeList.getLength(); index++) {
        Node node = nodeList.item(index);
        String path = XpathHelper.get(node, FqXpathUri.Xpath_Navigate_Laster_Update_Novel_Path);
        String author =
            XpathHelper.get(node, FqXpathUri.Xpath_Navigate_Laster_Update_Novel_Author);
        String name = XpathHelper.get(node, FqXpathUri.Xpath_Navigate_Laster_Update_Novel_Name);
        String lastChapter =
            XpathHelper.get(node, FqXpathUri.Xpath_Navigate_Laster_Update_Novel_Laster_Chapter);
        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put(K.Novel_Path, FqUrl.Url_Base + path);
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
        XpathHelper.getNodeList(dom, FqXpathUri.Xpath_Navigate_Novel_Item);
    if (null == nodeList) return null;
    JSONArray novels = new JSONArray();
    for (int m = 0; m < nodeList.getLength(); m++) {
      Node item = nodeList.item(m);
      List<String> introList = XpathHelper.getN(item, FqXpathUri.Xpath_Navigate_Novel_Intro);
      String intro = null;
      if (null != introList) {
        StringBuilder buffer = new StringBuilder();
        for (String in : introList) {
          buffer.append(in.replace("\n", ""));
        }
        intro = buffer.toString();
      }
      String author = XpathHelper.get(item, FqXpathUri.Xpath_Navigate_Novel_Author);
      String logo = XpathHelper.get(item, FqXpathUri.Xpath_Navigate_Novel_Logo);
      if (null != logo && !logo.startsWith(FqUrl.Url_Base)) {
        logo = FqUrl.Url_Base + logo;
      }
      String path = XpathHelper.get(item, FqXpathUri.Xpath_Navigate_Novel_Path);
      if (null != path && !path.startsWith(FqUrl.Url_Base)) {
        path = FqUrl.Url_Base + path;
      }
      String name = XpathHelper.get(item, FqXpathUri.Xpath_Navigate_Novel_Name);

      try {
        JSONObject novelJ = new JSONObject();
        novelJ.put(K.Novel_Name, name);
        novelJ.put(K.Novel_Author, author);
        novelJ.put(K.Novel_intro, intro);
        if (null != logo && !logo.startsWith(FqUrl.Url_Base)) {
          logo = FqUrl.Url_Base + logo;
        }
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

  public String toHanyuPinyin(String content) {
    StringBuilder builder = new StringBuilder();
    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    for (int index = 0; index < content.length(); index++) {
      try {
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(content.charAt(index), format);
        builder.append(pinyinArray[0]);
      } catch (Exception e) {
      }
    }
    return builder.toString();
  }
}
