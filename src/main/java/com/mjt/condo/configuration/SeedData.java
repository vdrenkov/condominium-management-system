package com.mjt.condo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mjt.condo.models.Apartment;
import com.mjt.condo.models.Privilege;
import com.mjt.condo.models.Tenant;
import com.mjt.condo.models.User;
import com.mjt.condo.repositories.PrivilegeRepository;
import com.mjt.condo.services.ApartmentService;
import com.mjt.condo.services.TenantService;
import com.mjt.condo.services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private ApartmentService apartmentService;
    
    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Apartment> apartments = apartmentService.findAllApartments();
        List<Tenant> tenants = tenantService.findAllTenants();

        if (apartments.size() == 0 && tenants.size()==0) {

            Privilege user = new Privilege();
            user.setName("ROLE_USER");
            privilegeRepository.save(user);

            Privilege admin = new Privilege();
            admin.setName("ROLE_ADMIN");
            privilegeRepository.save(admin);

            User user1 = new User();
            User user2 = new User();

            user1.setUsername("user");
            user1.setPassword("user");
            Set<Privilege> privileges1 = new HashSet<>();
            privilegeRepository.findById("ROLE_USER").ifPresent(privileges1::add);
            user1.setPrivileges(privileges1);


            user2.setUsername("admin");
            user2.setPassword("admin");
            Set<Privilege> privileges2 = new HashSet<>();
            privilegeRepository.findById("ROLE_ADMIN").ifPresent(privileges2::add);
            privilegeRepository.findById("ROLE_USER").ifPresent(privileges2::add);
            user2.setPrivileges(privileges2);

            userService.createUser(user1);
            userService.createUser(user2);
        }
    }

}