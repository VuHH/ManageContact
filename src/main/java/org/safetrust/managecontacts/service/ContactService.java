package org.safetrust.managecontacts.service;

import org.safetrust.managecontacts.entity.Contact;
import org.safetrust.managecontacts.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
  private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
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
  public ResponseEntity<?> getContactById(Long id) {
    if (id == null || id <= 0) {
      return ResponseEntity.badRequest().body("Invalid ID: " + id);
    }

    return contactRepository
        .findById(id)
        .map(
            contact -> {
              logger.info("Found contact with ID: {}", id);
              return ResponseEntity.ok(contact);
            })
        .orElseGet(
            () -> {
              logger.warn("Contact not found with ID: {}", id);
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .header("Message", "Contact with ID " + id + " not found")
                  .build();
            });
  }

  /**
   * Retrieves a specific contact by its unique identifier (ID).
   *
   * @param id the unique identifier of the contact to retrieve.
   * @return a ResponseEntity containing the Contact object if found, or an appropriate HTTP status
   *     (e.g., 404 Not Found) if the contact does not exist.
   */
  public ResponseEntity<Void> deleteContactById(Long id) {
    if (id == null || id <= 0) {
      return ResponseEntity.badRequest().header("Message", "Invalid ID: " + id).build();
    }

    try {
      contactRepository.deleteById(id);
      logger.info("Deleted contact with ID: {}", id);
      return ResponseEntity.noContent().build();
    } catch (EmptyResultDataAccessException ex) {
      logger.warn("Contact with ID {} not found for deletion", id);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .header("Message", "Contact with ID " + id + " not found")
          .build();
    } catch (DataAccessException ex) {
      logger.error("Database error while deleting contact with ID: {}", id, ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .header("Message", "Database error occurred: " + ex.getMessage())
          .build();
    }
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
    if (id == null || id <= 0) {
      return ResponseEntity.badRequest().header("Message", "Invalid ID: " + id).build();
    }

    if (updatedContact == null) {
      return ResponseEntity.badRequest()
          .header("Message", "Updated contact details cannot be null")
          .build();
    }

    try {
      return contactRepository
          .findById(id)
          .map(
              existingContact -> {
                if (updatedContact.getName() != null) {
                  existingContact.setName(updatedContact.getName());
                }
                if (updatedContact.getEmail() != null) {
                  existingContact.setEmail(updatedContact.getEmail());
                }
                if (updatedContact.getTelephoneNumber() != null) {
                  existingContact.setTelephoneNumber(updatedContact.getTelephoneNumber());
                }
                if (updatedContact.getPostalAddress() != null) {
                  existingContact.setPostalAddress(updatedContact.getPostalAddress());
                }
                Contact savedContact = contactRepository.save(existingContact);
                logger.info("Updated contact with ID: {}", id);
                return ResponseEntity.ok(savedContact);
              })
          .orElseGet(
              () -> {
                logger.warn("Contact with ID {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Message", "Contact with ID " + id + " not found")
                    .build();
              });
    } catch (DataAccessException ex) {
      logger.error("Database error while updating contact with ID: {}", id, ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .header("Message", "Database error occurred: " + ex.getMessage())
          .build();
    }
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
    if (keyword == null || keyword.trim().isEmpty()) {
      throw new IllegalArgumentException("Keyword must not be null or empty");
    }

    if (pageable == null) {
      throw new IllegalArgumentException("Pageable must not be null");
    }

    try {
      Page<Contact> result =
          contactRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);

      if (result.isEmpty()) {
        logger.warn("No contacts found for keyword: {}", keyword);
      } else {
        logger.info("Found {} contacts for keyword: {}", result.getTotalElements(), keyword);
      }

      return result;
    } catch (DataAccessException ex) {
      logger.error(
          "Database error occurred while searching for contacts with keyword: {}", keyword, ex);
      throw new RuntimeException("Database error occurred. Please try again later.", ex);
    }
  }

  /**
   * Saves a new contact or updates an existing contact in the database.
   *
   * @param contact the Contact object to be saved or updated.
   * @return the saved Contact object, including any generated fields like ID or timestamps.
   */
  public Contact saveContact(Contact contact) {
    if (contact == null) {
      throw new IllegalArgumentException("Contact cannot be null");
    }

    try {
      Contact savedContact = contactRepository.save(contact);
      logger.info("Contact saved successfully with ID: {}", savedContact.getId());
      return savedContact;
    } catch (DataAccessException ex) {
      logger.error("Failed to save contact: {}", contact, ex);
      throw new RuntimeException("Unable to save contact. Please try again later.", ex);
    }
  }
}
