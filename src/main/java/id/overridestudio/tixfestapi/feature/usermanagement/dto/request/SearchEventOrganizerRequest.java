package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import id.overridestudio.tixfestapi.core.dto.request.PagingAndSortingRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchEventOrganizerRequest extends PagingAndSortingRequest {
    private String query;
}
