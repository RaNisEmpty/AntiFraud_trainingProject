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
public class UserRoleRequest {
    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Role should not be empty")
    private String role;

}
