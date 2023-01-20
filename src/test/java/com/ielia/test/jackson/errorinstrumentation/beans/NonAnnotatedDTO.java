package com.ielia.test.jackson.errorinstrumentation.beans;

public class NonAnnotatedDTO {
    private final int privateValue = 1;
    public final int ignored = 2;
    protected final int protectedValue = 3;
    protected final int ignoredProtected = 4;
}
