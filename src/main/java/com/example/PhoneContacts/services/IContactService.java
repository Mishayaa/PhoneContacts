package com.example.PhoneContacts.services;

import com.example.PhoneContacts.dto.ContactDto;

public interface IContactService {
    boolean createContact(ContactDto contactDto, String token);

    public boolean updateContact(Long contactId, ContactDto contactDto, String token);

    boolean deleteContact(Long contactId, String token);

    boolean renameContact(Long contactId, String newName, String token);

    boolean updatePhoneNumber(Long phoneNumberId, String newPhoneNumber, String token);

    boolean updateEmailAddress(Long emailId, String newEmail, String token);
}