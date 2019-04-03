package com.support.picture;

import org.json.JSONArray;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2019/4/2
 */
public interface IPictureSupport {

  /**
   * 获取推荐的图集
   */
  JSONArray getRecommendPictures();

  /**
   * 获取图集站点导航栏内容
   */
  JSONArray getNavigateList();

  /**
   * 通过页数获取导航列表
   */
  JSONArray getNavigateItem(String url, int page);
}
