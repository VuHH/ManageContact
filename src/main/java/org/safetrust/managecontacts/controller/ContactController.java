package org.safetrust.managecontacts.controller;

import jakarta.validation.Valid;
import org.safetrust.managecontacts.entity.Contact;
import org.safetrust.managecontacts.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
  private final ContactService contactService;

  @Autowired
  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @GetMapping
  public Page<Contact> getAllContacts(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return contactService.getAllContacts(pageable);
  }

  @PostMapping
  public Contact saveContact(@RequestBody @Valid Contact contact) {
    return contactService.saveContact(contact);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
    return contactService.getContactById(id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteContactById(@PathVariable Long id) {
    return contactService.deleteContactById(id);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Contact> updateContact(
      @PathVariable Long id, @RequestBody @Valid Contact updatedContact) {
    return contactService.updateContact(id, updatedContact);
  }

  @GetMapping("/search")
  public Page<Contact> searchContacts(
      @RequestParam String searchKeyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return contactService.searchContacts(searchKeyword, pageable);
  }
}
