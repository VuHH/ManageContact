package org.safetrust.managecontacts;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@OpenAPIDefinition(
    info =
        @Info(
            title = "Contact Management API",
            version = "1.0",
            description =
                "API for managing contacts including create, update, delete, and search operations"),
    servers = @Server(url = "http://localhost:8080", description = "Local server"))
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ManageContactsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ManageContactsApplication.class, args);
  }
}
