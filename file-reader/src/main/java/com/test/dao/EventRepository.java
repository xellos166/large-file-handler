package com.test.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.test.model.entity.EventDto;

@Repository
public interface EventRepository extends CrudRepository<EventDto, String> {

}
