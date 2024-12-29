package bit.user.service;

public interface TokenService {
    String createNewAccessToken(String refreshToken);

    String createNewFakeToken();
}
