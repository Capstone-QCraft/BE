package QCraft.QCraft.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReissueTokenRequestDTO {
    @NotBlank
    private String memberId;

    @NotBlank
    private String refreshToken;

}
