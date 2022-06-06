package com.assignment.gds.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.assignment.gds.entity.SmartCard;

public interface SmartCardRepository extends CrudRepository<SmartCard, Long> {
    Optional<SmartCard> findById(Long id);
}
