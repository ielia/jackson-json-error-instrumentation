package com.ielia.test.jackson.errorinstrumentation.integration.services;

import com.ielia.test.jackson.errorinstrumentation.integration.beans.FooDTO;
import org.springframework.stereotype.Component;

@Component
public interface FooService {
    FooDTO getFoo();
    boolean checkFoo(FooDTO foo);
}
