package avocado.moim.user.model;

import avocado.moim.util.Role;
import lombok.Data;

@Data
public class LoginRequestModel {

    private String email;
    private String password;
    private Role role;
    private int status;
    private String accessToken;
}
