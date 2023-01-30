package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class DigitsDTO {
    public static class Group1 {}
    public static class Group2 {}

    @Digits.List(value = {
            @Digits(groups = { Group1.class }, integer = 3, fraction = 0),
            @Digits(groups = { Group2.class }, integer = 10, fraction = 0)
    }) public BigDecimal x = BigDecimal.ONE;
    @Digits.List(value = {
            @Digits(groups = { Group1.class }, integer = 0, fraction = 3),
            @Digits(groups = { Group2.class }, integer = 0, fraction = 10)
    }) public BigDecimal y = BigDecimal.ONE;
    @Digits(integer = 3, fraction = 3) public Integer z = 1;
}
