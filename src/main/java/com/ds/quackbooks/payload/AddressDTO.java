package com.ds.quackbooks.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
private Long id;

    private String zipCode;
    
    private String state;
    
    private String city;

    private String street;

    private String externalNumber;

    private String internalNumber;

    private String houseDescription;
}
