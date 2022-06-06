package com.assignment.gds.repository;

import org.springframework.data.repository.CrudRepository;

import com.assignment.gds.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    
}
