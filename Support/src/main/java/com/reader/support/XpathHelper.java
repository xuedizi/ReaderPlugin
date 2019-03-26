package com.reader.support;

import android.text.Html;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/19
 */
public class XpathHelper {

  public static NodeList getNodeList(String url, String xpath) {
    Document dom = getDom(url);
    return getNodeList(dom, xpath);
  }

  public static NodeList getNodeList(Document dom, String xpath) {
    try {
      Object evaluate = XPathFactory.newInstance()
          .newXPath()
          .evaluate(xpath, dom, XPathConstants.NODESET);
      if (evaluate instanceof NodeList) {
        return (NodeList) evaluate;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static NodeList getNodeList(Node node, String xpath) {
    try {
      Object evaluate = XPathFactory.newInstance()
          .newXPath()
          .evaluate(xpath, node, XPathConstants.NODESET);
      if (evaluate instanceof NodeList) {
        return (NodeList) evaluate;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static String get(Document document, String xpath) {
    Object evaluate = null;
    try {
      evaluate = XPathFactory.newInstance()
          .newXPath()
          .evaluate(xpath, document, XPathConstants.NODESET);
      if (evaluate instanceof NodeList) {
        return get((NodeList) evaluate);
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String get(Node node, String xpath) {
    Object evaluate = null;
    try {
      evaluate = XPathFactory.newInstance()
          .newXPath()
          .evaluate(xpath, node, XPathConstants.NODESET);
      if (evaluate instanceof NodeList) {
        return get((NodeList) evaluate);
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static List<String> getN(Node node, String xpath) {
    Object evaluate = null;
    try {
      evaluate = XPathFactory.newInstance()
          .newXPath()
          .evaluate(xpath, node, XPathConstants.NODESET);
      if (evaluate instanceof NodeList) {
        return getN((NodeList) evaluate);
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static List<String> getN(Document document, String xpath) {
    Object evaluate = null;
    try {
      evaluate = XPathFactory.newInstance()
          .newXPath()
          .evaluate(xpath, document, XPathConstants.NODESET);
      if (evaluate instanceof NodeList) {
        return getN((NodeList) evaluate);
      }
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Document getDom(String url) {
    String html = null;
    try {
      Connection connect = Jsoup.connect(url).validateTLSCertificates(false);
      org.jsoup.nodes.Document document = connect.get();
      html = document.body().html();
      HtmlCleaner htmlCleaner = new HtmlCleaner();
      TagNode clean = htmlCleaner.clean(html);
      Document dom = new DomSerializer(new CleanerProperties()).createDOM(clean);
      return dom;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Document getDomByPost(String url, Map<String, String> params) {
    return getDomByPost(url, params, "GBK");
  }

  public static Document getDomByPost(String url, Map<String, String> params, String charset) {
    String html = null;
    try {

      html = Jsoup.connect(url)
          .validateTLSCertificates(false)
          .data(params)
          .postDataCharset(charset)
          .post().body().html();

      HtmlCleaner htmlCleaner = new HtmlCleaner();
      TagNode clean = htmlCleaner.clean(html);
      Document dom = new DomSerializer(new CleanerProperties()).createDOM(clean);
      return dom;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Document getDom(org.jsoup.nodes.Document document) {
    try {
      HtmlCleaner htmlCleaner = new HtmlCleaner();
      TagNode clean = htmlCleaner.clean(document.body().html());
      Document dom = new DomSerializer(new CleanerProperties()).createDOM(clean);
      return dom;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Document getDom(Connection.Response response) {
    try {
      org.jsoup.nodes.Document document = response.parse();
      HtmlCleaner htmlCleaner = new HtmlCleaner();
      TagNode clean = htmlCleaner.clean(document.body().html());
      Document dom = new DomSerializer(new CleanerProperties()).createDOM(clean);
      return dom;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Connection.Response getResponse(String url) {
    try {
      return Jsoup.connect(url).execute();
    } catch (IOException e) {

    }
    return null;
  }

  public static Map<String, String> getCookie(String url) {
    Connection.Response response = getResponse(url);
    if (null != response) {
      return response.cookies();
    }
    return new HashMap<>();
  }

  public static String get(String url, String xpath) {
    NodeList nodeList = getNodeList(url, xpath);
    return get(nodeList);
  }

  public static String get(NodeList nodeList) {
    if (null != nodeList && nodeList.getLength() > 0) {
      try {
        return Html.fromHtml(nodeList.item(0).getNodeValue().trim()).toString();
      } catch (Exception e) {
      }
    }
    return null;
  }

  public static List<String> getN(String url, String xpath) {
    NodeList nodeList = getNodeList(url, xpath);
    List<String> values = getN(nodeList);
    return values;
  }

  public static List<String> getN(NodeList nodeList) {
    List<String> values = new ArrayList<>();
    if (null != nodeList) {
      for (int index = 0; index < nodeList.getLength(); index++) {
        String nodeValue = nodeList.item(index).getNodeValue();
        try {
          values.add(Html.fromHtml(nodeValue.trim()).toString());
        } catch (Exception e) {
        }
      }
    }
    return values;
  }
}
