package QCraft.QCraft.domain;

import QCraft.QCraft.dto.request.SignUpRequestDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "member")
public class Member {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String name;

    private String role;
    private String type;

    public Member(SignUpRequestDTO signUpRequestDTO) {
        this.email = signUpRequestDTO.getEmail();
        this.password = signUpRequestDTO.getPassword();
        this.name = signUpRequestDTO.getName();
        this.role = "ROLE_USER";
        this.type = "email";
    }

    public Member(String email, String name, String type) {
        this.email = email;
        this.password = "password";
        this.name = name;
        this.role = "ROLE_USER";
        this.type = type;
    }
}
