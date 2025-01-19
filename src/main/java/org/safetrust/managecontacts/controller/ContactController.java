package org.safetrust.managecontacts.controller;

import jakarta.validation.Valid;
import org.safetrust.managecontacts.entity.Contact;
import org.safetrust.managecontacts.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
   * @return a ResponseEntity containing: - The Contact object if found (HTTP 200 OK). - An
   *     appropriate HTTP status with a detailed message if the contact does not exist (HTTP 404 Not
   *     Found). - A bad request response if the provided ID is invalid (HTTP 400 Bad Request).
   * @throws IllegalArgumentException if the provided ID is null or invalid.
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getContactById(@PathVariable Long id) {
    return contactService.getContactById(id);
  }

  /**
   * Deletes a specific contact by its unique identifier (ID).
   *
   * @param id the unique identifier of the contact to delete, extracted from the URL path.
   * @return a ResponseEntity with: - HTTP 204 No Content if the contact is successfully deleted. -
   *     HTTP 404 Not Found if the contact with the given ID does not exist. - HTTP 400 Bad Request
   *     if the provided ID is invalid (null or negative).
   * @throws IllegalArgumentException if the provided ID is null or invalid.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteContactById(@PathVariable Long id) {
    return contactService.deleteContactById(id);
  }

  /**
   * Updates an existing contact with the provided details.
   *
   * @param id the unique identifier of the contact to update, extracted from the URL path.
   * @param updatedContact a valid Contact object containing the updated details, provided in the
   *     request body. Validation is applied to ensure the data is valid.
   * @return a ResponseEntity containing: - The updated Contact object if the update is successful
   *     (HTTP 200 OK). - HTTP 404 Not Found if the contact with the given ID does not exist. - HTTP
   *     400 Bad Request if the provided ID is invalid or if validation on the updated contact
   *     fails.
   * @throws IllegalArgumentException if the provided ID is null or invalid.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Contact> updateContact(
      @PathVariable Long id, @RequestBody @Valid Contact updatedContact) {
    return contactService.updateContact(id, updatedContact);
  }

  /**
   * Searches for contacts based on the provided keyword with pagination support.
   *
   * @param searchKeyword the keyword to search for in the contacts' attributes (e.g., name, email,
   *     etc.).
   * @param page the page number to retrieve, defaulting to 0 (the first page) if not specified.
   * @param size the number of contacts to retrieve per page, defaulting to 10 if not specified.
   * @return a Page object containing the matching contacts and pagination metadata. - If matches
   *     are found, it returns the contacts (HTTP 200 OK). - If no matches are found, it returns an
   *     empty page.
   * @throws IllegalArgumentException if the `searchKeyword` is null or empty.
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
