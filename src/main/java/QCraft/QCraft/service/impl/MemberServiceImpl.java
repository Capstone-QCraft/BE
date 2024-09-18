package QCraft.QCraft.service.impl;

import QCraft.QCraft.domain.Certification;
import QCraft.QCraft.domain.Member;
import QCraft.QCraft.domain.RefreshToken;
import QCraft.QCraft.dto.request.*;
import QCraft.QCraft.dto.response.*;
import QCraft.QCraft.email.CertificationNumber;
import QCraft.QCraft.email.EmailUtils;
import QCraft.QCraft.jwt.JwtUtils;
import QCraft.QCraft.repository.CertificationRepository;
import QCraft.QCraft.repository.MemberRepository;
import QCraft.QCraft.repository.RefreshTokenRepository;
import QCraft.QCraft.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final CertificationRepository certificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final EmailUtils emailUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    //이메일 중복체크
    @Override
    public ResponseEntity<? super EmailCheckResponseDTO> emailCheck(EmailCheckRequestDTO emailCheckRequestDTO) {
        try {
            String userEmail = emailCheckRequestDTO.getEmail();
            boolean isExist = memberRepository.findByEmail(userEmail).isPresent();
            if (isExist) {
                return EmailCheckResponseDTO.duplicated();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return EmailCheckResponseDTO.success();
    }

    //이메일 인증
    @Override
    public ResponseEntity<? super EmailCertificationResponseDTO> emailCertification(EmailCertificationRequestDTO emailCertificationRequestDTO) {

        try {
            String userEmail = emailCertificationRequestDTO.getEmail();
            boolean isExist = memberRepository.findByEmail(userEmail).isPresent();
            if (isExist) {
                return EmailCheckResponseDTO.duplicated();
            }

            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSuccess = emailUtils.sendCertificationMail(userEmail, certificationNumber);
            if (!isSuccess) {
                return EmailCertificationResponseDTO.mailSendFailed();
            }

            Certification certification = new Certification();
            certification.setCertificationNumber(certificationNumber);
            certification.setEmail(userEmail);
            certificationRepository.save(certification);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return EmailCertificationResponseDTO.success();
    }

    //인증번호 확인
    @Override
    public ResponseEntity<? super CheckCertificationResponseDTO> checkCertification(CheckCertificationRequestDTO checkCertificationRequestDTO) {
        try {

            String email = checkCertificationRequestDTO.getEmail();
            String certificationNumber = CertificationNumber.getCertificationNumber();

            Optional<Certification> certification = certificationRepository.findByEmail(email);
            if (certification.isEmpty()) {
                return CheckCertificationResponseDTO.certificationFailed();
            }

            boolean isMatched = certification.get().getEmail().equals(email) && certification.get().getCertificationNumber().equals(certificationNumber);
            if (!isMatched) {
                return CheckCertificationResponseDTO.certificationFailed();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return CheckCertificationResponseDTO.success();
    }

    //회원가입
    @Override
    public ResponseEntity<? super SignUpResponseDTO> signUp(SignUpRequestDTO signUpRequestDTO) {
        try {
            String email = signUpRequestDTO.getEmail();
            String password = signUpRequestDTO.getPassword();
            String name = signUpRequestDTO.getName();
            String certificationNumber = signUpRequestDTO.getCertificationNumber();

            Optional<Member> member = memberRepository.findByEmail(email);
            if (member.isPresent()) {
                return SignUpResponseDTO.duplicated();
            }

            Optional<Certification> certification = certificationRepository.findByEmail(email);
            if (certification.isEmpty() || !(certification.get().getEmail().equals(email) && certification.get().getCertificationNumber().equals(certificationNumber))) {
                return SignUpResponseDTO.certificationFailed();
            }

            String encodedPassword = passwordEncoder.encode(password);
            signUpRequestDTO.setPassword(encodedPassword);

            Member memberEntity = new Member(signUpRequestDTO);
            memberRepository.save(memberEntity);

            certificationRepository.deleteByEmail(email);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return SignUpResponseDTO.success();
    }

    //로그인
    @Override
    public ResponseEntity<? super SignInResponseDTO> signIn(SignInRequestDTO signInRequestDTO) {
        String accessToken=null;
        String refreshToken=null;
        try{
            String email = signInRequestDTO.getEmail();
            String password = signInRequestDTO.getPassword();

            Optional<Member> member = memberRepository.findByEmail(email);
            if (member.isEmpty()) {
                return SignInResponseDTO.signInFailed();
            }

            String encodedPassword = member.get().getPassword();
            if(!passwordEncoder.matches(password, encodedPassword)){
                return SignInResponseDTO.signInFailed();
            }

            String type = member.get().getType();
            if(type.equals("email")){
                return SignInResponseDTO.signInFailed();
            }

            String memberId = member.get().getId();
            accessToken = jwtUtils.createAccessToken(memberId);
            refreshToken = jwtUtils.createRefreshToken(memberId);



        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return SignInResponseDTO.success(accessToken,refreshToken);
    }

    //accessToken 재발급
    @Override
    public ResponseEntity<? super ReissueTokenResponseDTO> reissueToken(ReissueTokenRequestDTO reissueTokenRequestDTO) {
        String newAccessToken = null;
        String newRefreshToken = null;
        try {
            String memberId = reissueTokenRequestDTO.getMemberId();
            String refreshToken = reissueTokenRequestDTO.getRefreshToken();

            Optional<RefreshToken> rt = refreshTokenRepository.findByRefreshToken(refreshToken);

            if(rt.isEmpty()){
                return ReissueTokenResponseDTO.tokenExpiration();
            }

            if(!rt.get().getRefreshToken().equals(refreshToken)){
                return ReissueTokenResponseDTO.tokenExpiration();
            }

            newAccessToken = jwtUtils.createAccessToken(memberId);
            newRefreshToken = jwtUtils.createRefreshToken(memberId, rt.get());

        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return ReissueTokenResponseDTO.success(newAccessToken, newRefreshToken);

    }

}
