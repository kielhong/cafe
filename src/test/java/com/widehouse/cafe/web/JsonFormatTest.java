package com.widehouse.cafe.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.widehouse.cafe.domain.cafe.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * Created by kiel on 2017. 2. 20..
 */
@RunWith(SpringRunner.class)
@JsonTest
public class JsonFormatTest {
    @Autowired
    private JacksonTester<Category> json;

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime datetime = LocalDateTime.of(2017, 2, 15, 13, 14, 15);
        Category category = new Category(1L, "test", datetime, 1);

        assertThat(this.json.write(category))
                .hasJsonPathStringValue("@.createDateTime");
        assertThat(this.json.write(category))
                .extractingJsonPathStringValue("@.createDateTime")
                .isEqualTo("2017-02-15T13:14:15");
    }
}
