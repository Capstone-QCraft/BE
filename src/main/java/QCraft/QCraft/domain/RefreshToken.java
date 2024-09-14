package QCraft.QCraft.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@AllArgsConstructor
@Document(collection = "refreshToken")
public class RefreshToken {

    @Id
    private String id;
    private String refreshToken;

    @Indexed(expireAfterSeconds = 0)
    private Date expiresAt;

}
