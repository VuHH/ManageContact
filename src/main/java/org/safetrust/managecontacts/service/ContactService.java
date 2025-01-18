package org.safetrust.managecontacts.service;

import java.util.Optional;
import org.safetrust.managecontacts.entity.Contact;
import org.safetrust.managecontacts.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
  private final ContactRepository contactRepository;

  @Autowired
  public ContactService(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }

  public Page<Contact> getAllContacts(Pageable pageable) {
    return contactRepository.findAll(pageable);
  }

  public ResponseEntity<Contact> getContactById(Long id) {
    Optional<Contact> contact = contactRepository.findById(id);
    return contact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  public ResponseEntity<Void> deleteContactById(Long id) {
    if (contactRepository.existsById(id)) {
      contactRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  public ResponseEntity<Contact> updateContact(Long id, Contact updatedContact) {
    return contactRepository
        .findById(id)
        .map(
            contact -> {
              contact.setName(updatedContact.getName());
              contact.setEmail(updatedContact.getEmail());
              contact.setTelephoneNumber(updatedContact.getTelephoneNumber());
              contact.setPostalAddress(updatedContact.getPostalAddress());
              contactRepository.save(contact);
              return ResponseEntity.ok(contact);
            })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  public Page<Contact> searchContacts(String keyword, Pageable pageable) {
    return contactRepository.findByNameContainingIgnoreCase(keyword, pageable);
  }

  public Contact saveContact(Contact contact) {
    return contactRepository.save(contact);
  }
}
