package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TickerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ticker getTickerSample1() {
        return new Ticker()
            .id(1L)
            .currency("currency1")
            .description("description1")
            .displaySymbol("displaySymbol1")
            .figi("figi1")
            .mic("mic1")
            .shareClassFIGI("shareClassFIGI1")
            .symbol("symbol1")
            .symbol2("symbol21")
            .type("type1");
    }

    public static Ticker getTickerSample2() {
        return new Ticker()
            .id(2L)
            .currency("currency2")
            .description("description2")
            .displaySymbol("displaySymbol2")
            .figi("figi2")
            .mic("mic2")
            .shareClassFIGI("shareClassFIGI2")
            .symbol("symbol2")
            .symbol2("symbol22")
            .type("type2");
    }

    public static Ticker getTickerRandomSampleGenerator() {
        return new Ticker()
            .id(longCount.incrementAndGet())
            .currency(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .displaySymbol(UUID.randomUUID().toString())
            .figi(UUID.randomUUID().toString())
            .mic(UUID.randomUUID().toString())
            .shareClassFIGI(UUID.randomUUID().toString())
            .symbol(UUID.randomUUID().toString())
            .symbol2(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString());
    }
}
