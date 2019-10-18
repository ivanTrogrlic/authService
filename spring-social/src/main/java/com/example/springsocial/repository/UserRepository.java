package com.example.springsocial.repository;

import com.example.springsocial.model.User;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;

public interface UserRepository extends CouchbasePagingAndSortingRepository<User, String> {

    User findByEmail(String email);

}
