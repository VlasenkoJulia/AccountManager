package account_manager.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Integer id;
    private String lastName;
    private String firstName;
    private String socialNumber;
    private String city;
    private String street;
    private String houseNumber;
    private String apartment;
}
