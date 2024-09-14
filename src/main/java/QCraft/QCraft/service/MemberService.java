package QCraft.QCraft.service;

import QCraft.QCraft.domain.Member;
import QCraft.QCraft.dto.request.SignUpDTO;

public interface MemberService {
    Member signup(SignUpDTO signUpDTO);
}
