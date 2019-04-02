package com.fengyun;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/20
 */
public class XpathUri {

  //小说章节内容
  public static final String Xpath_chapter_content = "//p[@id='content']/text()";
  //小说章节标题
  public static final String Xpath_chapter_title= "//div[@class='readerTitle']/h1/text()";
  //小说章节列表的url
  public static final String Xpath_chapter_url = "//td[@class='ccss']/a/@href";
  //匹配小说章节列表
  public static final String Xpath_chapter_list = "//td[@class='ccss']/a/text()";
  //匹配小说结果页
  public static final String Xpath_Search = "//div/h3[@class='c-title']/a[@cpos='title']/@href";
  //匹配小说结果页列表
  public static final String Xpath_Search_Item = "//div[@id='content']/table[@class='grid']/tbody/tr[@id='nr']";
  //匹配小说结果页小说名称
  public static final String Xpath_Search_Novel_Name = "td[1]/a/text()";
  //匹配小说结果页小说路径
  public static final String Xpath_Search_Novel_Path = "td[1]/a/@href";
  //匹配小说结果页最新章节
  public static final String Xpath_Search_Novel_Last_Chapter = "td[2]/a/text()";
  //匹配小说结果页小说作者
  public static final String Xpath_Search_Novel_Author = "td[3]/text()";
  //匹配小说结果页小说最新更新时间
  public static final String Xpath_Search_Novel_Last_Modified = "td[5]/text()";
  //小说书名
  public static final String Xpath_novel_name = "//div[@class='readerListHeader']/h1/text()";
  //搜索结果标题列表
  public static final String Xpath_Novel_Search_Title_List = "//tr[@id='nr']/td[@class='odd']/a/text()";
  //搜索结果链接列表
  public static final String Xpath_Novel_Search_Url_List = "//tr[@id='nr']/td[@class='odd']/a/@href";
  //导航地址列表
  public static final String Xpath_Navigate_Url_List = "//div[@class='nav']/ul/li/a/@href";
  //导航名称列表
  public static final String Xpath_Navigate_Name_List = "//div[@class='nav']/ul/li/a/text()";
  //导航小说地址
  public static final String Xpath_Navigate_Novel_Path = "dl/dt/a/@href";
  //导航小说名称
  public static final String Xpath_Navigate_Novel_Name = "dl/dt/a/text()";
  //导航小说logo
  public static final String Xpath_Navigate_Novel_Logo = "div[@class='image']/a/img/@src";
  //导航小说作者
  public static final String Xpath_Navigate_Novel_Author = "dl/dt/span/text()";
  //导航小说简介
  public static final String Xpath_Navigate_Novel_Intro = "dl/dd/text()";
  //导航小说条目内容
  public static final String Xpath_Navigate_Novel_Item = "//div[@id='hotcontent']/div/div[@class='item']";
  //最近更新的小说列表
  public static final String Xpath_Navigate_Laster_Update_Novel_Item = "//div[@id='newscontent']/div[@class='l']/ul/li";
  //最近更新小说地址
  public static final String Xpath_Navigate_Laster_Update_Novel_Path = "span[@class='s2']/a/@href";
  //最近更新小说名称
  public static final String Xpath_Navigate_Laster_Update_Novel_Name = "span[@class='s2']/a/text()";
  //最近更新小说作者
  public static final String Xpath_Navigate_Laster_Update_Novel_Author = "span[@class='s5']/text()";
  //最近更新小说最新章节
  public static final String Xpath_Navigate_Laster_Update_Novel_Laster_Chapter = "span[@class='s3']/a/text()";
}
