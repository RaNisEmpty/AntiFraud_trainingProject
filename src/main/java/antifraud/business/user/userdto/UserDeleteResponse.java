package antifraud.business.user.userdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Getter

public class UserDeleteResponse {
    private String username;
    private String status;

}