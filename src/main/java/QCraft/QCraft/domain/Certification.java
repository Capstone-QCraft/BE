package QCraft.QCraft.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "certification")
public class Certification {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String certificationNumber;

}
