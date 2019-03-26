package com.qinxiaoshuo;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/20
 */
public class QinXsXpathUri {

  //小说章节内容
  public static final String Xpath_chapter_content = "//div[@class='chaptercontent']/text()";
  //小说章节标题
  public static final String Xpath_chapter_title = "//p[@id='BookTitle']/text()";
  //小说章节列表的url
  public static String Xpath_chapter_url = "//div[@class='chapter cpt-828586']/a/@href";
  //匹配小说章节列表
  public static String Xpath_chapter_list = "//div[@class='chapter cpt-828586']/a/@alt";

  public static String Xpath_catalog_url_by_chapter_url = "//a[@class='btn btn-primary btn-block btn-outlined']/@href";

  //匹配小说结果页
  public static final String Xpath_Search = "//div[@class='box_con']/div[@id='list']/dl/dd/a/text()";
  //小说书名
  public static final String Xpath_novel_name = "//div[@class='d_title']/h1/text()";
  //小说作者
  public static final String Xpath_novel_author = "//span[@class='p_author']/a/text()";

  public static final String Xpath_novel_last_chapter = "//div[@id='info']/p[4]/a/text()";

  public static final String Xpath_novel_logo = "//div[@id='bookimg']/mip-img/@src";
  //小说简介
  public static final String Xpath_novel_introduction = "//div[@id='bookintro']/text()";
  //小说更新时间
  public static final String Xpath_novel_last_modified="//li[@id='uptime']/span/text()";
  //搜索结果标题列表
  public static final String Xpath_Novel_Search_Title_List = "//div[@class='d_title']/h1/text()";
  //搜索结果链接列表
  public static final String Xpath_Novel_Search_Url_List = "//span[@class='s2']/a/@href";

}
