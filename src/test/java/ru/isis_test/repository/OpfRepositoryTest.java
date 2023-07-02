package ru.isis_test.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.isis_test.model.OpfForm;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest
class OpfRepositoryTest {

    @Autowired
    private OpfRepository repository;

    @Test
    public void whenFind() {
        OpfForm form = repository.findByName("ЮР").get();
        OpfForm form1 = new OpfForm(1, "ЮР");

        assertEquals(form1, form);
    }

    @Test
    public void whenNotFind() {
        assertThrows(NoSuchElementException.class, () -> {
            OpfForm form = repository.findByName("ЮР2").get();
        });
    }

}