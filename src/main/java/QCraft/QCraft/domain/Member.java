package QCraft.QCraft.domain;

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
    private String age;

    private String role;
}
