package bit.app.auth.service;

public interface TokenService {
    String createNewAccessToken(String refreshToken);

    String createNewFakeToken();
}
