package account_manager.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDocument {
    private Integer id;
    private String lastName;
    private String firstName;
    private String socialNumber;
    private String city;
    private String street;
    private String houseNumber;
    private String apartment;
}
