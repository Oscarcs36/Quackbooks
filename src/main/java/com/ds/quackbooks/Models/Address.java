package com.ds.quackbooks.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zip_code")
    @NotBlank
    @Size(min = 5, max = 5)
    private String zipCode;
    
    @NotBlank
    @Size(min = 3, max = 15)
    private String state;
    
    @NotBlank
    @Size(min = 3, max = 15)
    private String city;
    
    @NotBlank
    @Size(min = 5, max = 50)
    private String street;
    
    @Column(name = "external_number")
    @NotBlank
    @Size(min = 1, max = 5)
    private String externalNumber;
    
    @Column(name = "internal_number")
    private String internalNumber;

    @Column(name = "house_description")
    @NotBlank
    @Size(min = 5, max = 40)
    private String houseDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(@NotBlank @Size(min = 5, max = 5) String zipCode, @NotBlank @Size(min = 3, max = 15) String state,
            @NotBlank @Size(min = 3, max = 15) String city, @NotBlank @Size(min = 5, max = 50) String street,
            @NotBlank @Size(min = 1, max = 5) String externalNumber, @NotNull String internalNumber,
            @NotBlank @Size(min = 5, max = 40) String houseDescription) {
        this.zipCode = zipCode;
        this.state = state;
        this.city = city;
        this.street = street;
        this.externalNumber = externalNumber;
        this.internalNumber = internalNumber;
        this.houseDescription = houseDescription;
    }

    
}
