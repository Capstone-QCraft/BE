package QCraft.QCraft.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpDTO {
    @NotNull(message = "email cannot null")
    private String email;
    @NotNull(message = "password cannot null")
    private String password;
    @NotNull(message = "name cannot null")
    private String name;
    @NotNull(message = "age cannot null")
    private int age;
}
