package com.widehouse.cafe.config;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * Created by kiel on 2017. 2. 28..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {
    @Autowired
    private TestService testService;

    @Test
    public void testCache() {
        String s1 = testService.cachedMethod("p1", "p2");
        String s2 = testService.cachedMethod("p1", "p2");

        then(s1).isEqualTo(s2);
    }
}

interface TestService{
    String cachedMethod(String param1,String param2);
}

@Component
class TestServiceImpl implements TestService{

    @Cacheable(value="default", key="#p0.concat('-').concat(#p1)")
    public String cachedMethod(String param1, String param2){
        return "response " + new Random().nextInt();
    }
}
