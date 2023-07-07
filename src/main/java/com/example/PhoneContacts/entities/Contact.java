package com.example.PhoneContacts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.*;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "contact")
    private List<Email> emailAddresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "contact")
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    //needed for tests
    public List<String> getEmails() {
        List<String> emailList = new ArrayList<>();
        for (Email emailAddress : emailAddresses) {
            emailList.add(emailAddress.getEmail());
        }
        return emailList;
    }
    //needed for tests
    public List<String> getPhoneNumbersList() {
        List<String> phoneNumberList = new ArrayList<>();
        for (PhoneNumber phoneNumber : phoneNumbers) {
            phoneNumberList.add(phoneNumber.getPhoneNumber());
        }
        return phoneNumberList;
    }
}