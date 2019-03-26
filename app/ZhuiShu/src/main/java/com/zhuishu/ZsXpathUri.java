package com.zhuishu;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/20
 */
public class ZsXpathUri {

  //小说章节内容
  public static final String Xpath_chapter_content = "//div[@class='inner-text']/text()";
  //小说章节标题
  public static final String Xpath_chapter_title = "//span[@class='current-chapter']/text()";
  //小说章节列表的url
  public static String Xpath_chapter_url =
      "//ul[@class='chapter-list clearfix hidden-list']/li/a/@href";
  //匹配小说章节列表
  public static String Xpath_chapter_list =
      "//ul[@class='chapter-list clearfix hidden-list']/li/a/text()";
  //匹配小说结果页
  public static final String Xpath_Search = "//div[@class='books-list']/div/@data-href";
  //匹配小说结果页
  public static final String Xpath_Search_Item = "//div[@class='books-list']/div[@class='book']";
  //匹配小说结果页Logo
  public static final String Xpath_Search_Novel_Logo = "img/@src";
  //匹配小说结果页名称
  public static final String Xpath_Search_Novel_Name = "div/h4[@class='name']/a/text()";
  //匹配小说结果页作者
  public static final String Xpath_Search_Novel_Author= "div/p[@class='author']/span[1]/text()";
  //匹配小说结果页简介
  public static final String Xpath_Search_Novel_Intro = "div/p[@class='desc']/text()";
  //匹配小说结果页Path
  public static final String Xpath_Search_Novel_Path = "@data-href";
  //小说书名
  public static final String Xpath_novel_name = "//div[@class='info']//h1/text()";
  //小说作者
  public static final String Xpath_novel_author = "//p[@class='sup']/a/text()";
  //小说最新更新时间
  public static final String Xpath_novel_last_modified = "//div[@class='info']/p[3]/text()";
  //小说logo
  public static final String Xpath_novel_logo = "//div[@class='book-info']/img/@src";
  //小说简介
  public static final String Xpath_novel_introduction = "//p[@class='content intro']/text()";
  //导航列表
  public static final String Xpath_Navigate_List =
      "//div[@class='c-full-sideBar']/div[@class='sub-sideBar']";
  //导航子列表
  public static final String Xpath_Navigate_Sub_List = "a";
  //导航列表标题
  public static final String Xpath_Navigate_Tag = "div/text()";
  //导航地址列表
  public static final String Xpath_Navigate_Url = "@href";
  //导航名称列表
  public static final String Xpath_Navigate_Name = "span/text()";
  //导航小说地址
  public static final String Xpath_Navigate_Novel_Path = "@href";
  //导航小说名称
  public static final String Xpath_Navigate_Novel_Name = "div/h4[@class='name']/span/text()";
  //导航小说logo
  public static final String Xpath_Navigate_Novel_Logo = "img/@src";
  //导航小说作者
  public static final String Xpath_Navigate_Novel_Author = "div/p[@class='author']/span/text()";
  //导航小说简介
  public static final String Xpath_Navigate_Novel_Intro = "div/p[@class='desc']/text()";
  //导航小说条目内容
  public static final String Xpath_Navigate_Novel_Item =
      "//div[@class='books-list']/a[@class='book']";
}
