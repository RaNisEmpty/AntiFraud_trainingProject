package antifraud.business.stolencard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardRequest {
    @NotEmpty(message = "Number should not be empty")
    private String number;
}