package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ticker;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.TickerRepository;
import com.mycompany.myapp.service.TickerService;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.service.mapper.TickerMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TickerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TickerResourceIT {

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_FIGI = "AAAAAAAAAA";
    private static final String UPDATED_FIGI = "BBBBBBBBBB";

    private static final String DEFAULT_MIC = "AAAAAAAAAA";
    private static final String UPDATED_MIC = "BBBBBBBBBB";

    private static final String DEFAULT_SHARE_CLASS_FIGI = "AAAAAAAAAA";
    private static final String UPDATED_SHARE_CLASS_FIGI = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL_2 = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tickers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TickerRepository tickerRepository;

    @Mock
    private TickerRepository tickerRepositoryMock;

    @Autowired
    private TickerMapper tickerMapper;

    @Mock
    private TickerService tickerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTickerMockMvc;

    private Ticker ticker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticker createEntity(EntityManager em) {
        Ticker ticker = new Ticker()
            .currency(DEFAULT_CURRENCY)
            .description(DEFAULT_DESCRIPTION)
            .displaySymbol(DEFAULT_DISPLAY_SYMBOL)
            .figi(DEFAULT_FIGI)
            .mic(DEFAULT_MIC)
            .shareClassFIGI(DEFAULT_SHARE_CLASS_FIGI)
            .symbol(DEFAULT_SYMBOL)
            .symbol2(DEFAULT_SYMBOL_2)
            .type(DEFAULT_TYPE);
        return ticker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticker createUpdatedEntity(EntityManager em) {
        Ticker ticker = new Ticker()
            .currency(UPDATED_CURRENCY)
            .description(UPDATED_DESCRIPTION)
            .displaySymbol(UPDATED_DISPLAY_SYMBOL)
            .figi(UPDATED_FIGI)
            .mic(UPDATED_MIC)
            .shareClassFIGI(UPDATED_SHARE_CLASS_FIGI)
            .symbol(UPDATED_SYMBOL)
            .symbol2(UPDATED_SYMBOL_2)
            .type(UPDATED_TYPE);
        return ticker;
    }

    @BeforeEach
    public void initTest() {
        ticker = createEntity(em);
    }

    @Test
    @Transactional
    void createTicker() throws Exception {
        int databaseSizeBeforeCreate = tickerRepository.findAll().size();
        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);
        restTickerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDTO)))
            .andExpect(status().isCreated());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeCreate + 1);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testTicker.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicker.getDisplaySymbol()).isEqualTo(DEFAULT_DISPLAY_SYMBOL);
        assertThat(testTicker.getFigi()).isEqualTo(DEFAULT_FIGI);
        assertThat(testTicker.getMic()).isEqualTo(DEFAULT_MIC);
        assertThat(testTicker.getShareClassFIGI()).isEqualTo(DEFAULT_SHARE_CLASS_FIGI);
        assertThat(testTicker.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTicker.getSymbol2()).isEqualTo(DEFAULT_SYMBOL_2);
        assertThat(testTicker.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createTickerWithExistingId() throws Exception {
        // Create the Ticker with an existing ID
        ticker.setId(1L);
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        int databaseSizeBeforeCreate = tickerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTickerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTickers() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList
        restTickerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticker.getId().intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].displaySymbol").value(hasItem(DEFAULT_DISPLAY_SYMBOL)))
            .andExpect(jsonPath("$.[*].figi").value(hasItem(DEFAULT_FIGI)))
            .andExpect(jsonPath("$.[*].mic").value(hasItem(DEFAULT_MIC)))
            .andExpect(jsonPath("$.[*].shareClassFIGI").value(hasItem(DEFAULT_SHARE_CLASS_FIGI)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].symbol2").value(hasItem(DEFAULT_SYMBOL_2)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTickersWithEagerRelationshipsIsEnabled() throws Exception {
        when(tickerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTickerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tickerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTickersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tickerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTickerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tickerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get the ticker
        restTickerMockMvc
            .perform(get(ENTITY_API_URL_ID, ticker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticker.getId().intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.displaySymbol").value(DEFAULT_DISPLAY_SYMBOL))
            .andExpect(jsonPath("$.figi").value(DEFAULT_FIGI))
            .andExpect(jsonPath("$.mic").value(DEFAULT_MIC))
            .andExpect(jsonPath("$.shareClassFIGI").value(DEFAULT_SHARE_CLASS_FIGI))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.symbol2").value(DEFAULT_SYMBOL_2))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getTickersByIdFiltering() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        Long id = ticker.getId();

        defaultTickerShouldBeFound("id.equals=" + id);
        defaultTickerShouldNotBeFound("id.notEquals=" + id);

        defaultTickerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTickerShouldNotBeFound("id.greaterThan=" + id);

        defaultTickerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTickerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTickersByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where currency equals to DEFAULT_CURRENCY
        defaultTickerShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the tickerList where currency equals to UPDATED_CURRENCY
        defaultTickerShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTickersByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultTickerShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the tickerList where currency equals to UPDATED_CURRENCY
        defaultTickerShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTickersByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where currency is not null
        defaultTickerShouldBeFound("currency.specified=true");

        // Get all the tickerList where currency is null
        defaultTickerShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where currency contains DEFAULT_CURRENCY
        defaultTickerShouldBeFound("currency.contains=" + DEFAULT_CURRENCY);

        // Get all the tickerList where currency contains UPDATED_CURRENCY
        defaultTickerShouldNotBeFound("currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTickersByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where currency does not contain DEFAULT_CURRENCY
        defaultTickerShouldNotBeFound("currency.doesNotContain=" + DEFAULT_CURRENCY);

        // Get all the tickerList where currency does not contain UPDATED_CURRENCY
        defaultTickerShouldBeFound("currency.doesNotContain=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTickersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where description equals to DEFAULT_DESCRIPTION
        defaultTickerShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the tickerList where description equals to UPDATED_DESCRIPTION
        defaultTickerShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTickersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTickerShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the tickerList where description equals to UPDATED_DESCRIPTION
        defaultTickerShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTickersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where description is not null
        defaultTickerShouldBeFound("description.specified=true");

        // Get all the tickerList where description is null
        defaultTickerShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where description contains DEFAULT_DESCRIPTION
        defaultTickerShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the tickerList where description contains UPDATED_DESCRIPTION
        defaultTickerShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTickersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where description does not contain DEFAULT_DESCRIPTION
        defaultTickerShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the tickerList where description does not contain UPDATED_DESCRIPTION
        defaultTickerShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTickersByDisplaySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where displaySymbol equals to DEFAULT_DISPLAY_SYMBOL
        defaultTickerShouldBeFound("displaySymbol.equals=" + DEFAULT_DISPLAY_SYMBOL);

        // Get all the tickerList where displaySymbol equals to UPDATED_DISPLAY_SYMBOL
        defaultTickerShouldNotBeFound("displaySymbol.equals=" + UPDATED_DISPLAY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllTickersByDisplaySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where displaySymbol in DEFAULT_DISPLAY_SYMBOL or UPDATED_DISPLAY_SYMBOL
        defaultTickerShouldBeFound("displaySymbol.in=" + DEFAULT_DISPLAY_SYMBOL + "," + UPDATED_DISPLAY_SYMBOL);

        // Get all the tickerList where displaySymbol equals to UPDATED_DISPLAY_SYMBOL
        defaultTickerShouldNotBeFound("displaySymbol.in=" + UPDATED_DISPLAY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllTickersByDisplaySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where displaySymbol is not null
        defaultTickerShouldBeFound("displaySymbol.specified=true");

        // Get all the tickerList where displaySymbol is null
        defaultTickerShouldNotBeFound("displaySymbol.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersByDisplaySymbolContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where displaySymbol contains DEFAULT_DISPLAY_SYMBOL
        defaultTickerShouldBeFound("displaySymbol.contains=" + DEFAULT_DISPLAY_SYMBOL);

        // Get all the tickerList where displaySymbol contains UPDATED_DISPLAY_SYMBOL
        defaultTickerShouldNotBeFound("displaySymbol.contains=" + UPDATED_DISPLAY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllTickersByDisplaySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where displaySymbol does not contain DEFAULT_DISPLAY_SYMBOL
        defaultTickerShouldNotBeFound("displaySymbol.doesNotContain=" + DEFAULT_DISPLAY_SYMBOL);

        // Get all the tickerList where displaySymbol does not contain UPDATED_DISPLAY_SYMBOL
        defaultTickerShouldBeFound("displaySymbol.doesNotContain=" + UPDATED_DISPLAY_SYMBOL);
    }

    @Test
    @Transactional
    void getAllTickersByFigiIsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where figi equals to DEFAULT_FIGI
        defaultTickerShouldBeFound("figi.equals=" + DEFAULT_FIGI);

        // Get all the tickerList where figi equals to UPDATED_FIGI
        defaultTickerShouldNotBeFound("figi.equals=" + UPDATED_FIGI);
    }

    @Test
    @Transactional
    void getAllTickersByFigiIsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where figi in DEFAULT_FIGI or UPDATED_FIGI
        defaultTickerShouldBeFound("figi.in=" + DEFAULT_FIGI + "," + UPDATED_FIGI);

        // Get all the tickerList where figi equals to UPDATED_FIGI
        defaultTickerShouldNotBeFound("figi.in=" + UPDATED_FIGI);
    }

    @Test
    @Transactional
    void getAllTickersByFigiIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where figi is not null
        defaultTickerShouldBeFound("figi.specified=true");

        // Get all the tickerList where figi is null
        defaultTickerShouldNotBeFound("figi.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersByFigiContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where figi contains DEFAULT_FIGI
        defaultTickerShouldBeFound("figi.contains=" + DEFAULT_FIGI);

        // Get all the tickerList where figi contains UPDATED_FIGI
        defaultTickerShouldNotBeFound("figi.contains=" + UPDATED_FIGI);
    }

    @Test
    @Transactional
    void getAllTickersByFigiNotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where figi does not contain DEFAULT_FIGI
        defaultTickerShouldNotBeFound("figi.doesNotContain=" + DEFAULT_FIGI);

        // Get all the tickerList where figi does not contain UPDATED_FIGI
        defaultTickerShouldBeFound("figi.doesNotContain=" + UPDATED_FIGI);
    }

    @Test
    @Transactional
    void getAllTickersByMicIsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where mic equals to DEFAULT_MIC
        defaultTickerShouldBeFound("mic.equals=" + DEFAULT_MIC);

        // Get all the tickerList where mic equals to UPDATED_MIC
        defaultTickerShouldNotBeFound("mic.equals=" + UPDATED_MIC);
    }

    @Test
    @Transactional
    void getAllTickersByMicIsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where mic in DEFAULT_MIC or UPDATED_MIC
        defaultTickerShouldBeFound("mic.in=" + DEFAULT_MIC + "," + UPDATED_MIC);

        // Get all the tickerList where mic equals to UPDATED_MIC
        defaultTickerShouldNotBeFound("mic.in=" + UPDATED_MIC);
    }

    @Test
    @Transactional
    void getAllTickersByMicIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where mic is not null
        defaultTickerShouldBeFound("mic.specified=true");

        // Get all the tickerList where mic is null
        defaultTickerShouldNotBeFound("mic.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersByMicContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where mic contains DEFAULT_MIC
        defaultTickerShouldBeFound("mic.contains=" + DEFAULT_MIC);

        // Get all the tickerList where mic contains UPDATED_MIC
        defaultTickerShouldNotBeFound("mic.contains=" + UPDATED_MIC);
    }

    @Test
    @Transactional
    void getAllTickersByMicNotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where mic does not contain DEFAULT_MIC
        defaultTickerShouldNotBeFound("mic.doesNotContain=" + DEFAULT_MIC);

        // Get all the tickerList where mic does not contain UPDATED_MIC
        defaultTickerShouldBeFound("mic.doesNotContain=" + UPDATED_MIC);
    }

    @Test
    @Transactional
    void getAllTickersByShareClassFIGIIsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where shareClassFIGI equals to DEFAULT_SHARE_CLASS_FIGI
        defaultTickerShouldBeFound("shareClassFIGI.equals=" + DEFAULT_SHARE_CLASS_FIGI);

        // Get all the tickerList where shareClassFIGI equals to UPDATED_SHARE_CLASS_FIGI
        defaultTickerShouldNotBeFound("shareClassFIGI.equals=" + UPDATED_SHARE_CLASS_FIGI);
    }

    @Test
    @Transactional
    void getAllTickersByShareClassFIGIIsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where shareClassFIGI in DEFAULT_SHARE_CLASS_FIGI or UPDATED_SHARE_CLASS_FIGI
        defaultTickerShouldBeFound("shareClassFIGI.in=" + DEFAULT_SHARE_CLASS_FIGI + "," + UPDATED_SHARE_CLASS_FIGI);

        // Get all the tickerList where shareClassFIGI equals to UPDATED_SHARE_CLASS_FIGI
        defaultTickerShouldNotBeFound("shareClassFIGI.in=" + UPDATED_SHARE_CLASS_FIGI);
    }

    @Test
    @Transactional
    void getAllTickersByShareClassFIGIIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where shareClassFIGI is not null
        defaultTickerShouldBeFound("shareClassFIGI.specified=true");

        // Get all the tickerList where shareClassFIGI is null
        defaultTickerShouldNotBeFound("shareClassFIGI.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersByShareClassFIGIContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where shareClassFIGI contains DEFAULT_SHARE_CLASS_FIGI
        defaultTickerShouldBeFound("shareClassFIGI.contains=" + DEFAULT_SHARE_CLASS_FIGI);

        // Get all the tickerList where shareClassFIGI contains UPDATED_SHARE_CLASS_FIGI
        defaultTickerShouldNotBeFound("shareClassFIGI.contains=" + UPDATED_SHARE_CLASS_FIGI);
    }

    @Test
    @Transactional
    void getAllTickersByShareClassFIGINotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where shareClassFIGI does not contain DEFAULT_SHARE_CLASS_FIGI
        defaultTickerShouldNotBeFound("shareClassFIGI.doesNotContain=" + DEFAULT_SHARE_CLASS_FIGI);

        // Get all the tickerList where shareClassFIGI does not contain UPDATED_SHARE_CLASS_FIGI
        defaultTickerShouldBeFound("shareClassFIGI.doesNotContain=" + UPDATED_SHARE_CLASS_FIGI);
    }

    @Test
    @Transactional
    void getAllTickersBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol equals to DEFAULT_SYMBOL
        defaultTickerShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the tickerList where symbol equals to UPDATED_SYMBOL
        defaultTickerShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllTickersBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultTickerShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the tickerList where symbol equals to UPDATED_SYMBOL
        defaultTickerShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllTickersBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol is not null
        defaultTickerShouldBeFound("symbol.specified=true");

        // Get all the tickerList where symbol is null
        defaultTickerShouldNotBeFound("symbol.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersBySymbolContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol contains DEFAULT_SYMBOL
        defaultTickerShouldBeFound("symbol.contains=" + DEFAULT_SYMBOL);

        // Get all the tickerList where symbol contains UPDATED_SYMBOL
        defaultTickerShouldNotBeFound("symbol.contains=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllTickersBySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol does not contain DEFAULT_SYMBOL
        defaultTickerShouldNotBeFound("symbol.doesNotContain=" + DEFAULT_SYMBOL);

        // Get all the tickerList where symbol does not contain UPDATED_SYMBOL
        defaultTickerShouldBeFound("symbol.doesNotContain=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllTickersBySymbol2IsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol2 equals to DEFAULT_SYMBOL_2
        defaultTickerShouldBeFound("symbol2.equals=" + DEFAULT_SYMBOL_2);

        // Get all the tickerList where symbol2 equals to UPDATED_SYMBOL_2
        defaultTickerShouldNotBeFound("symbol2.equals=" + UPDATED_SYMBOL_2);
    }

    @Test
    @Transactional
    void getAllTickersBySymbol2IsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol2 in DEFAULT_SYMBOL_2 or UPDATED_SYMBOL_2
        defaultTickerShouldBeFound("symbol2.in=" + DEFAULT_SYMBOL_2 + "," + UPDATED_SYMBOL_2);

        // Get all the tickerList where symbol2 equals to UPDATED_SYMBOL_2
        defaultTickerShouldNotBeFound("symbol2.in=" + UPDATED_SYMBOL_2);
    }

    @Test
    @Transactional
    void getAllTickersBySymbol2IsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol2 is not null
        defaultTickerShouldBeFound("symbol2.specified=true");

        // Get all the tickerList where symbol2 is null
        defaultTickerShouldNotBeFound("symbol2.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersBySymbol2ContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol2 contains DEFAULT_SYMBOL_2
        defaultTickerShouldBeFound("symbol2.contains=" + DEFAULT_SYMBOL_2);

        // Get all the tickerList where symbol2 contains UPDATED_SYMBOL_2
        defaultTickerShouldNotBeFound("symbol2.contains=" + UPDATED_SYMBOL_2);
    }

    @Test
    @Transactional
    void getAllTickersBySymbol2NotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where symbol2 does not contain DEFAULT_SYMBOL_2
        defaultTickerShouldNotBeFound("symbol2.doesNotContain=" + DEFAULT_SYMBOL_2);

        // Get all the tickerList where symbol2 does not contain UPDATED_SYMBOL_2
        defaultTickerShouldBeFound("symbol2.doesNotContain=" + UPDATED_SYMBOL_2);
    }

    @Test
    @Transactional
    void getAllTickersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where type equals to DEFAULT_TYPE
        defaultTickerShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the tickerList where type equals to UPDATED_TYPE
        defaultTickerShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTickersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTickerShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the tickerList where type equals to UPDATED_TYPE
        defaultTickerShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTickersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where type is not null
        defaultTickerShouldBeFound("type.specified=true");

        // Get all the tickerList where type is null
        defaultTickerShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllTickersByTypeContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where type contains DEFAULT_TYPE
        defaultTickerShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the tickerList where type contains UPDATED_TYPE
        defaultTickerShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTickersByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList where type does not contain DEFAULT_TYPE
        defaultTickerShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the tickerList where type does not contain UPDATED_TYPE
        defaultTickerShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTickersByOwnedByIsEqualToSomething() throws Exception {
        User ownedBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            tickerRepository.saveAndFlush(ticker);
            ownedBy = UserResourceIT.createEntity(em);
        } else {
            ownedBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(ownedBy);
        em.flush();
        ticker.setOwnedBy(ownedBy);
        tickerRepository.saveAndFlush(ticker);
        Long ownedById = ownedBy.getId();
        // Get all the tickerList where ownedBy equals to ownedById
        defaultTickerShouldBeFound("ownedById.equals=" + ownedById);

        // Get all the tickerList where ownedBy equals to (ownedById + 1)
        defaultTickerShouldNotBeFound("ownedById.equals=" + (ownedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTickerShouldBeFound(String filter) throws Exception {
        restTickerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticker.getId().intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].displaySymbol").value(hasItem(DEFAULT_DISPLAY_SYMBOL)))
            .andExpect(jsonPath("$.[*].figi").value(hasItem(DEFAULT_FIGI)))
            .andExpect(jsonPath("$.[*].mic").value(hasItem(DEFAULT_MIC)))
            .andExpect(jsonPath("$.[*].shareClassFIGI").value(hasItem(DEFAULT_SHARE_CLASS_FIGI)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].symbol2").value(hasItem(DEFAULT_SYMBOL_2)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restTickerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTickerShouldNotBeFound(String filter) throws Exception {
        restTickerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTickerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTicker() throws Exception {
        // Get the ticker
        restTickerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker
        Ticker updatedTicker = tickerRepository.findById(ticker.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTicker are not directly saved in db
        em.detach(updatedTicker);
        updatedTicker
            .currency(UPDATED_CURRENCY)
            .description(UPDATED_DESCRIPTION)
            .displaySymbol(UPDATED_DISPLAY_SYMBOL)
            .figi(UPDATED_FIGI)
            .mic(UPDATED_MIC)
            .shareClassFIGI(UPDATED_SHARE_CLASS_FIGI)
            .symbol(UPDATED_SYMBOL)
            .symbol2(UPDATED_SYMBOL_2)
            .type(UPDATED_TYPE);
        TickerDTO tickerDTO = tickerMapper.toDto(updatedTicker);

        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testTicker.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicker.getDisplaySymbol()).isEqualTo(UPDATED_DISPLAY_SYMBOL);
        assertThat(testTicker.getFigi()).isEqualTo(UPDATED_FIGI);
        assertThat(testTicker.getMic()).isEqualTo(UPDATED_MIC);
        assertThat(testTicker.getShareClassFIGI()).isEqualTo(UPDATED_SHARE_CLASS_FIGI);
        assertThat(testTicker.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testTicker.getSymbol2()).isEqualTo(UPDATED_SYMBOL_2);
        assertThat(testTicker.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTickerWithPatch() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker using partial update
        Ticker partialUpdatedTicker = new Ticker();
        partialUpdatedTicker.setId(ticker.getId());

        partialUpdatedTicker
            .currency(UPDATED_CURRENCY)
            .description(UPDATED_DESCRIPTION)
            .displaySymbol(UPDATED_DISPLAY_SYMBOL)
            .figi(UPDATED_FIGI)
            .mic(UPDATED_MIC)
            .symbol(UPDATED_SYMBOL)
            .symbol2(UPDATED_SYMBOL_2)
            .type(UPDATED_TYPE);

        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicker))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testTicker.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicker.getDisplaySymbol()).isEqualTo(UPDATED_DISPLAY_SYMBOL);
        assertThat(testTicker.getFigi()).isEqualTo(UPDATED_FIGI);
        assertThat(testTicker.getMic()).isEqualTo(UPDATED_MIC);
        assertThat(testTicker.getShareClassFIGI()).isEqualTo(DEFAULT_SHARE_CLASS_FIGI);
        assertThat(testTicker.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testTicker.getSymbol2()).isEqualTo(UPDATED_SYMBOL_2);
        assertThat(testTicker.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTickerWithPatch() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker using partial update
        Ticker partialUpdatedTicker = new Ticker();
        partialUpdatedTicker.setId(ticker.getId());

        partialUpdatedTicker
            .currency(UPDATED_CURRENCY)
            .description(UPDATED_DESCRIPTION)
            .displaySymbol(UPDATED_DISPLAY_SYMBOL)
            .figi(UPDATED_FIGI)
            .mic(UPDATED_MIC)
            .shareClassFIGI(UPDATED_SHARE_CLASS_FIGI)
            .symbol(UPDATED_SYMBOL)
            .symbol2(UPDATED_SYMBOL_2)
            .type(UPDATED_TYPE);

        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicker))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testTicker.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicker.getDisplaySymbol()).isEqualTo(UPDATED_DISPLAY_SYMBOL);
        assertThat(testTicker.getFigi()).isEqualTo(UPDATED_FIGI);
        assertThat(testTicker.getMic()).isEqualTo(UPDATED_MIC);
        assertThat(testTicker.getShareClassFIGI()).isEqualTo(UPDATED_SHARE_CLASS_FIGI);
        assertThat(testTicker.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testTicker.getSymbol2()).isEqualTo(UPDATED_SYMBOL_2);
        assertThat(testTicker.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tickerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(longCount.incrementAndGet());

        // Create the Ticker
        TickerDTO tickerDTO = tickerMapper.toDto(ticker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tickerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeDelete = tickerRepository.findAll().size();

        // Delete the ticker
        restTickerMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
