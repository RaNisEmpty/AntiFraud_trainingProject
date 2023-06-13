package antifraud.business.susip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IpRequest {
    @NotEmpty(message = "Ip should not be empty")
    private String ip;
}
