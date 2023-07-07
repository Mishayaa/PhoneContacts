package com.example.PhoneContacts.repos;

import com.example.PhoneContacts.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByTitle(String title);
}