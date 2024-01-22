package com.mycompany.myapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.TickerRepository;
import com.mycompany.myapp.service.criteria.TickerCriteria;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.service.mapper.TickerMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ticker} entities in the database.
 * The main input is a {@link TickerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TickerDTO} or a {@link Page} of {@link TickerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TickerQueryService extends QueryService<Ticker> {

    private final Logger log = LoggerFactory.getLogger(TickerQueryService.class);

    private final TickerRepository tickerRepository;

    private final TickerMapper tickerMapper;

    public TickerQueryService(TickerRepository tickerRepository, TickerMapper tickerMapper) {
        this.tickerRepository = tickerRepository;
        this.tickerMapper = tickerMapper;
    }

    /**
     * Return a {@link List} of {@link TickerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TickerDTO> findByCriteria(TickerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ticker> specification = createSpecification(criteria);
        return tickerMapper.toDto(tickerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TickerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    /*  @Transactional(readOnly = true)
    public Page<TickerDTO> findByCriteria(TickerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ticker> specification = createSpecification(criteria);

       // http://127.0.0.1:5000/items
        final String pythonApiUrl = "http://127.0.0.1:5000/items";

         WebClient webClient = WebClient.create();

        // Make a GET request to the Python API

         Page<TickerDTO> allTickers = 
                 webClient.get()
                .uri(pythonApiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block(); 
        // block() is used here for simplicity, but in a real application, you might want to handle the response asynchronously
        return tickerRepository.findAll(specification, page).map(tickerMapper::toDto);
    } */

    @Transactional(readOnly = true)
    public Page<TickerDTO> findByCriteria(TickerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ticker> specification = createSpecification(criteria);

        // Set up WebClient

        final String pythonApiUrl = "http://127.0.0.1:5000/items?page=0&size=10";
        // WebClient webClient = WebClient.create();

        WebClient webClient = WebClient
            .builder()
            .exchangeStrategies(
                ExchangeStrategies
                    .builder()
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(6 * 1024 * 1024)) // 10 MB buffer size
                    .build()
            )
            .build();

        // Make a GET request to the Python API and retrieve the data asynchronously

        String pythonApiData = webClient.get().uri(pythonApiUrl).retrieve().bodyToMono(String.class).block(); // block() is used here for simplicity, handle the response asynchronously in a real application

        // Process the data obtained from the Python API (replace this with your own logic)

        List<TickerDTO> pythonApiTickers = processPythonApiData(pythonApiData);
        int pageSize = 10;
        List<TickerDTO> list = new ArrayList<>();
        for (int i = 9; i < 11; i++) {
            System.out.println("EVO GA" + i);
            System.out.println(pythonApiTickers.get(i));
            list.add(pythonApiTickers.get(i));
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println("Ticker------------------ " + list.get(i));
        }

        // Create a Page based on the data from the Python API
        return new PageImpl<>(list, PageRequest.of(0, pageSize), pythonApiTickers.size());
    }

    private List<TickerDTO> processPythonApiData(String pythonApiData) {
        try {
            // Parse the JSON data and get the "items" array
            JsonNode root = new ObjectMapper().readTree(pythonApiData);
            JsonNode itemsNode = root.get("items");

            // Deserialize the "items" array into a list of TickerDTO
            return Arrays.asList(new ObjectMapper().treeToValue(itemsNode, TickerDTO[].class));
        } catch (JsonProcessingException e) {
            // Handle the exception or log the error
            e.printStackTrace();
            return Collections.emptyList(); // or return null, depending on your use case
        }
    }

    /* private List<TickerDTO> processPythonApiData(String pythonApiData) {
    // Implement your logic to parse and process data from the Python API
    // Replace this with your own logic to map the Python API data to TickerDTO objects
    // For demonstration purposes, let's assume the data is a JSON array of TickerDTO objects
    try {
        return Arrays.asList(new ObjectMapper().readValue(pythonApiData, TickerDTO[].class));
    } catch (JsonMappingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
} */

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TickerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ticker> specification = createSpecification(criteria);
        return tickerRepository.count(specification);
    }

    /**
     * Function to convert {@link TickerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ticker> createSpecification(TickerCriteria criteria) {
        Specification<Ticker> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ticker_.id));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), Ticker_.currency));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Ticker_.description));
            }
            if (criteria.getDisplaySymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplaySymbol(), Ticker_.displaySymbol));
            }
            if (criteria.getFigi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFigi(), Ticker_.figi));
            }
            if (criteria.getMic() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMic(), Ticker_.mic));
            }
            if (criteria.getShareClassFIGI() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShareClassFIGI(), Ticker_.shareClassFIGI));
            }
            if (criteria.getSymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymbol(), Ticker_.symbol));
            }
            if (criteria.getSymbol2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymbol2(), Ticker_.symbol2));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Ticker_.type));
            }
            if (criteria.getOwnedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnedById(), root -> root.join(Ticker_.ownedBy, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
