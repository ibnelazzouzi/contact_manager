package net.clockworkideas.contacts.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumber {
    String number;
    String countryCode;
    String prefix;
    String extension;
    String phoneType;
    Boolean preferred;
    Boolean confirmed;
}
