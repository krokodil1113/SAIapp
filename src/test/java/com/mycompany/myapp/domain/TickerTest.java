package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.TickerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TickerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ticker.class);
        Ticker ticker1 = getTickerSample1();
        Ticker ticker2 = new Ticker();
        assertThat(ticker1).isNotEqualTo(ticker2);

        ticker2.setId(ticker1.getId());
        assertThat(ticker1).isEqualTo(ticker2);

        ticker2 = getTickerSample2();
        assertThat(ticker1).isNotEqualTo(ticker2);
    }
}
