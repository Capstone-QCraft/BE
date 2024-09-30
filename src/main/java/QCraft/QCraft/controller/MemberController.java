package QCraft.QCraft.controller;

import QCraft.QCraft.dto.request.*;
import QCraft.QCraft.dto.response.*;
import QCraft.QCraft.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;


    //이메일 중복체크
    @GetMapping("/email-check")
    public ResponseEntity<? super EmailCheckResponseDTO> EmailCheck(@RequestBody @Valid EmailCheckRequestDTO emailCheckRequestDTO) {
        return memberService.emailCheck(emailCheckRequestDTO);
    }

    //이메일 인증번호 발송
    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDTO> emailCertification(@RequestBody @Valid EmailCertificationRequestDTO emailCertificationRequestDTO) {
        return memberService.emailCertification(emailCertificationRequestDTO);
    }

    //이메일 인증번호 확인
    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDTO> checkCertification(@RequestBody @Valid CheckCertificationRequestDTO checkCertificationRequestDTO) {
        return memberService.checkCertification(checkCertificationRequestDTO);
    }

    //회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {
        return memberService.signUp(signUpRequestDTO);
    }

    //로그인
    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDTO> signIn(@RequestBody @Valid SignInRequestDTO signInRequestDTO) {
        return memberService.signIn(signInRequestDTO);
    }

    //회원정보 가져오기
    @GetMapping("/get-info")
    public ResponseEntity<? super GetMemberInfoResponseDTO> getMemberInfo() {
        return memberService.getMemberInfo();
    }

    //회원정보 수정
    @PutMapping("/update-info")
    public ResponseEntity<? super UpdateMemberInfoResponseDTO> updateMemberInfo(@RequestBody @Valid UpdateMemberInfoRequestDTO updateMemberInfoRequestDTO) {
        return memberService.updateMemberInfo(updateMemberInfoRequestDTO);
    }

    //회원탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<? super WithdrawMemberResponseDTO> withdraw(){
        return memberService.withdraw();
    }





}
