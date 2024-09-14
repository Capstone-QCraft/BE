package QCraft.QCraft.controller;

import QCraft.QCraft.dto.request.SignUpDTO;
import QCraft.QCraft.exception.ValidateMemberException;
import QCraft.QCraft.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Controller("/member")
public class MemberController {
    private final MemberService memberService;


    //회원가입
    @PostMapping("/signUp")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO) {
        try {
            memberService.signup(signUpDTO);
            return ResponseEntity.ok("success signup");
        } catch (ValidateMemberException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
