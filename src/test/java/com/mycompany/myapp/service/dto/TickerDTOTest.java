package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TickerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TickerDTO.class);
        TickerDTO tickerDTO1 = new TickerDTO();
        tickerDTO1.setId(1L);
        TickerDTO tickerDTO2 = new TickerDTO();
        assertThat(tickerDTO1).isNotEqualTo(tickerDTO2);
        tickerDTO2.setId(tickerDTO1.getId());
        assertThat(tickerDTO1).isEqualTo(tickerDTO2);
        tickerDTO2.setId(2L);
        assertThat(tickerDTO1).isNotEqualTo(tickerDTO2);
        tickerDTO1.setId(null);
        assertThat(tickerDTO1).isNotEqualTo(tickerDTO2);
    }
}
