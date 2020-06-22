package com.bogdan.eventreminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventDao eventDao;

    @GetMapping("/events")
    @ResponseBody
    public List<Event> events() {
        return eventDao.findAll();
    }
}
