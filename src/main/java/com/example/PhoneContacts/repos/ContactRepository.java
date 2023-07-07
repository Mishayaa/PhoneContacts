package com.example.PhoneContacts.repos;

import com.example.PhoneContacts.entities.Contact;
import com.example.PhoneContacts.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByNameAndUser(String name, User user);
    boolean existsByEmailAddressesEmailAndUser(String email, User user);
    boolean existsByPhoneNumbersPhoneNumberAndUser(String phoneNumber, User user);
    List<Contact> findByUser(User user);
}
