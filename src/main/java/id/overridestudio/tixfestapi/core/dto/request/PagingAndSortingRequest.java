package id.overridestudio.tixfestapi.core.dto.request;

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
public class PagingAndSortingRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    public Integer getPage() {
        return page <= 0 ? 0 : page - 1;
    }
}
