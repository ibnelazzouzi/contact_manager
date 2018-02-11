package net.clockworkideas.contacts;

import com.github.javafaker.Faker;
import net.clockworkideas.contacts.domain.Address;
import net.clockworkideas.contacts.domain.Contact;
import net.clockworkideas.contacts.domain.Email;
import net.clockworkideas.contacts.domain.PhoneNumber;
import net.clockworkideas.contacts.repository.ContactRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactIntegrationTests
{
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ContactRepository repository;

    @Test
    public void testAll()
    {
        for(int count=0;count<10;count++) {
            Contact contact = createContact();
            webTestClient.post().uri("/contacts")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .body(Mono.just(contact), Contact.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .expectBody()
                    .jsonPath("$.id").isNotEmpty()
                    .jsonPath("$.firstName").isEqualTo(contact.getFirstName())
                    .jsonPath("$.lastName").isEqualTo(contact.getLastName());
                    //TODO:FINISH THIS OUT
        }
        //Test get
        webTestClient.get().uri("/contacts")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Contact.class);
    }

    @Test
    public void testGetSingleContact() {
        Contact contact = repository.save(createContact()).block();

        webTestClient.get()
                .uri("/contacts/{id}", Collections.singletonMap("id", contact.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateContact() {
        Contact contact= repository.save(createContact()).block();

       Contact newContact=createContact();

        webTestClient.put()
                .uri("/contacts/{id}", Collections.singletonMap("id", contact.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newContact), Contact.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.firstName").isEqualTo(newContact.getFirstName());
    }

    @Test
    public void testDeleteContact() {
        Contact contact = repository.save(createContact()).block();

        webTestClient.delete()
                .uri("/contacts/{id}", Collections.singletonMap("id",  contact.getId()))
                .exchange()
                .expectStatus().isOk();
    }



    protected Contact createContact()
    {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM=dd");
        Faker faker = new Faker();
        Contact contact=new Contact();
        contact.setTitle(faker.name().prefix());
        contact.setFirstName(faker.name().firstName());
        contact.setMiddleName(faker.name().firstName());
        contact.setLastName(faker.name().lastName());
        contact.setSuffix(faker.name().suffix());
        Date input=faker.date().birthday();
        LocalDate dob = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        contact.setBirthDate(dob);
        Email email=new Email();
        email.setAddress(faker.name().firstName()+"@gmail.com");
        List<Email> emails= Arrays.asList(email);
        contact.setEmails(emails);
        Address address=new Address();
        address.setAddressType("Home");
        address.setStreet1(faker.address().streetAddress());
        address.setStreet2("Apt "+faker.address().buildingNumber());
        address.setCity(faker.address().city());
        address.setRegion(faker.address().stateAbbr());
        address.setPostalCode(faker.address().zipCode());
        address.setCountry("USA");
        List<Address>addresses=Arrays.asList(address);
        contact.setAddresses(addresses);
        PhoneNumber phoneNumber=new PhoneNumber();
        phoneNumber.setPhoneType("Home");
        phoneNumber.setNumber(faker.phoneNumber().phoneNumber());
        List<PhoneNumber>phoneNumbers=Arrays.asList(phoneNumber);
        contact.setPhoneNumbers(phoneNumbers);
        return contact;
    }
}
