package QCraft.QCraft.Handler;

import QCraft.QCraft.domain.CustomOAuth2User;
import QCraft.QCraft.domain.Member;
import QCraft.QCraft.jwt.JwtUtils;
import QCraft.QCraft.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;

    @Value("${oauth.redirect.url}")
    String redirectUrl;
    @Value("${token.expiration.refresh}")
    long refreshTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String userEmail = customOAuth2User.getName();

        Optional<Member> member = memberRepository.findByEmail(userEmail);
        if (member.isEmpty()) {
            return;
        }
        String accessToken = jwtUtils.createAccessToken(member.get().getId(), Collections.singletonList(member.get().getRole()));
        String refreshToken = jwtUtils.createRefreshToken(member.get().getId());

        member.get().setRefreshToken(refreshToken);
        memberRepository.save(member.get());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration / 100) // 7일 유효
                .sameSite("Strict") // 필요한 경우 "Lax"로 변경
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.sendRedirect(redirectUrl + "?accessToken=" + accessToken);
    }
}
