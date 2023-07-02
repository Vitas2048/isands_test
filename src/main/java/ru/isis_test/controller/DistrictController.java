package ru.isis_test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.core.exception.ValidationException;
import ru.isis_test.core.message.ErrorResponse;
import ru.isis_test.model.District;
import ru.isis_test.service.DistrictService;

import java.util.List;
@Tag(name = "districts", description = "district API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/districts")
public class DistrictController {
    private DistrictService districtService;

    @Operation(summary = "Save new District")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saved successfully new District"
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
    public ResponseEntity<District> addNew(@Valid @RequestBody District district, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        return ResponseEntity.ok(districtService.save(district));
    }

    @Operation(summary = "Edit District by id")
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
    @PostMapping("/{id}/edit")
    public ResponseEntity<District> edit(@Valid @RequestBody District district,
                                         @PathVariable int id, BindingResult result)
            throws ApplicationException {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        return ResponseEntity.ok(districtService.edit(district, id));
    }

    @Operation(summary = "Archive District by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saved successfully new Farmer"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "District not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    @PostMapping("/{id}/toArchive")
    private ResponseEntity<District> toArchive(@PathVariable int id)
            throws ApplicationException {
        return ResponseEntity.ok(districtService.toArchive(id));
    }

    @Operation(summary = "Get Districts")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully get districts by filter (query params)"
            )
    })
    @GetMapping()
    public ResponseEntity<List<District>> getAll(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String code) {
        return ResponseEntity.ofNullable(districtService.getByFilter(name, code));
    }

}
