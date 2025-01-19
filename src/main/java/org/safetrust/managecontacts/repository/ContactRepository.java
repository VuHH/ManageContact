package org.safetrust.managecontacts.repository;

import org.safetrust.managecontacts.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
  /**
   * Finds contacts whose names contain the specified substring, ignoring case.
   *
   * @param name the substring to search for in the contact names. This is case-insensitive.
   * @param pageable a Pageable object specifying the pagination and sorting details, such as page
   *     number, page size, and sort order.
   * @return a Page object containing the contacts that match the search criteria, along with
   *     pagination metadata.
   */
  Page<Contact> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
