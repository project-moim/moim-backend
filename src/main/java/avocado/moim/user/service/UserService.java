package avocado.moim.user.service;

import avocado.moim.user.dto.UserDto;
import avocado.moim.user.entity.User;

public interface UserService {

    Long join(User user);

    UserDto confirmUser(String email, String encryptedPassword);

    User findByEmail(String email);

    void saveLoginInfo(String email, String remoteAddr, long issueTime, String refreshToken);

    void deleteExpireToken();
}
