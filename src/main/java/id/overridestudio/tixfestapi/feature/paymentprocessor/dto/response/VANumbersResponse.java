package id.overridestudio.tixfestapi.feature.paymentprocessor.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VANumbersResponse {

    @JsonProperty(value = "bank")
    private String bankName;

    @JsonProperty(value = "va_number")
    private String vaNumber;
}
