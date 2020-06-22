package com.bogdan.eventreminder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EventimCrawler {

    //@Scheduled(fixedRate = 5000)
    public List<Event> extract() throws IOException {
        Document doc = Jsoup.connect("https://www.eventim.ro/ro/venues/bucuresti/city.html?elu=1&esp=2").get();

        Elements elements = doc.select(".m-eventListItem");
        for(Element element : elements) {
            Element elementTitle = element.selectFirst(".m-eventListItem__title");
            System.out.println(elementTitle.text());
        }

        return null;
    }
}
