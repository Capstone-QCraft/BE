package QCraft.QCraft.service;

import QCraft.QCraft.dto.request.*;
import QCraft.QCraft.dto.response.*;
import org.springframework.http.ResponseEntity;

public interface MemberService {

    ResponseEntity<? super EmailCheckResponseDTO> emailCheck(EmailCheckRequestDTO emailCheckRequestDTO);
    ResponseEntity<? super EmailCertificationResponseDTO> emailCertification(EmailCertificationRequestDTO emailCertificationRequestDTO);
    ResponseEntity<? super CheckCertificationResponseDTO> checkCertification(CheckCertificationRequestDTO checkCertificationRequestDTO);
    ResponseEntity<? super SignUpResponseDTO> signUp(SignUpRequestDTO signUpRequestDTO);
    ResponseEntity<? super SignInResponseDTO> signIn(SignInRequestDTO signInRequestDTO);
}
