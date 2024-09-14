package QCraft.QCraft.service.impl;

import QCraft.QCraft.domain.Member;
import QCraft.QCraft.dto.request.SignUpDTO;
import QCraft.QCraft.exception.ValidateMemberException;
import QCraft.QCraft.repository.MemberRepository;
import QCraft.QCraft.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    //회원가입
    @Transactional
    @Override
    public Member signup(SignUpDTO signUpDTO) {
        if(memberRepository.findByEmail(signUpDTO.getEmail()).isPresent()) {
            throw new ValidateMemberException("Email already exists, "+signUpDTO.getEmail());
        }
        Member member = new Member();
        member.setEmail(signUpDTO.getEmail());
        member.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        member.setName(signUpDTO.getName());
        member.setRole("ROLE_USER");

        return memberRepository.save(member);
    }

}
