package ru.teamidea;

import org.junit.Assert;
import org.junit.Test;
import ru.teamidea.solutions.weather.App;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void test() {
        Assert.assertEquals(1, new App().someLogic());
    }
}
