package ru.isis_test.config.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import ru.isis_test.model.Farmer;


@AllArgsConstructor
@Getter
public class FarmerSpecification implements Specification<Farmer> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate
            (Root<Farmer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation() == SearchOperation.EQUALITY) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

}
