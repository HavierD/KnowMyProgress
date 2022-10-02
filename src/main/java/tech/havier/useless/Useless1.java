package tech.havier.useless;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class Useless1 {
    /**
     * for scraping data online
     */
    public static void main(String[] args) {
        //        System.out.println("@" + checkSingleString("        ") + "@");
//        SearchOnWeb(new ArrayList<>());
        String searchQuery = "likef";

        WebClient client = new WebClient(BrowserVersion.EDGE);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        String searchUrl = "https://www.oxfordlearnersdictionaries.com/definition/english/" + searchQuery;

        try {
            HtmlPage page = client.getPage(searchUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        HtmlElement head = page.getFirstByXPath("//h1[@class='headword']");
//        List<HtmlElement> itemList = page.getByXPath("//span[@class='def']");
//        if (itemList.size() == 0) {
//            System.out.println(false);
//        }else {
//            System.out.println(true);
//        }
    }
}
