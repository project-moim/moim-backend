package avocado.moim.user.repository;

import avocado.moim.user.entity.CurrentLogin;
import avocado.moim.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper {

    User confirmUser(String email);

    void deleteExpireToken();

    void saveLoginInfo(CurrentLogin currentLoginEntity);

    void deleteToken(String email);

    boolean checkValidRefreshToken(String claimEmail, String refreshToken);

    void updateLastRefreshTime(String email);
}
