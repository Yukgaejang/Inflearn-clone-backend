package com.yukgaejang.inflearnclone;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CicdTest {

    @Test
    public void failTest(){
        Assertions.assertThat(1).isEqualTo(2);
    }
}
