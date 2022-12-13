package com.mjt.condo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mjt.condo.exceptions.TenantNotFoundException;
import com.mjt.condo.models.Tenant;
import com.mjt.condo.services.TenantService;

import lombok.SneakyThrows;

@RestController
@RequestMapping("/tenants")
public class TenantController{
	
    @Autowired
    private TenantService tenantService;

    @GetMapping()
    public ResponseEntity<List<Tenant>> getAllTenants(){
            return ResponseEntity.ok(tenantService.findAllTenants());
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable Long id) throws TenantNotFoundException {
    	Tenant tenant = tenantService.findTenantById(id);
        return ResponseEntity.ok(tenant);
    }

    @SneakyThrows
    @PostMapping()
    public ResponseEntity<Tenant> addTenant(@RequestBody Tenant tenant) {
        tenantService.saveTenant(tenant);
        return new ResponseEntity<Tenant>(tenant, HttpStatus.CREATED);
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<Tenant> updateTenant(@PathVariable Long id, @RequestBody Tenant tenant) throws TenantNotFoundException {
        Tenant updatedTenant = tenantService.updateTenant(id, tenant);
        return ResponseEntity.ok(updatedTenant);
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteTenant(@PathVariable Long id) throws TenantNotFoundException {
    	tenantService.deleteTenantById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(value = TenantNotFoundException.class)
    private ResponseEntity<String> handleTenantNotFoundException(TenantNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    }