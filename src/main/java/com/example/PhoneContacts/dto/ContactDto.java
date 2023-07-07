package com.example.PhoneContacts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    private String name;
    private List<String> emails;
    private List<String> phones;
}