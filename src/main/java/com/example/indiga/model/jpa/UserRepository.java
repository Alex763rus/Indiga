package com.example.indiga.model.jpa;

import com.example.indiga.enums.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    List<User> findByUserRoleIn(List<UserRole> userRoles);
}
