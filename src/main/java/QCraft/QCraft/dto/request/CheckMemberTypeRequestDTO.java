package QCraft.QCraft.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckMemberTypeRequestDTO {

    @NotBlank
    private String email;
}
