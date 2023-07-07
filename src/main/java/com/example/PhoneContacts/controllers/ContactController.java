package com.example.PhoneContacts.controllers;


import com.example.PhoneContacts.dto.ContactDto;
import com.example.PhoneContacts.services.impl.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import org.springframework.http.HttpStatus;


import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/create")
    public ResponseEntity<String> createContact(@RequestBody ContactDto contactDto, @RequestHeader("Authorization") String authorizationHeader) {

        String token = extractToken(authorizationHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (contactService. createContact(contactDto, token)) {
            return new ResponseEntity<>("Contact created successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to create contact!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/update/{contactId}")
    public ResponseEntity<String> updateContact(@PathVariable Long contactId, @RequestBody ContactDto contactDto, @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (contactService.updateContact(contactId, contactDto, token)) {
            return new ResponseEntity<>("Contact updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to update contact!", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/rename/{contactId}")
    public ResponseEntity<String> renameContact(@PathVariable Long contactId, @RequestBody Map<String, String> newNameMap, @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newName = newNameMap.get("newName");
        if (newName == null || newName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New name is required");
        }

        if (contactService.renameContact(contactId, newName, token)) {
            return ResponseEntity.ok("Contact renamed successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to rename contact");
    }

    @PatchMapping("/update-email/{emailId}")
    public ResponseEntity<String> updateEmailAddress(@PathVariable Long emailId, @RequestBody Map<String, String> newEmailMap, @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newEmail = newEmailMap.get("newEmail");
        if (newEmail == null || newEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New email is required");
        }

        if (contactService.updateEmailAddress(emailId, newEmail, token)) {
            return ResponseEntity.ok("Email updated successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update email");
    }

    @PatchMapping("/update-phone-number/{phoneNumberId}")
    public ResponseEntity<String> updatePhoneNumber(@PathVariable Long phoneNumberId, @RequestBody Map<String, String> newPhoneNumberMap, @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newPhoneNumber = newPhoneNumberMap.get("newPhoneNumber");
        if (newPhoneNumber == null || newPhoneNumber.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New phone number is required");
        }

        if (contactService.updatePhoneNumber(phoneNumberId, newPhoneNumber, token)) {
            return ResponseEntity.ok("Phone number updated successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update phone number");
    }

    @DeleteMapping("/delete/{contactId}")
    public ResponseEntity<String> deleteContact(@PathVariable Long contactId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (contactService.deleteContact(contactId, token)) {
            return new ResponseEntity<>("Contact deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to delete contact!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ContactDto>> getContactList(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ContactDto> contactList = contactService.getContactList(token);

        if (contactList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(contactList);
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}