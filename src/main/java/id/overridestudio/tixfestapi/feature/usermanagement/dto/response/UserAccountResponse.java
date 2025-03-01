package id.overridestudio.tixfestapi.feature.usermanagement.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountResponse {
    private String id;
    private String email;
    private String role;
}
