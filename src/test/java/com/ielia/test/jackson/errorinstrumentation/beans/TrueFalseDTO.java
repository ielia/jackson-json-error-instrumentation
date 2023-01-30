package com.ielia.test.jackson.errorinstrumentation.beans;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;

public class TrueFalseDTO {
    public boolean w = false;
    @AssertFalse @AssertTrue public boolean x = true;
    @AssertFalse public boolean y = false;
    @AssertTrue public boolean z = true;
}
