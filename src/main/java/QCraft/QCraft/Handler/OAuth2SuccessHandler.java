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

        response.sendRedirect(redirectUrl + accessToken+"/"+refreshToken);
    }
}
