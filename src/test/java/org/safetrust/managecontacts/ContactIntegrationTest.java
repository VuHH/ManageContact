package org.safetrust.managecontacts;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.safetrust.managecontacts.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ContactIntegrationTest {
  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void testCreateContact() {
    Contact contact = new Contact();
    contact.setName("John Doe");
    contact.setEmail("john.doe@example.com");
    contact.setTelephoneNumber("+1234567890");
    contact.setPostalAddress("123 Test St, Test City, TC 12345");

    ResponseEntity<Contact> response =
        restTemplate.postForEntity("/api/contact", contact, Contact.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getName()).isEqualTo("John Doe");
  }

  @Test
  public void testGetContacts() {
    Contact contact1 = new Contact();
    contact1.setName("Alice Johnson");
    contact1.setEmail("alice.johnson@example.com");
    contact1.setTelephoneNumber("+1122334455");
    contact1.setPostalAddress("111 Alice Lane, Wonderland, WL 12345");

    Contact contact2 = new Contact();
    contact2.setName("Bob Johnson");
    contact2.setEmail("bob.johnson@example.com");
    contact2.setTelephoneNumber("+5566778899");
    contact2.setPostalAddress("222 Bob St, Nowhere, NW 67890");

    restTemplate.postForEntity("/api/contact", contact1, Contact.class);
    restTemplate.postForEntity("/api/contact", contact2, Contact.class);

    ResponseEntity<String> response = restTemplate.exchange(
            "/api/contact",
            HttpMethod.GET,
            null,
            String.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    String responseBody = response.getBody();
    List<?> content = JsonPath.parse(responseBody).read("$.content");
    assertThat(content).isNotNull().isInstanceOf(List.class);
    assertThat(content.size()).isGreaterThanOrEqualTo(2);
  }

  @Test
  public void testGetContactById() {
    Contact contact = new Contact();
    contact.setName("Jane Doe");
    contact.setEmail("jane.doe@example.com");
    contact.setTelephoneNumber("+1987654321");
    contact.setPostalAddress("456 Example Blvd, Sample City, SC 67890");

    ResponseEntity<Contact> createResponse =
        restTemplate.postForEntity("/api/contact", contact, Contact.class);
    Long id = createResponse.getBody().getId();

    ResponseEntity<Contact> getResponse =
        restTemplate.getForEntity("/api/contact/" + id, Contact.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getResponse.getBody()).isNotNull();
    assertThat(getResponse.getBody().getName()).isEqualTo("Jane Doe");
  }

  @Test
  public void testDeleteContact() {
    Contact contact = new Contact();
    contact.setName("Mark Smith");
    contact.setEmail("mark.smith@example.com");
    contact.setTelephoneNumber("+1234509876");
    contact.setPostalAddress("789 Another Rd, Another City, AC 13579");

    ResponseEntity<Contact> createResponse =
        restTemplate.postForEntity("/api/contact", contact, Contact.class);
    Long id = createResponse.getBody().getId();

    restTemplate.delete("/api/contact/" + id);

    ResponseEntity<Contact> getResponse =
        restTemplate.getForEntity("/api/contact/" + id, Contact.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void testSearchContacts() {
    Contact contact1 = new Contact();
    contact1.setName("Alice Johnson");
    contact1.setEmail("alice.johnson@example.com");
    contact1.setTelephoneNumber("+1122334455");
    contact1.setPostalAddress("111 Alice Lane, Wonderland, WL 12345");

    Contact contact2 = new Contact();
    contact2.setName("Bob Johnson");
    contact2.setEmail("bob.johnson@example.com");
    contact2.setTelephoneNumber("+5566778899");
    contact2.setPostalAddress("222 Bob St, Nowhere, NW 67890");

    restTemplate.postForEntity("/api/contact", contact1, Contact.class);
    restTemplate.postForEntity("/api/contact", contact2, Contact.class);

    Map<String, String> params = new HashMap<>();
    params.put("keyword", "Johnson");

    ResponseEntity<String> response =
        restTemplate.getForEntity(
            "/api/contact/search?searchKeyword={keyword}", String.class, params);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("Alice Johnson", "Bob Johnson");
  }
}
