package antifraud.business.user.userdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter

public class UserResponse implements Comparable<UserResponse> {

    private long id;

    private String name;

    private String username;

    private String role;

    @Override
    public int compareTo(UserResponse other) {
        return Long.compare(this.id, other.id);
    }
}
