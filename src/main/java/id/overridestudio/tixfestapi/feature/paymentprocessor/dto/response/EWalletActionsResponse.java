package id.overridestudio.tixfestapi.feature.paymentprocessor.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EWalletActionsResponse {

    @JsonProperty(value = "name")
    private String nameAction;

    @JsonProperty(value = "method")
    private String method;

    @JsonProperty(value = "url")
    private String url;
}
