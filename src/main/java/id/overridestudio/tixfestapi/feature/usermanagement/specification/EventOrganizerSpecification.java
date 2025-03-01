package id.overridestudio.tixfestapi.feature.usermanagement.specification;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.SearchEventOrganizerRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EventOrganizerSpecification {
    public static Specification<EventOrganizer> getSpecification(SearchEventOrganizerRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getQuery())) {
                Predicate queryPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("eventOrganizerName")), request.getQuery() + "%");
                predicates.add(queryPredicate);
            }

            if (predicates.isEmpty()) return criteriaBuilder.conjunction();

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
