package com.ielia.test.jackson.errorinstrumentation;

import org.apache.commons.lang3.StringUtils;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class TestNGTest implements ITest {
    protected ThreadLocal<String> testName = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    protected void beforeMethod(Method method, ITestContext context, Object[] data) {
        String template = method.getAnnotation(Test.class).testName();
        testName.set(String.format(StringUtils.isBlank(template) ? method.getName() : template, data));
    }

    @Override
    public String getTestName() {
        return testName.get();
    }
}
