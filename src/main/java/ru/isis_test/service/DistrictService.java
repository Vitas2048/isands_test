package ru.isis_test.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.core.message.ErrorCode;
import ru.isis_test.model.District;
import ru.isis_test.repository.DistrictRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DistrictService {

    private DistrictRepository districtRepository;

    public District save(District district) {
        return districtRepository.save(district);
    }

    public District edit(District district, int id) throws ApplicationException {
        District dis = districtRepository.findById(id).orElseThrow(new ApplicationException(ErrorCode.DistrictNotFound));
        dis.setName(district.getName());
        dis.setCode(district.getCode());
        dis.setArchiveStatus(district.isArchiveStatus());
        return districtRepository.save(dis);
    }

    public District toArchive(int id) throws ApplicationException {
        District district = getById(id);
        district.setArchiveStatus(true);
        return save(district);
    }

    public Optional<District> findById(int id) {
        return districtRepository.findById(id);
    }

    public District getById(int id) throws ApplicationException {
        return findById(id).orElseThrow(new ApplicationException(ErrorCode.DistrictNotFound));
    }

    public List<District> getAll() {
        return districtRepository.getAll();
    }

    public District findByName(String name) throws ApplicationException {
        return districtRepository.findByName(name).orElseThrow(new ApplicationException(ErrorCode.DistrictNotFound));
    }

    public List<District> getByFilter(String name, String code) {
        if (name != null && code != null) {
            return districtRepository.getByNameAndCode(name, code);
        }
        if (name == null && code != null) {
            return districtRepository.getByCode(code);
        }
        if (name != null) {
            return districtRepository.getByName(name);
        }
        return getAll();
    }

    public void deleteAll() {
        districtRepository.deleteAll();
    }
}
