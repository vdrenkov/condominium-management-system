package com.mjt.condo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mjt.condo.models.Privilege;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, String> {}
