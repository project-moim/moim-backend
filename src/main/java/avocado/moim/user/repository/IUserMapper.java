package avocado.moim.user.repository;

import avocado.moim.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper {

    User confirmUser(String email);
}
