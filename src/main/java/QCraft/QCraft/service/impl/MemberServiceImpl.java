package QCraft.QCraft.service.impl;

import QCraft.QCraft.domain.Certification;
import QCraft.QCraft.domain.Member;
import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.dto.request.member.*;
import QCraft.QCraft.dto.response.member.*;
import QCraft.QCraft.email.CertificationNumber;
import QCraft.QCraft.email.EmailUtils;
import QCraft.QCraft.jwt.JwtUtils;
import QCraft.QCraft.repository.*;
import QCraft.QCraft.service.GetAuthenticationService;
import QCraft.QCraft.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final CertificationRepository certificationRepository;
    private final InterviewRepository interviewRepository;
    private final ResumeFileRepository resumeFileRepository;
    private final ResumeFileTextRepository resumeFileTextRepository;

    private final GetAuthenticationService getAuthenticationService;

    private final EmailUtils emailUtils;
    private final JwtUtils jwtUtils;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


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

            Optional<Certification> existingCertification = certificationRepository.findByEmail(userEmail);
            Certification certification;

            if (existingCertification.isPresent()) {
                // 기존 인증 정보가 있으면 해당 정보 가져오기
                certification = existingCertification.get();
            } else {
                // 새로운 인증 정보 생성
                certification = new Certification();
                certification.setEmail(userEmail);
            }

            // 인증번호 업데이트
            certification.setCertificationNumber(certificationNumber);
            certification.setCreatedAt(LocalDateTime.now());

            // 인증 정보 저장 (기존 인증 정보가 대체됨)
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
            String certificationNumber = checkCertificationRequestDTO.getCertificationNumber();

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
        String accessToken = null;
        String refreshToken = null;
        try {
            String email = signInRequestDTO.getEmail();
            String password = signInRequestDTO.getPassword();

            Optional<Member> member = memberRepository.findByEmail(email);
            if (member.isEmpty()) {
                return SignInResponseDTO.signInFailed();
            }

            if (!member.get().getType().equals("email")) {
                return SignInResponseDTO.signInFailed();
            }


            String encodedPassword = member.get().getPassword();
            if (!passwordEncoder.matches(password, encodedPassword)) {
                return SignInResponseDTO.passwordMismatch();
            }


            String memberId = member.get().getId();
            List<String> memberRole = Collections.singletonList(member.get().getRole());
            accessToken = jwtUtils.createAccessToken(memberId, memberRole);
            refreshToken = jwtUtils.createRefreshToken(memberId);

            member.get().setRefreshToken(refreshToken);

            memberRepository.save(member.get());

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return new ResponseEntity<>(SignInResponseDTO.success(accessToken), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    //토큰 재발급
    @Override
    public ResponseEntity<? super RefreshTokenResponseDTO> refreshToken(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();

            if (cookies == null) {
                return RefreshTokenResponseDTO.tokenNotFound();
            }

            String refreshToken = null;

            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }

            if (refreshToken == null) {
                return RefreshTokenResponseDTO.tokenNotFound();
            }

            List<String> tokens = jwtUtils.refreshToken(refreshToken);
            if (tokens == null || tokens.isEmpty()) {
                return RefreshTokenResponseDTO.expiredRefreshToken();
            }

            String newAccessToken = tokens.get(0);
            String newRefreshToken = tokens.get(1);

            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

            return new ResponseEntity<>(RefreshTokenResponseDTO.success(newAccessToken), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

    }

    //로그아웃
    @Override
    public ResponseEntity<? super SignOutResponseDTO> signOut(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return SignOutResponseDTO.alreadyLogOut();
            }

            String refreshToken = null;
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }

            if (refreshToken == null) {
                return SignOutResponseDTO.alreadyLogOut();
            }

            // JWT에서 memberId(subject) 추출
            String memberId = jwtUtils.getIdFromToken(refreshToken);
            if (memberId == null) {
                return ResponseDTO.validationError();
            }

            // memberId로 사용자 조회 및 처리
            Optional<Member> memberOptional = memberRepository.findById(memberId);
            if (memberOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }

            Member member = memberOptional.get();

            member.setRefreshToken(null);
            memberRepository.save(member);

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(0) // 즉시 만료
                    .sameSite("Strict")
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return new ResponseEntity<>(SignOutResponseDTO.success(), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }


    //회원정보 불러오기
    @Override
    public ResponseEntity<? super GetMemberInfoResponseDTO> getMemberInfo() {
        Optional<Member> memberOptional;
        Member member;
        try {
            memberOptional = getAuthenticationService.getAuthentication();
            if (memberOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }

            member = memberOptional.get();


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
        return GetMemberInfoResponseDTO.success(member);
    }


    //회원정보 수정
    @Override
    public ResponseEntity<? super UpdateMemberInfoResponseDTO> updateMemberInfo(UpdateMemberInfoRequestDTO updateMemberInfoRequestDTO) {
        try {
            String email = updateMemberInfoRequestDTO.getEmail();
            String name = updateMemberInfoRequestDTO.getName();
            String oldPassword = updateMemberInfoRequestDTO.getOldPassword();
            String newPassword = updateMemberInfoRequestDTO.getNewPassword();

            Optional<Member> member = memberRepository.findByEmail(email);
            if (member.isEmpty()) {
                return ResponseDTO.databaseError();
            }

            Member memberEntity = member.get();

            // 비밀번호 수정 로직 (oldPassword와 newPassword가 제공된 경우에만 처리)
            if (oldPassword != null && newPassword != null) {
                String curEncodePassword = memberEntity.getPassword();
                if (!passwordEncoder.matches(oldPassword, curEncodePassword)) {
                    return UpdateMemberInfoResponseDTO.passwordMismatch(); // 기존 비밀번호가 일치하지 않음
                }
                // 새로운 비밀번호로 업데이트
                memberEntity.setPassword(passwordEncoder.encode(newPassword));
            }

            // 이름 수정 로직 (name이 제공된 경우에만 처리)
            if (name != null && !name.isEmpty()) {
                memberEntity.setName(name); // 이름 수정
            }

            memberEntity.setUpdatedAt(LocalDateTime.now());

            memberRepository.save(memberEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return UpdateMemberInfoResponseDTO.success();
    }

    //회원탈퇴
    @Override
    @Transactional
    public ResponseEntity<? super WithdrawMemberResponseDTO> withdraw() {
        try {
            Optional<Member> memberOptional = getAuthenticationService.getAuthentication();
            if (memberOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }
            Member member = memberOptional.get();


            Optional<List<ResumeFile>> resumeFileOptional = resumeFileRepository.findByMemberId(member.getId());
            if (resumeFileOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }
            List<ResumeFile> resumeFiles = resumeFileOptional.get();

            for (ResumeFile resumeFile : resumeFiles) {
                resumeFileTextRepository.deleteByResumeFile_Id(resumeFile.getId());
            }
            interviewRepository.deleteByMemberId(member.getId());
            resumeFileRepository.deleteByMemberId(member.getId());
            memberRepository.deleteById(member.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return WithdrawMemberResponseDTO.success();
    }


}
