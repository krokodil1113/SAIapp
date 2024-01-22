package com.mycompany.myapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.Ticker;
import com.mycompany.myapp.repository.TickerRepository;
import com.mycompany.myapp.service.TickerService;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.service.mapper.TickerMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Ticker}.
 */
@Service
@Transactional
public class TickerServiceImpl implements TickerService {

    private final Logger log = LoggerFactory.getLogger(TickerServiceImpl.class);

    private final TickerRepository tickerRepository;

    private final TickerMapper tickerMapper;

    /*  private final RestTemplate restTemplate; */

    /*  private final WebClient webClient; */

    public TickerServiceImpl(TickerRepository tickerRepository, TickerMapper tickerMapper) {
        this.tickerRepository = tickerRepository;
        this.tickerMapper = tickerMapper;
    }

    @Override
    public TickerDTO save(TickerDTO tickerDTO) {
        log.debug("Request to save Ticker : {}", tickerDTO);
        Ticker ticker = tickerMapper.toEntity(tickerDTO);
        ticker = tickerRepository.save(ticker);
        return tickerMapper.toDto(ticker);
    }

    @Override
    public TickerDTO update(TickerDTO tickerDTO) {
        log.debug("Request to update Ticker : {}", tickerDTO);
        Ticker ticker = tickerMapper.toEntity(tickerDTO);
        ticker = tickerRepository.save(ticker);
        return tickerMapper.toDto(ticker);
    }

    @Override
    public Optional<TickerDTO> partialUpdate(TickerDTO tickerDTO) {
        log.debug("Request to partially update Ticker : {}", tickerDTO);

        return tickerRepository
            .findById(tickerDTO.getId())
            .map(existingTicker -> {
                tickerMapper.partialUpdate(existingTicker, tickerDTO);

                return existingTicker;
            })
            .map(tickerRepository::save)
            .map(tickerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TickerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tickers");
        return tickerRepository.findAll(pageable).map(tickerMapper::toDto);
    }

    public Page<TickerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tickerRepository.findAllWithEagerRelationships(pageable).map(tickerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TickerDTO> findOne(Long id) {
        log.debug("Request to get Ticker : {}", id);
        return tickerRepository.findOneWithEagerRelationships(id).map(tickerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TickerDTO> findBySymbol(String symbol) {
        log.debug("Request to get Ticker : {}----------------------------------------", symbol);
        final String pythonApiUrl = "http://127.0.0.1:5000/items/" + symbol;

        System.out.println("URL Request to get ONE Ticker : {}---------------------------------------- " + pythonApiUrl);

        WebClient webClient = WebClient.create();

        /*  TickerDTO responseBody = webClient.get()
                .uri(pythonApiUrl)
                .retrieve()
                .bodyToMono(TickerDTO.class)
                .blockOptional()
                .orElse(null); */

        String responseBody = webClient.get().uri(pythonApiUrl).retrieve().bodyToMono(String.class).block();

        System.out.println("STRING Ticker : {}----------------------------------------" + responseBody);

        // Process the data obtained from the Python API
        TickerDTO tickerDTO = processSingleTickerFromJson(responseBody);

        return Optional.ofNullable(tickerDTO);
    }

    private TickerDTO processSingleTickerFromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON and get the "item" node
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode itemNode = rootNode.get("item");

            // Convert the "item" node to TickerDTO
            return objectMapper.treeToValue(itemNode, TickerDTO.class);
        } catch (JsonProcessingException e) {
            // Handle exception (e.g., log, throw, etc.)
            System.out.println("!!!!!!!!!!!ERORR");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ticker : {}", id);
        tickerRepository.deleteById(id);
    }
}
