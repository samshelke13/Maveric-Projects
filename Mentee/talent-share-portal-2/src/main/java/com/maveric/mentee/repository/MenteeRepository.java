package com.maveric.mentee.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.maveric.mentee.model.Mentee;




public interface MenteeRepository extends MongoRepository<Mentee, Integer> {

	Optional<Mentee> findByEmployeeId(Integer employeeId);
}
