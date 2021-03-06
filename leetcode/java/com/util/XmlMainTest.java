package com.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rensong.pu on 2016/9/13.
 */
public class XmlMainTest {

    public static void main(String[] args) {
        String svgCode = "F://svg.svg";
        XmlUtil xmlUtil = new XmlUtil(svgCode);
        xmlUtil.buildDoc();
        Document d = xmlUtil.getDocument();
        Node root = d.getFirstChild();
        xmlUtil.rgbWork(root);//rgb处理
        xmlUtil.clipPathNone(root);//处理url#
        List<String> textVal = new ArrayList<>(); //图例列表
        textVal.add("新访客-UV");
        textVal.add("老访客-UV");
        xmlUtil.legendWork(root,textVal);
        xmlUtil.saveToXml(d);
    }


}
