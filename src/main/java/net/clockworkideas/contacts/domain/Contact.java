package net.clockworkideas.contacts.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sun.tools.tree.ThisExpression;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Document(collection = "contact")
public class Contact {

    @Id
    private String id;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private LocalDate birthDate;
    private String company;
    private List<Email> emails;
    private List<Address> addresses;
    private List<PhoneNumber> phoneNumbers;
    private String notes;

    @Override
    public String toString() {
        return String.format("%s, %s",this.getLastName(), this.getFirstName());
    }
}
