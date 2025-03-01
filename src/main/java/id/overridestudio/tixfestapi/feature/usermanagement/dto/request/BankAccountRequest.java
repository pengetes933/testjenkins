package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountRequest {
    private String bankCode;
    private String accountNumber;
    private String accountHolderName;
}
