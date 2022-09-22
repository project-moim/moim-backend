package avocado.moim.user.service;

import avocado.moim.user.dto.UserDto;
import avocado.moim.user.entity.User;
import avocado.moim.user.repository.IUserMapper;
import avocado.moim.user.repository.UserRepository;
import avocado.moim.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IUserMapper userMapper;

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
}
