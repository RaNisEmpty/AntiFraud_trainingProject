package antifraud.business.user.userdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserStatusRequest {

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Status operation should not be empty")
    private String operation;
}
