package ru.isis_test.core.message;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FarmerDto {
    private int id;
    @NotNull(message = "Required")
    private String organizationName;

    private String opf;
    @Pattern(regexp = "\\d{10}", message = "only 10 digits")
    @NotNull(message = "Required")
    private String inn;
    @Pattern(regexp = "[0-9]+", message = "only numbers")
    private String kpp;

    @Pattern(regexp = "[0-9]+", message = "only numbers")
    private String ogrn;

    private String regDate = LocalDate.now().toString();

    private boolean archiveStatus;

    private String regDistrict;

    private List<String> fieldDistricts = new ArrayList<>();
}
