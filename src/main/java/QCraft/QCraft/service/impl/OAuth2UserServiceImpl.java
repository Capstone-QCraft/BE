package QCraft.QCraft.service.impl;

import QCraft.QCraft.domain.CustomOAuth2User;
import QCraft.QCraft.domain.Member;
import QCraft.QCraft.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        String userEmail = null;
        String userName = null;
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        switch (provider) {
            case "google":
                userEmail = (String) oAuth2User.getAttributes().get("email");
                userName = (String) oAuth2User.getAttributes().get("name");
                //member = new Member(userEmail,userName,"google");
                break;
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
                Map<String, Object> kakaoProperties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");

                userEmail = (String) kakaoAccount.get("email");
                userName = (String) kakaoProperties.get("nickname");
                //member = new Member(userEmail, userName, "kakao");
                break;
            case "naver":
                Map<String, String> response = (Map<String, String>) oAuth2User.getAttributes().get("response");
                userEmail = response.get("email");
                userName = response.get("name");
                //member = new Member(userEmail, userName, "naver");

                break;
            default:
                throw new OAuth2AuthenticationException("Unsupported registration ID: " + oAuth2UserRequest.getClientRegistration().getRegistrationId());

        }
        System.out.println(userEmail + " " + userName);

        String finalUserEmail = userEmail;
        String finalUserName = userName;

        Member member = memberRepository.findByEmail(userEmail)
                .orElseGet(() -> {
                    Member newMember = new Member(finalUserEmail, finalUserName, provider);
                    return memberRepository.save(newMember);
                });

        return new CustomOAuth2User(userEmail);
    }


}
