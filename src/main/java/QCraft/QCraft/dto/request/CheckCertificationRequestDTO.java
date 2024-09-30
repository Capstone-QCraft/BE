package QCraft.QCraft.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckCertificationRequestDTO {

    @Email
    @NotBlank
    public String email;

    @NotBlank
    public String certificationNumber;

}
