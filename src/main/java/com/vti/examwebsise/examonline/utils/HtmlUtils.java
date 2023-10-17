package com.vti.examwebsise.examonline.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@UtilityClass
public class HtmlUtils {


    public String removeTag(@NonNull String htmlCode) {
        htmlCode = htmlCode.replace("&nbsp;", "%space");
        Document doc = Jsoup.parse(htmlCode);
        StringBuilder b = new StringBuilder();
        for (Element p : doc.select("h2,h3,h4,h5,h6,p")) {
            b.append(p.text());
            b.append(System.getProperty("line.separator"));
        }

        return b.toString().replaceAll("%space", " ");
    }


}
