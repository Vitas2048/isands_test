package ru.isis_test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.isis_test.IsisTestApplication;
import ru.isis_test.core.exception.ApplicationException;
import ru.isis_test.model.OpfForm;

@SpringBootTest(classes = IsisTestApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class OpfServiceTest {
    @Autowired
    private OpfService opfService;

    @Test
    public void whenGetByName() throws ApplicationException {
        OpfForm form = new OpfForm();
        form.setId(1);
        form.setName("ЮР");
        Assertions.assertEquals(form, opfService.getByName("ЮР"));
    }

    @Test
    public void whenGetByNameReturnEmpty() {
        Assertions.assertThrows(ApplicationException.class, () -> {
            opfService.getByName("Ю");
        });
    }

}