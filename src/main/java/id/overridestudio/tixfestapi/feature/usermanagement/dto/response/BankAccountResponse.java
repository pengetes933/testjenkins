package id.overridestudio.tixfestapi.feature.usermanagement.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountResponse {
    private String id;
    private String eventOrganizerId;
    private String bankName;
    private String accountNumber;
    private String accountHolderName;
}
