package com.reader.support;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/19
 */
public interface ISupport {

  /**
   * 通过小说目录地址获取小说信息
   * @param url
   * @return
   */
  JSONObject getNovelInfoByUrl(String url);

  /**
   * 通过小说名获取小说信息
   * @param name
   * @return
   */
  JSONObject getNovelInfoByName(String name);

  /**
   * 已知章节页面的URL
   */
  JSONObject getChapterContent(String url);

  /**
   * 通过作者名搜索小说信息
   * @param author
   * @return
   */
  JSONArray getNovelInfoByAuthor(String author);

  /**
   * 获取小说站点导航栏内容
   * @return
   */
  JSONArray getNavigateList();

  /**
   * 获取推荐小说
   * @return
   */
  JSONArray getRecommendNovels();

  JSONArray getNavigateItem(String url);

}
