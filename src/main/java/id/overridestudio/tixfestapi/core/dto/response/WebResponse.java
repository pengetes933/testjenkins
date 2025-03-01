package id.overridestudio.tixfestapi.core.dto.response;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WebResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private PagingResponse paging;
}
