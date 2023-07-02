package ru.isis_test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.core.exception.ValidationException;
import ru.isis_test.core.message.ErrorResponse;
import ru.isis_test.model.District;
import ru.isis_test.model.Farmer;
import ru.isis_test.core.message.FarmerDto;
import ru.isis_test.service.DistrictService;
import ru.isis_test.service.FarmerService;
import ru.isis_test.config.specification.FarmerSpecificationsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api/farmers")
@AllArgsConstructor
public class FarmerController {
    private FarmerService farmerService;

    private DistrictService districtService;

    @Operation(summary = "Save new Farmer")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saved successfully new Farmer"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<FarmerDto> addNew(@Valid @RequestBody FarmerDto farmer,
                                            BindingResult result) throws ApplicationException {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        return ResponseEntity.ok(farmerService.saveByDto(farmer));
    }

    @Operation(summary = "Get farmers with filter example 'search=inn:155,organizationName:SibFarms'")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully get farmers")
    })
    @GetMapping
    public ResponseEntity<List<FarmerDto>> getAll(@RequestParam(value = "search") String search) {
        FarmerSpecificationsBuilder builder = new FarmerSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:)(\\w+?|\\d{4}-\\d{2}-\\d{2}),",
                Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            buildWithObject(matcher, builder);
        }
        Specification<Farmer> spec = builder.build();
        List<Farmer> farmers = farmerService.getAll(spec);
        return ResponseEntity.ok(farmerService.AllToDto(farmers));
    }

    @Operation(summary = "edit Farmer by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saved successfully edited Farmer"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PostMapping("/{id}/edit")
    public ResponseEntity<FarmerDto> edit(@Valid @RequestBody FarmerDto farmerDto, @PathVariable int id,
                                          BindingResult result)
            throws ApplicationException {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        return ResponseEntity.ok(farmerService.editById(id, farmerDto));
    }

    @Operation(summary = "get Farmer by id")
    @ApiResponses({
            @ApiResponse(
                    description = "Farmer successfully found",
                    responseCode = "200"
            ),
            @ApiResponse(
                    description = "Not found with current id",
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<FarmerDto> getById(@PathVariable int id)
            throws ApplicationException{
        return ResponseEntity.ok(farmerService.toDtoById(id));
    }

    @Operation(summary = "Archive Farmer by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "archived successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Farmer not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PostMapping("/{id}/toArchive")
    public ResponseEntity<FarmerDto> toArchive(@PathVariable int id)
            throws ApplicationException{
        Farmer farmer = farmerService.toArchive(id);
        return ResponseEntity.ok(farmerService.toDto(farmer));
    }

    private void buildWithObject(Matcher matcher, FarmerSpecificationsBuilder builder) {

        if (matcher.group(3).equalsIgnoreCase("true")) {
            builder.with(matcher.group(1), matcher.group(2), true);
            return;
        }
        if (matcher.group(3).equalsIgnoreCase("false")) {
            builder.with(matcher.group(1), matcher.group(2), false);
            return;
        }
        if (isValidDate(matcher.group(1), matcher.group(3))) {
            builder.with(matcher.group(1), matcher.group(2),  LocalDate.parse(matcher.group(3)));
            return;
        }
        Optional<District> optDistrict = hasDistrict(matcher.group(1), matcher.group(3));
        if (optDistrict.isPresent()) {
            builder.with(matcher.group(1), matcher.group(2), optDistrict.get());
            return;
        }
        builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
    }
    private static boolean isValidDate(String name, String date) {
        if (!name.equals("regDate")) return false;
        try {
            LocalDate.parse(date);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Optional<District> hasDistrict(String name, String number) {
        if (!name.equals("regDistrict")) return Optional.empty();
        return districtService.findById(Integer.parseInt(number));
    }

}
