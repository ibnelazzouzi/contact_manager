package net.clockworkideas.contacts.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String street1;
    private String street2;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private String addressType;
    private Boolean preferred;
}
