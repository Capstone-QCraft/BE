package QCraft.QCraft.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "refreshToken")
public class RefreshToken {

    @Id
    private String memberId;
    private String refreshToken;

    @Indexed(expireAfterSeconds = 0)
    private Date expiresAt;

}
