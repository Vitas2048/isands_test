package ru.isis_test.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.core.message.ErrorCode;
import ru.isis_test.model.OpfForm;
import ru.isis_test.repository.OpfRepository;

@Service
@AllArgsConstructor
public class OpfService {
    private OpfRepository opfRepository;

    public OpfForm getByName(String name) throws ApplicationException {
        return opfRepository.findByName(name).orElseThrow(new ApplicationException(ErrorCode.OpfNotFound));
    }
}
