package com.bogdan.eventreminder;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventDao extends CrudRepository<Event, Integer> {

    List<Event> findAll();

    Integer countAllByName(String name);
}


