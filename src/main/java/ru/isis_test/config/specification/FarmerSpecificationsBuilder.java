package ru.isis_test.config.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class FarmerSpecificationsBuilder {
    private final List<SpecSearchCriteria> params;

    public FarmerSpecificationsBuilder() {
        params = new ArrayList<>();
        params.add(SpecSearchCriteria.DEFAULT_CRITERIA);
    }

    public final FarmerSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SpecSearchCriteria(key, SearchOperation.getSimpleOperation(operation.charAt(0)), value));
        return this;
    }

    public Specification build() {

        Specification result = new FarmerSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new FarmerSpecification(params.get(i)))
                    : Specification.where(result).and(new FarmerSpecification(params.get(i)));
        }
        return result;
    }
}
