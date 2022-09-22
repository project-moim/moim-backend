package avocado.moim.user.service;

import avocado.moim.user.dto.UserDto;
import avocado.moim.user.entity.User;
import avocado.moim.user.repository.UserRepository;
import avocado.moim.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Long join(User user) {
        UserDto userDto = new UserDto(user);
        userDto.setRole(Role.ROLE_USER);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        userDto.setPassword(encPassword);
        return userRepository.save(userDto.toEntity()).getId();
    }
}
