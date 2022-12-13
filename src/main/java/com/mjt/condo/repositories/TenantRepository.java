package com.mjt.condo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mjt.condo.models.Tenant;

@Repository
public interface TenantRepository extends CrudRepository <Tenant, Long>{
	
	List<Tenant> findAll();
	Tenant findTenantById(Long id);
}
