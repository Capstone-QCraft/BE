package QCraft.QCraft.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMemberInfoRequestDTO {
    @NotBlank
    public String email;

    @NotBlank
    public String name;

    @NotBlank
    public String oldPassword;

    @NotBlank
    public String newPassword;
}
