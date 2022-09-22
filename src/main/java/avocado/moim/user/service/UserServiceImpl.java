package avocado.moim.user.service;

import avocado.moim.user.dto.UserDto;
import avocado.moim.user.entity.CurrentLogin;
import avocado.moim.user.entity.User;
import avocado.moim.user.repository.IUserMapper;
import avocado.moim.user.repository.UserRepository;
import avocado.moim.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IUserMapper userMapper;
    private final Environment env;

    @Override
    public Long join(User user) {
        UserDto userDto = new UserDto(user);
        userDto.setRole(Role.USER);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        userDto.setPassword(encPassword);
        return userRepository.save(userDto.toEntity()).getId();
    }

    @Override
    public UserDto confirmUser(String email, String encryptedPassword) {
        ModelMapper modelMapper = new ModelMapper();
        User userEntity = userMapper.confirmUser(email);

        if (userEntity == null) {
            log.info(String.format("not exists user : %s", email));
            return null;
        }

        if (bCryptPasswordEncoder.matches(encryptedPassword, userEntity.getPassword())) {
            return modelMapper.map(userEntity, UserDto.class);
        } else {
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveLoginInfo(String email, String remoteAddr, long issueTime, String refreshToken) {
        long expirationTimeLimit = Long.parseLong(Objects.requireNonNull(env.getProperty("token.access_expiration_time")));

        // 토큰 만료 시간이 경과한 정보 삭제(last_login_date가 expirationTimeLimit를 초과한 경우 삭제)
        deleteExpireToken();

        CurrentLogin currentLoginEntity = new CurrentLogin(email, refreshToken, remoteAddr, expirationTimeLimit);
        log.info(email + " : 로그인 시간 : " + new Date(issueTime));

        // remote_addr, last_login_date, token 업데이트
        userMapper.saveLoginInfo(currentLoginEntity);
    }

    @Override
    public void deleteExpireToken() {
        userMapper.deleteExpireToken();
    }
}
