package QCraft.QCraft.Handler;

import QCraft.QCraft.domain.CustomOAuth2User;
import QCraft.QCraft.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String userEmail = customOAuth2User.getName();
        String accessToken = jwtUtils.createAccessToken(userEmail);
        String refreshToken = jwtUtils.createRefreshToken(userEmail);

        response.sendRedirect("http://localhost/member/oauth-response/" + accessToken + "/3600");
    }
}
