package com.bogdan.eventreminder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IaBiletCrawler {

    Logger LOG = LoggerFactory.getLogger(IaBiletCrawler.class);

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


    @Scheduled(fixedRate = 10000)
    public void run() {
        LOG.info("A pornit IaBiletCrawler");
        extract("https://www.iabilet.ro/bilete-in-bucuresti/?eventListPaginatedId=eventListPaginatedId&page=1");
        LOG.info("S-a terminat IaBiletCrawler");
    }

    public List<Event> extract(String html)  {
        LOG.info("Incep sa parsez fisiserul html: " + html);
        List<Event> events = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect(html).get();
        } catch (IOException e) {
            LOG.error("A aparut o eroare cand s-a procesat html" + html);
            e.printStackTrace();
        }
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
            LocalDate dateStart = LocalDate.of(
                    year,
                    monthToInt.get(dateStartMonth),
                    Integer.parseInt(dateStartDay));
            event.setDateStart(dateStart);

            event.setDescription(eventElement.select(".main-info div").get(2).text());
            event.setLocation(eventElement.select(".location .venue span").get(0).text());
            event.setCity(eventElement.select(".location .venue span").get(1).text());

            events.add(event);
        }

        for (Event event : events) {
            Integer existingEventsWithSameName = eventDao.countAllByName(event.getName());
            if (existingEventsWithSameName == 0) {
                eventDao.save(event);
            }
        }

        LOG.info("Am terminat sa parsez fisiserul html: " + html +". Am extras " + events);

        return events;
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
