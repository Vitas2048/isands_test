package ru.isis_test.core.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isis_test.model.District;
import ru.isis_test.model.OpfForm;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter@Setter
@NoArgsConstructor
public class Temp {

    private OpfForm opf;

    private District regDistrict;

    private Set<District> fieldDistricts = new HashSet<>();
}
