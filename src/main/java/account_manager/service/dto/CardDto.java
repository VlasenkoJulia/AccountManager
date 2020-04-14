package account_manager.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Integer id;
    private String number;
    private List<Integer> accountIds;
}
