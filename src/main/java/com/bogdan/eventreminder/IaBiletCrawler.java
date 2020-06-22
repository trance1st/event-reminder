package com.bogdan.eventreminder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IaBiletCrawler {

    private Map<String, Integer> monthToInt = new HashMap<>();

    @Autowired
    private EventDao eventDao;

    public IaBiletCrawler() {
        monthToInt.put("ian", 1);
        monthToInt.put("feb", 2);
        monthToInt.put("mar", 3);
        monthToInt.put("apr", 4);
        monthToInt.put("mai", 5);
        monthToInt.put("iun", 6);
        monthToInt.put("iul", 7);
        monthToInt.put("aug", 8);
        monthToInt.put("sep", 9);
        monthToInt.put("oct", 10);
        monthToInt.put("nov", 11);
        monthToInt.put("dec", 12);
    }

    @Scheduled(fixedRate = 30000)
    //"https://www.iabilet.ro/bilete-in-bucuresti/?eventListPaginatedId=eventListPaginatedId&page=1"
    public List<Event> extract(String html) throws Exception {
        List<Event> events = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(".event-list-item");
        for (Element eventElement : elements) {
            Element elementTitle = eventElement.selectFirst(".title span");

            String dateStartDay = eventElement.select(".date-start .date-day").text();
            String dateStartMonth = eventElement.select(".date-start .date-month").text();
            String dateStartYear = eventElement.select(".date-start .date-year").text().replace("'", "");

            Event event = new Event();
            event.setName(elementTitle.text());

            int year = LocalDate.now().getYear();
            if (!dateStartYear.equals("")) {
                year = Integer.parseInt("20" + Integer.parseInt(dateStartYear));
            }
            LocalDate eventDate = LocalDate.of(
                    year,
                    monthToInt.get(dateStartMonth),
                    Integer.parseInt(dateStartDay));
            event.setDate(eventDate);

            events.add(event);
        }

        for (Event event : events) {
            Integer existingEventsWithSameName = eventDao.countAllByName(event.getName());
            if (existingEventsWithSameName == 0) {
                eventDao.save(event);
            }
        }

        return events;
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
