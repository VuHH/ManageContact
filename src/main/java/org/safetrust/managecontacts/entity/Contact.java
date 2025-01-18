package org.safetrust.managecontacts.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Contact")
public class Contact {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  @NotBlank(message = "Name is required")
  @Size(max = 100, message = "Name must be less than 100 characters")
  private String name;

  @Column(name = "email")
  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @Column(name = "address")
  private String address;

  @Column(name = "telephone_number")
  @NotBlank(message = "Telephone number is required")
  @Pattern(regexp = "\\+?[0-9 .()-]{7,15}", message = "Telephone number is invalid")
  private String telephoneNumber;

  @Column(name = "postal_address")
  @NotBlank(message = "Postal address is required")
  @Size(max = 255, message = "Postal address must be less than 255 characters")
  private String postalAddress;

  public Contact(
      Long id,
      String name,
      String email,
      String address,
      String telephoneNumber,
      String postalAddress) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.address = address;
    this.telephoneNumber = telephoneNumber;
    this.postalAddress = postalAddress;
  }

  public Contact() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public String getPostalAddress() {
    return postalAddress;
  }

  public void setPostalAddress(String postalAddress) {
    this.postalAddress = postalAddress;
  }
}
