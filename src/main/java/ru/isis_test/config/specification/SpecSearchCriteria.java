package ru.isis_test.config.specification;

import lombok.*;

@Getter@Setter
@NoArgsConstructor
public class SpecSearchCriteria {

    public static final SpecSearchCriteria DEFAULT_CRITERIA = new SpecSearchCriteria("archiveStatus",
            SearchOperation.EQUALITY, false);
    private String key;
    private SearchOperation operation;
    private Object value;
    private boolean orPredicate;

    public SpecSearchCriteria(final String key, final SearchOperation operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
