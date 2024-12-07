package QCraft.QCraft.domain;

import QCraft.QCraft.dto.request.member.SignUpRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "member")
@NoArgsConstructor
public class Member {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String name;

    private String role;
    private String type;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String refreshToken;

    public Member(SignUpRequestDTO signUpRequestDTO) {
        this.email = signUpRequestDTO.getEmail();
        this.password = signUpRequestDTO.getPassword();
        this.name = signUpRequestDTO.getName();
        this.role = "ROLE_USER";
        this.type = "email";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Member(String email, String name, String type) {
        this.email = email;
        this.password = "thisIsPasswordForOAuth2UsersDoNotUseThisPasswordPlease+qwer1234!!";
        this.name = name;
        this.role = "ROLE_USER";
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
