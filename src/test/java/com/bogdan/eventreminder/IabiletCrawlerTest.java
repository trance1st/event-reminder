package com.bogdan.eventreminder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class IabiletCrawlerTest {

    @Test
    public void testParsing() throws Exception {
        IaBiletCrawler iaBiletCrawler = new IaBiletCrawler();
        EventDao eventDao = Mockito.mock(EventDao.class);
        iaBiletCrawler.setEventDao(eventDao);

        String html = Files.readString(Path.of("src/test/resources/iabilet_page1.html"));
        List<Event> events = iaBiletCrawler.extract(html);

        assertEquals(events.size(), 24);
        assertEquals(events.get(0).getName(),"Bucuresti: Museum of Senses");
    }

    @Test
    public void testParsingAndEventsAreNotDuplicatedInTheDatabase() throws Exception {
        IaBiletCrawler iaBiletCrawler = new IaBiletCrawler();
        EventDao eventDao = Mockito.mock(EventDao.class);

        Mockito.when(eventDao.countAllByName("Bucuresti: Museum of Senses")).thenReturn(1);

        iaBiletCrawler.setEventDao(eventDao);

        String html = Files.readString(Path.of("src/test/resources/iabilet_page2.html"));
        List<Event> events = iaBiletCrawler.extract(html);

        assertEquals(events.size(), 3);
        Mockito.verify(eventDao, Mockito.times(2)).save(any(Event.class));
    }
}
