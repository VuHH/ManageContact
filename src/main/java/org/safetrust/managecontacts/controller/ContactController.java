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

  /**
   * Retrieves a paginated list of all contacts.
   *
   * @param page the page number to retrieve, defaulting to 0 (the first page) if not specified.
   * @param size the number of contacts per page, defaulting to 10 if not specified.
   * @return a Page object containing a subset of Contact entities based on the specified pagination
   *     parameters.
   */
  @GetMapping
  public Page<Contact> getAllContacts(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return contactService.getAllContacts(pageable);
  }

  /**
   * Saves a new contact or updates an existing contact in the database.
   *
   * @param contact the Contact object received in the request body, validated to ensure it meets
   *     the constraints. This object should contain the necessary fields like name, email, and
   *     phone number.
   * @return the saved Contact object, including any auto-generated fields such as ID or timestamps.
   */
  @PostMapping
  public Contact saveContact(@RequestBody @Valid Contact contact) {
    return contactService.saveContact(contact);
  }

  /**
   * Retrieves a specific contact by its unique identifier (ID).
   *
   * @param id the unique identifier of the contact to retrieve, extracted from the URL path.
   * @return a ResponseEntity containing the Contact object if found, or an appropriate HTTP status
   *     (e.g., 404 Not Found) if the contact does not exist.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
    return contactService.getContactById(id);
  }

  /**
   * Deletes a specific contact by its unique identifier (ID).
   *
   * @param id the unique identifier of the contact to delete, extracted from the URL path.
   * @return a ResponseEntity with an appropriate HTTP status: - 204 No Content if the contact is
   *     successfully deleted. - 404 Not Found if the contact does not exist.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteContactById(@PathVariable Long id) {
    return contactService.deleteContactById(id);
  }

  /**
   * Updates an existing contact with the provided details.
   *
   * @param id the unique identifier of the contact to update, extracted from the URL path.
   * @param updatedContact a Contact object containing the updated details for the contact.
   * @return a ResponseEntity containing the updated Contact object if the update is successful, or
   *     an appropriate HTTP status: - 404 Not Found if the contact does not exist. - 400 Bad
   *     Request if the updated data fails validation.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Contact> updateContact(
      @PathVariable Long id, @RequestBody @Valid Contact updatedContact) {
    return contactService.updateContact(id, updatedContact);
  }

  /**
   * Searches for contacts that match the given keyword across specific fields.
   *
   * @param searchKeyword the keyword to search for in contact attributes (e.g., name, email, etc.).
   * @param page the page number to retrieve, defaulting to 0 (the first page) if not specified.
   * @param size the number of contacts per page, defaulting to 10 if not specified.
   * @return a Page object containing the matching contacts based on the search criteria and
   *     pagination parameters.
   */
  @GetMapping("/search")
  public Page<Contact> searchContacts(
      @RequestParam String searchKeyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return contactService.searchContacts(searchKeyword, pageable);
  }
}
