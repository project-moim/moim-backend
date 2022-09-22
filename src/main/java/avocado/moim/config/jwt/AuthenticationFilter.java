package avocado.moim.config.jwt;

import avocado.moim.config.auth.PrincipalDetails;
import avocado.moim.user.dto.UserDto;
import avocado.moim.user.model.LoginRequestModel;
import avocado.moim.user.service.UserService;
import avocado.moim.util.AuthenticationUtils;
import avocado.moim.util.Const;
import avocado.moim.util.TokenProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;
    private AuthenticationUtils utils;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment env, UserService userService, AuthenticationUtils utils) {
        this.userService = userService;
        this.env = env;
        this.utils = utils;
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.warn("================================================attemptAuthentication");

        try {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestModel.class);

            System.out.println("creds password = " + creds.getPassword());
            // 	패스워드 정책 체크
            if (!utils.checkPw(creds.getPassword())) {
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "fail 1");
                response.addHeader("message", String.valueOf(Const.LOGIN_FAIL_PASSWORD_POLICY_VIOLATION));
            }

            // 	인증정보 조회
            UserDto userDto = userService.confirmUser(creds.getEmail(), creds.getPassword());


            //	일치하는 정보가 없는 경우
            if (!StringUtils.hasText(userDto.getEmail())) {
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                response.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "fail 2");
                response.addHeader("message", String.valueOf(Const.LOGIN_FAIL_NO_MATCHING_ACCOUNT));
                return null;
            }


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDto.getEmail(),
                    creds.getPassword()
            );
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException ex) {
            log.warn("attemptAuthentication error : {}", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("accessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + TokenProperties.EXPIRATION_TIME))
                .withClaim("email", principalDetails.getUser().getEmail())
                .withClaim("name", principalDetails.getUser().getName())
                .withClaim("address", principalDetails.getUser().getAddress())
                .withClaim("role", principalDetails.getUser().getRole().toString())
                .sign(Algorithm.HMAC512(TokenProperties.SECRET));

        response.addHeader(TokenProperties.HEADER_STRING, TokenProperties.TOKEN_PREFIX + jwtToken);
    }
}
