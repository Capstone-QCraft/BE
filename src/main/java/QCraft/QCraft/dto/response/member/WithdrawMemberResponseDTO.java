package QCraft.QCraft.dto.response.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class WithdrawMemberResponseDTO extends ResponseDTO {

    private WithdrawMemberResponseDTO() {
        super();
    }

    public static ResponseEntity<WithdrawMemberResponseDTO> success() {
        WithdrawMemberResponseDTO responseDTO = new WithdrawMemberResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
