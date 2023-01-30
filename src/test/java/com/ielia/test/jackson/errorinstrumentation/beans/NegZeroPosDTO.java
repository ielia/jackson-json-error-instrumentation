package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class NegZeroPosDTO {
    @Negative @NegativeOrZero @PositiveOrZero @Positive public int immutable = 0;
    @Negative public int toBePosOrZero = -1;
    @NegativeOrZero public int toBePos = -2;
    @PositiveOrZero public int toBeNeg = 3;
    @Positive public int toBeNegOrZero = 4;
    @Negative @Positive public int toBeZero = 5;
    @Negative @NegativeOrZero public int toBePos2 = -6;
    @PositiveOrZero @Positive public int toBeNeg2 = 7;
}
