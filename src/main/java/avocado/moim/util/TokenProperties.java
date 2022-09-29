package avocado.moim.util;

public interface TokenProperties {
    String SECRET = "avocado";
    int EXPIRATION_TIME = 60000 * 10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "AccessToken";
}
