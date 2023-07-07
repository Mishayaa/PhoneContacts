package com.example.PhoneContacts.services.impl;


import com.example.PhoneContacts.config.JwtConfig;
import com.example.PhoneContacts.dto.ContactDto;
import com.example.PhoneContacts.entities.Contact;
import com.example.PhoneContacts.entities.Email;
import com.example.PhoneContacts.entities.PhoneNumber;
import com.example.PhoneContacts.entities.User;
import com.example.PhoneContacts.repos.ContactRepository;
import com.example.PhoneContacts.repos.EmailRepository;
import com.example.PhoneContacts.repos.PhoneNumberRepository;
import com.example.PhoneContacts.repos.UserRepository;
import com.example.PhoneContacts.services.IContactService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService implements IContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final EmailRepository emailAddressRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    @Override
    public boolean createContact(ContactDto contactDto, String token) {
        User authenticatedUser = getAuthenticatedUser(token);
        if (authenticatedUser == null) {
            log.error("User not authenticated");
            return false;
        }

        if (contactRepository.existsByNameAndUser(contactDto.getName(), authenticatedUser)) {
            log.error("Contact with same name `{}` already exists", contactDto.getName());
            return false;
        }

        Contact contact = new Contact();
        contact.setUser(authenticatedUser);
        contact.setName(contactDto.getName());

        List<Email> emailAddresses = new ArrayList<>();
        for (String email : contactDto.getEmails()) {
            if (isValidEmail(email)) {
                if (contactRepository.existsByEmailAddressesEmailAndUser(email, authenticatedUser)) {
                    log.error("Contact with email `{}` already exist", email);
                    return false;
                }
                Email emailAddress = new Email();
                emailAddress.setContact(contact);
                emailAddress.setEmail(email);
                emailAddresses.add(emailAddress);
            } else {
                log.error("Invalid email format: {}", email);
                return false;
            }
        }
        contact.setEmailAddresses(emailAddresses);

        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        for (String number : contactDto.getPhones()) {
            if (isValidPhoneNumber(number)) {
                if (contactRepository.existsByPhoneNumbersPhoneNumberAndUser(number, authenticatedUser)) {
                    log.error("Contact with same phone number `{}` already exist", number);
                    return false;
                }
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setContact(contact);
                phoneNumber.setPhoneNumber(number);
                phoneNumbers.add(phoneNumber);
            } else {
                log.error("Invalid phone number format: {}", number);
                return false;
            }
        }
        contact.setPhoneNumbers(phoneNumbers);

        contactRepository.save(contact);
        return true;
    }
    @Override
    public boolean updateContact(Long contactId, ContactDto contactDto, String token) {
        User authenticatedUser = getAuthenticatedUser(token);
        if (authenticatedUser == null) {
            log.error("User not authenticated");
            return false;
        }

        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isEmpty()) {
            log.error("Contact with ID `{}` not found", contactId);
            return false;
        }

        Contact contact = optionalContact.get();

        if (!contact.getUser().equals(authenticatedUser)) {
            log.error("User is not authorized to update this contact");
            return false;
        }

        List<Email> updatedEmailAddresses = new ArrayList<>();
        for (String email : contactDto.getEmails()) {
            if (isValidEmail(email)) {
                if (contactRepository.existsByEmailAddressesEmailAndUser(email, authenticatedUser)) {
                    log.error("Contact with email `{}` already exist", email);
                    return false;
                }
                Email emailAddress = new Email();
                emailAddress.setContact(contact);
                emailAddress.setEmail(email);
                updatedEmailAddresses.add(emailAddress);
            } else {
                log.error("Invalid email format: {}", email);
                return false;
            }
        }
        contact.setEmailAddresses(updatedEmailAddresses);

        List<PhoneNumber> updatedPhoneNumbers = new ArrayList<>();
        for (String number : contactDto.getPhones()) {
            if (isValidPhoneNumber(number)) {
                if (contactRepository.existsByPhoneNumbersPhoneNumberAndUser(number, authenticatedUser)) {
                    log.error("Contact with same phone number `{}` already exist", number);
                    return false;
                }
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setContact(contact);
                phoneNumber.setPhoneNumber(number);
                updatedPhoneNumbers.add(phoneNumber);
            } else {
                log.error("Invalid phone number format: {}", number);
                return false;
            }
        }
        contact.setPhoneNumbers(updatedPhoneNumbers);

        contactRepository.save(contact);
        return true;
    }
    @Override
    public boolean deleteContact(Long contactId, String token) {
        User authenticatedUser = getAuthenticatedUser(token);
        if (authenticatedUser == null) {
            log.error("User not authenticated");
            return false;
        }

        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isEmpty()) {
            log.error("Contact with ID `{}` not found", contactId);
            return false;
        }

        Contact contact = optionalContact.get();

        if (!contact.getUser().equals(authenticatedUser)) {
            log.error("User is not authorized to update this contact");
            return false;
        }

        User user = contact.getUser();
        user.getContacts().remove(contact);

        contactRepository.delete(contact);
        return true;
    }
    @Override
    public boolean renameContact(Long contactId, String newName, String token) {
        User authenticatedUser = getAuthenticatedUser(token);
        if (authenticatedUser == null) {
            log.error("User not authenticated");
            return false;
        }

        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isEmpty()) {
            log.error("Contact with ID `{}` not found", contactId);
            return false;
        }

        Contact contact = optionalContact.get();

        if (!contact.getUser().equals(authenticatedUser)) {
            log.error("User is not authorized to update this contact");
            return false;
        }

        if (contactRepository.existsByNameAndUser(newName, authenticatedUser)) {
            log.error("Contact with same name `{}` already exists", newName);
            return false;
        }

        contact.setName(newName);
        contactRepository.save(contact);
        return true;
    }
    @Override
    public boolean updateEmailAddress(Long emailId, String newEmail, String token) {
        User authenticatedUser = getAuthenticatedUser(token);
        if (authenticatedUser == null) {
            log.error("User not authenticated");
            return false;
        }

        Optional<Email> optionalEmailAddress = emailAddressRepository.findById(emailId);
        if (optionalEmailAddress.isEmpty()) {
            log.error("Email address with ID `{}` not found", emailId);
            return false;
        }

        Email emailAddress = optionalEmailAddress.get();
        Contact contact = emailAddress.getContact();

        if (!contact.getUser().equals(authenticatedUser)) {
            log.error("User is not authorized to update this email address");
            return false;
        }

        if (isValidEmail(newEmail)) {
            if (contactRepository.existsByEmailAddressesEmailAndUser(newEmail, authenticatedUser)) {
                log.error("Contact with email `{}` already exist", newEmail);
                return false;
            } else {
                emailAddress.setEmail(newEmail);
                emailAddressRepository.save(emailAddress);
                return true;
            }
        }

        log.error("Invalid email format: {}", newEmail);
        return false;
    }
    @Override
    public boolean updatePhoneNumber(Long phoneNumberId, String newPhoneNumber, String token) {
        User authenticatedUser = getAuthenticatedUser(token);
        if (authenticatedUser == null) {
            log.error("User not authenticated");
            return false;
        }

        Optional<PhoneNumber> optionalPhoneNumber = phoneNumberRepository.findById(phoneNumberId);
        if (optionalPhoneNumber.isEmpty()) {
            log.error("Phone number with ID `{}` not found", phoneNumberId);
            return false;
        }

        PhoneNumber phoneNumber = optionalPhoneNumber.get();

        Contact contact = phoneNumber.getContact();

        if (!contact.getUser().equals(authenticatedUser)) {
            log.error("User is not authorized to update this phone number");
            return false;
        }

        if (!isValidPhoneNumber(newPhoneNumber)) {
            if (contactRepository.existsByPhoneNumbersPhoneNumberAndUser(newPhoneNumber, authenticatedUser)) {
                log.error("Contact with same phone number `{}` already exist", newPhoneNumber);
                return false;
            } else {
                phoneNumber.setPhoneNumber(newPhoneNumber);
                phoneNumberRepository.save(phoneNumber);
                return true;
            }
        }

        log.error("Invalid phone number format: {}", newPhoneNumber);
        return false;
    }


    public List<ContactDto> getContactList(String token) {
        User authenticatedUser = getAuthenticatedUser(token);
        if (authenticatedUser == null) {
            log.error("User not authenticated");
            return Collections.emptyList();
        }

        List<Contact> contactList = contactRepository.findByUser(authenticatedUser);

        return contactList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ContactDto convertToDto(Contact contact) {
        List<String> emails = contact.getEmailAddresses().stream()
                .map(Email::getEmail)
                .collect(Collectors.toList());

        List<String> phones = contact.getPhoneNumbers().stream()
                .map(PhoneNumber::getPhoneNumber)
                .collect(Collectors.toList());

        return new ContactDto(contact.getName(), emails, phones);
    }

    private User getAuthenticatedUser(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JwtConfig.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        Long userId = claims.get("userId", Long.class);
        if (userId != null) {
            return userRepository.findById(userId).orElse(null);
        }
        return null;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
        return phoneNumber.matches(phoneRegex);
    }
}