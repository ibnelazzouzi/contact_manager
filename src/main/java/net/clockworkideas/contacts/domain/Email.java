package net.clockworkideas.contacts.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Email {
    private String address;
    private Boolean preferred;
    private Boolean confirmed;

}
