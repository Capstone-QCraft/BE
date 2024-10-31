package QCraft.QCraft.service;

import QCraft.QCraft.domain.Member;
import QCraft.QCraft.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetAuthenticationService {
    private final MemberRepository memberRepository;

    public Optional<Member> getAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null||authentication.getPrincipal()==null){
            return Optional.empty();
        }
        String memberId = (String) authentication.getPrincipal();

        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()){
            return Optional.empty();
        }

        return member;
    }
}
