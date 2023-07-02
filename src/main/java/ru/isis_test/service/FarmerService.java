package ru.isis_test.service;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.core.message.ErrorCode;
import ru.isis_test.core.message.FarmerDto;
import ru.isis_test.core.message.Temp;
import ru.isis_test.model.*;
import ru.isis_test.repository.FarmerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FarmerService {
    private FarmerRepository farmerRepository;

    private OpfService opfService;

    private DistrictService districtService;

    public Farmer getById(int id) throws ApplicationException {
        return farmerRepository.findById(id).orElseThrow(new ApplicationException(ErrorCode.FarmerNotFound));
    }

    public FarmerDto saveByDto(FarmerDto farmerDto) throws ApplicationException {
        Farmer farmer = toFarmer(farmerDto);
        farmerDto.setId(save(farmer).getId());
        return farmerDto;
    }

    public FarmerDto toDtoById(int id) throws ApplicationException {
        Farmer farmer = getById(id);
        return toDto(farmer);
    }

    public Farmer toFarmer(FarmerDto farmerDto) throws ApplicationException {
        Temp tempData = tempData(farmerDto);
        Farmer farmer = new Farmer();

        farmer.setOpf(tempData.getOpf());
        farmer.setOgrn(farmerDto.getOgrn());
        farmer.setKpp(farmerDto.getKpp());
        farmer.setRegDistrict(tempData.getRegDistrict());
        farmer.setFieldDistricts(tempData.getFieldDistricts());
        farmer.setRegDate(LocalDate.parse(farmerDto.getRegDate()));
        farmer.setOrganizationName(farmerDto.getOrganizationName());
        farmer.setInn(farmerDto.getInn());
        farmer.setArchiveStatus(farmerDto.isArchiveStatus());

        return farmer;
    }

    public FarmerDto toDto(Farmer farmer) {
        FarmerDto dto = new FarmerDto();

        dto.setId(farmer.getId());
        dto.setOrganizationName(farmer.getOrganizationName());
        dto.setOpf(farmer.getOpf().getName());
        dto.setInn(farmer.getInn());
        dto.setKpp(farmer.getKpp());
        dto.setOgrn(farmer.getOgrn());
        dto.setRegDate(farmer.getRegDate().toString());
        dto.setArchiveStatus(farmer.isArchiveStatus());
        dto.setRegDistrict(farmer.getRegDistrict().getName());
        dto.setFieldDistricts(farmer.getFieldDistricts().stream().map(District::getName).collect(Collectors.toList()));

        return dto;
    }

    public Farmer save(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    public FarmerDto editById(int id, FarmerDto farmerDto) throws ApplicationException {
        Temp tempData = tempData(farmerDto);
        Farmer oldFarmer = getById(id);

        oldFarmer.setOrganizationName(farmerDto.getOrganizationName());
        oldFarmer.setInn(farmerDto.getInn());
        oldFarmer.setArchiveStatus(farmerDto.isArchiveStatus());
        oldFarmer.setOpf(tempData.getOpf());

        oldFarmer.getFieldDistricts().clear();
        tempData.getFieldDistricts().forEach(p -> oldFarmer.getFieldDistricts().add(p));

        oldFarmer.setOgrn(farmerDto.getOgrn());
        oldFarmer.setKpp(farmerDto.getKpp());
        oldFarmer.setRegDate(LocalDate.parse(farmerDto.getRegDate()));
        oldFarmer.setRegDistrict(tempData.getRegDistrict());

        save(oldFarmer);

        farmerDto.setId(oldFarmer.getId());
        return farmerDto;
    }

    private Temp tempData(FarmerDto farmerDto) throws ApplicationException {
        OpfForm opf = opfService.getByName(farmerDto.getOpf());
        District regDistrict = districtService.findByName(farmerDto.getRegDistrict());
        Set<District> fieldDistricts = farmerDto.getFieldDistricts().stream().map(p -> {
            try {
                return districtService.findByName(p);
            } catch (ApplicationException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());
        return new Temp(opf, regDistrict, fieldDistricts);
    }

    public Farmer toArchive(int id) throws ApplicationException {
        Farmer farmer = getById(id);
        farmer.setArchiveStatus(true);
        return save(farmer);
    }

    public List<Farmer> getAll(Specification<Farmer> specification) {
        return farmerRepository.findAll(specification);
    }

    public List<Farmer> getAll() {
        return farmerRepository.findAll();
    }

    public List<FarmerDto> AllToDto(List<Farmer> farmers) {
        return farmers.stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteAll() {
        farmerRepository.deleteAll();
    }
}
