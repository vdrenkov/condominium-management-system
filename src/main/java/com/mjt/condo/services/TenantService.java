package com.mjt.condo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.mjt.condo.exceptions.TenantNotFoundException;
import com.mjt.condo.models.Tenant;
import com.mjt.condo.repositories.TenantRepository;

@Service
public class TenantService {

	@Autowired
	private TenantRepository tenantRepo;

	public List<Tenant> findAllTenants() {
		return tenantRepo.findAll();
	}

	public Tenant findTenantById(Long id) throws TenantNotFoundException {
		return tenantRepo.findById(id)
				.orElseThrow(() -> new TenantNotFoundException("The tenant you are looking for does not exist."));
	}

	public Tenant saveTenant(Tenant tenant) {
		return tenantRepo.save(tenant);
	}

	public Tenant updateTenant(Long id, Tenant updatedTenant) throws TenantNotFoundException {
	Tenant oldTenant=tenantRepo.findById(id)
				.orElseThrow(() -> new TenantNotFoundException("The tenant you are trying to modify does not exist."));

		tenantRepo.delete(oldTenant);
		tenantRepo.save(updatedTenant);
		return updatedTenant;
	}

	public void deleteTenantById(Long id) throws TenantNotFoundException {
		try {
			tenantRepo.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new TenantNotFoundException("The tenant you are trying to delete does not exist.");
		}
	}
}