package com.jitendra.javaspearhead.learning.services;

import com.jitendra.javaspearhead.learning.CommonExample;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KaveriWinterTest {

    @Spy
    CommonExample commonExample;
    @InjectMocks
    KaveriWinter kaveriWinter;

    @Test
    void name() throws Exception {
        kaveriWinter.execute();
    }
}