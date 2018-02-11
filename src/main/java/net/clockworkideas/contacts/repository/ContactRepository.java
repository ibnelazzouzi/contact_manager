package net.clockworkideas.contacts.repository;

import net.clockworkideas.contacts.domain.Contact;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends ReactiveMongoRepository<Contact,String> {

}
