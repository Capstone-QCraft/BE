package QCraft.QCraft.service;

import QCraft.QCraft.dto.request.member.*;
import QCraft.QCraft.dto.response.member.*;
import org.springframework.http.ResponseEntity;

public interface MemberService {

    ResponseEntity<? super EmailCheckResponseDTO> emailCheck(EmailCheckRequestDTO emailCheckRequestDTO);
    ResponseEntity<? super EmailCertificationResponseDTO> emailCertification(EmailCertificationRequestDTO emailCertificationRequestDTO);
    ResponseEntity<? super CheckCertificationResponseDTO> checkCertification(CheckCertificationRequestDTO checkCertificationRequestDTO);
    ResponseEntity<? super SignUpResponseDTO> signUp(SignUpRequestDTO signUpRequestDTO);
    ResponseEntity<? super SignInResponseDTO> signIn(SignInRequestDTO signInRequestDTO);
    ResponseEntity<? super UpdateMemberInfoResponseDTO> updateMemberInfo(UpdateMemberInfoRequestDTO updateMemberInfoRequestDTO);
    ResponseEntity<? super GetMemberInfoResponseDTO> getMemberInfo();
    ResponseEntity<? super WithdrawMemberResponseDTO> withdraw();
    ResponseEntity<? super RefreshTokenResponseDTO> refreshToken(RefreshTokenRequestDTO requestDTO);
}
