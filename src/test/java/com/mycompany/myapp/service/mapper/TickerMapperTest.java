package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TickerMapperTest {

    private TickerMapper tickerMapper;

    @BeforeEach
    public void setUp() {
        tickerMapper = new TickerMapperImpl();
    }
}
