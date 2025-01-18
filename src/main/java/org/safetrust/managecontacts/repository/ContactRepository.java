package org.safetrust.managecontacts.repository;

import org.safetrust.managecontacts.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
  Page<Contact> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
