package account_manager.service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Integer id;
    private String number;
    private String currencyCode;
    private String type;
    private Double balance;
    private String openDate;
    private Integer ownerId;
}
