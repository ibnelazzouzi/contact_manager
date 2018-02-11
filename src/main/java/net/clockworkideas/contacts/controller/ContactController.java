package net.clockworkideas.contacts.controller;

import net.clockworkideas.contacts.domain.Contact;
import net.clockworkideas.contacts.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class ContactController {

    @Autowired
    ContactRepository repository;

    @GetMapping("/contacts")
    public Flux<Contact> getAllContacts()
    {
        return repository.findAll();
    }

    @PostMapping("/contacts")
    public Mono<Contact> createContact(@Valid @RequestBody Contact contact)
    {
        return repository.save(contact);
    }

    @GetMapping("/contacts/{id}")
    public Mono<ResponseEntity<Contact>> getContactById(@PathVariable(value = "id") String contactId) {
        return repository.findById(contactId)
                .map(savedContact -> ResponseEntity.ok(savedContact))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PutMapping("/contacts/{id}")
    public Mono<ResponseEntity<Contact>> updateContact(@PathVariable(value = "id") String contactId,
                                                   @Valid @RequestBody Contact contact) {
        return repository.findById(contactId)
                .flatMap(existingContact -> {
                    existingContact=contact;
                    return repository.save(existingContact);
                })
                .map(updatedContact -> new ResponseEntity<>(updatedContact, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/contacts/{id}")
    public Mono<ResponseEntity<Void>> deleteContact(@PathVariable(value = "id") String contactId) {

        return repository.findById(contactId)
                .flatMap(existingContact ->
                        repository.delete(existingContact)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/stream/contacts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Contact> streamAllContacts() {
        return repository.findAll();
    }
}
