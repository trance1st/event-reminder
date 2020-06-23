package com.bogdan.eventreminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.GeneratedValue;

@Controller
public class UserController {

    @Autowired
    KeywordDao keywordDao;

    @GetMapping()
    public ModelAndView saveNewKeyword() {
        Iterable<Keyword> keywordList = keywordDao.findAll();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("keywords", keywordList);

        return modelAndView;
    }

    @PostMapping("/keywords")
    public ModelAndView saveNewKeyword(@RequestParam("keyword") String keyword) {
        Keyword newKeyword = new Keyword();
        newKeyword.setKeyword(keyword);

        keywordDao.save(newKeyword);

        Iterable<Keyword> keywordList = keywordDao.findAll();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("keywords", keywordList);

        return modelAndView;
    }
}
