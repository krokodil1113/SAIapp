package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TickerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Ticker}.
 */
public interface TickerService {
    /**
     * Save a ticker.
     *
     * @param tickerDTO the entity to save.
     * @return the persisted entity.
     */
    TickerDTO save(TickerDTO tickerDTO);

    /**
     * Updates a ticker.
     *
     * @param tickerDTO the entity to update.
     * @return the persisted entity.
     */
    TickerDTO update(TickerDTO tickerDTO);

    /**
     * Partially updates a ticker.
     *
     * @param tickerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TickerDTO> partialUpdate(TickerDTO tickerDTO);

    /**
     * Get all the tickers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TickerDTO> findAll(Pageable pageable);

    /**
     * Get all the tickers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TickerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" ticker.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TickerDTO> findOne(Long id);

    /**
     * Get the "symbol" ticker.
     *
     * @param symbol the symbol of the entity.
     * @return the entity.
     */
    Optional<TickerDTO> findBySymbol(String symbol);

    /**
     * Delete the "id" ticker.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
