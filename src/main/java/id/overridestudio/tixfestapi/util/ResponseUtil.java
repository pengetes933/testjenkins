package id.overridestudio.tixfestapi.util;

import id.overridestudio.tixfestapi.core.dto.response.WebResponse;
import id.overridestudio.tixfestapi.core.dto.response.PagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ResponseUtil {
    public static <T> ResponseEntity<WebResponse<T>> buildResponse(HttpStatus httpStatus, String message, T data) {
        WebResponse<T> response = new WebResponse<>(httpStatus.value(), message, data,  null);
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static <T> ResponseEntity<WebResponse<?>> buildResponsePage(HttpStatus httpStatus, String message, Page<T> page) {
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .page(page.getPageable().getPageNumber() + 1)
                .size(page.getSize())
                .build();
        WebResponse<List<T>> response = new WebResponse<>(
                httpStatus.value(),
                message,
                page.getContent(),
                pagingResponse
        );
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static ResponseEntity<WebResponse<?>> buildErrorResponse(HttpStatus httpStatus, String message, List<String> errors) {
        WebResponse<?> response = WebResponse.builder()
                .status(httpStatus.value())
                .message(message)
                .build();

        return ResponseEntity.status(httpStatus).body(response);
    }
}
