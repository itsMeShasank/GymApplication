package com.epam.gymservice.cucumber;

import com.epam.gymservice.dto.CredentialsDetails;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerTests {

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<HttpStatus> response;
    CredentialsDetails credentialsDetails;

    @Given(": Execute userLogin api")
    public void execute_user_login_api() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcmFrYXNoZ2FkaXBpbGxpODk5OSIsImlhdCI6MTY5MjU5MzA1MSwiZXhwIjoxNjkyNTk0ODUxfQ.dAIjEzqB-X5NSnc98-71-G1N7h8WZ9zkr93WFT7BR00");

        credentialsDetails = new CredentialsDetails();
        credentialsDetails.setUserName("prakashgadipilli8999");
        credentialsDetails.setPassword("$2a$10$Yn.hWIclvuj745y/7PhpYeia2LLbBuGezBrmgx1dThShmOFHcZ7LG");

        HttpEntity<CredentialsDetails> requestEntity = new HttpEntity<>(credentialsDetails, headers);

        response = restTemplate.exchange("http://localhost:7000/gym-service/login",HttpMethod.POST,requestEntity,HttpStatus.class);
    }
    @Then(": validate method invoked to verify given credentials and user verified and returned status OK")
    public void validate_method_invoked_to_verify_given_credentials_and_user_verified_and_returned_status_ok() {
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

}
