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

  /**
   * Retrieves a paginated list of all contacts.
   *
   * @param pageable a Pageable object that specifies the pagination and sorting information, such
   *     as page number, page size, and sort order.
   * @return a Page object containing a subset of Contact entities based on the pagination
   *     parameters.
   */
  public Page<Contact> getAllContacts(Pageable pageable) {
    return contactRepository.findAll(pageable);
  }

  /**
   * Retrieves a specific contact by its unique identifier (ID).
   *
   * @param id the unique identifier of the contact to retrieve.
   * @return a ResponseEntity containing the Contact object if found, or an appropriate HTTP status
   *     (e.g., 404 Not Found) if the contact does not exist.
   */
  public ResponseEntity<Contact> getContactById(Long id) {
    Optional<Contact> contact = contactRepository.findById(id);
    return contact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Retrieves a specific contact by its unique identifier (ID).
   *
   * @param id the unique identifier of the contact to retrieve.
   * @return a ResponseEntity containing the Contact object if found, or an appropriate HTTP status
   *     (e.g., 404 Not Found) if the contact does not exist.
   */
  public ResponseEntity<Void> deleteContactById(Long id) {
    if (contactRepository.existsById(id)) {
      contactRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * Updates an existing contact with the provided details.
   *
   * @param id the unique identifier of the contact to update.
   * @param updatedContact a Contact object containing the updated information.
   * @return a ResponseEntity containing the updated Contact object if the update is successful, or
   *     an appropriate HTTP status (e.g., 404 Not Found if the contact does not exist, or 400 Bad
   *     Request if validation fails).
   */
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

  /**
   * Searches for contacts that match the given keyword in their attributes.
   *
   * @param keyword the search keyword to look for in the contacts' attributes (e.g., name, email,
   *     phone number, etc.).
   * @param pageable a Pageable object that specifies the pagination and sorting information, such
   *     as page number, page size, and sort order.
   * @return a Page object containing a subset of Contact entities that match the keyword, based on
   *     the pagination parameters.
   */
  public Page<Contact> searchContacts(String keyword, Pageable pageable) {
    return contactRepository.findByNameContainingIgnoreCase(keyword, pageable);
  }

  /**
   * Saves a new contact or updates an existing contact in the database.
   *
   * @param contact the Contact object to be saved or updated.
   * @return the saved Contact object, including any generated fields like ID or timestamps.
   */
  public Contact saveContact(Contact contact) {
    return contactRepository.save(contact);
  }
}
