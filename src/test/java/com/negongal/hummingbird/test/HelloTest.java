package com.negongal.hummingbird.test;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HelloTest {

    @Autowired HelloRepository helloRepository;

    @Test
    public void DB_JPA_TEST() {
        //given
        Hello hello = new Hello();
        hello.setName("test hello");

        //when
        Hello saveHello = helloRepository.save(hello);

        //then
        Hello findHello = helloRepository.findById(saveHello.getId()).get();
        Assertions.assertThat(findHello.getName()).isEqualTo(saveHello.getName());
    }

}