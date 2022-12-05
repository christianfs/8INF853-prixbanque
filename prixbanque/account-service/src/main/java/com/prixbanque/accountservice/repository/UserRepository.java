package com.prixbanque.accountservice.repository;

import com.prixbanque.accountservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, Long>{
}
