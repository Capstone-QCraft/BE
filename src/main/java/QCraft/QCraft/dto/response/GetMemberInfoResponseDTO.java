package QCraft.QCraft.dto.response;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.domain.Member;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetMemberInfoResponseDTO extends ResponseDTO {
    private String email;
    private String name;
    private String type;
    private String role;

    public GetMemberInfoResponseDTO(Member member) {
        super();
        this.email = member.getEmail();
        this.name = member.getName();
        this.type = member.getType();
        this.role = member.getRole();
    }

    public static ResponseEntity<GetMemberInfoResponseDTO> success(Member member) {
        GetMemberInfoResponseDTO responseDTO = new GetMemberInfoResponseDTO(member);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> memberNotFound() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
