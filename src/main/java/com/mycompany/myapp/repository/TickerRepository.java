package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ticker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ticker entity.
 */
@Repository
public interface TickerRepository extends JpaRepository<Ticker, Long>, JpaSpecificationExecutor<Ticker> {
    @Query("select ticker from Ticker ticker where ticker.ownedBy.login = ?#{principal.username}")
    List<Ticker> findByOwnedByIsCurrentUser();

    default Optional<Ticker> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ticker> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ticker> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select ticker from Ticker ticker left join fetch ticker.ownedBy",
        countQuery = "select count(ticker) from Ticker ticker"
    )
    Page<Ticker> findAllWithToOneRelationships(Pageable pageable);

    @Query("select ticker from Ticker ticker left join fetch ticker.ownedBy")
    List<Ticker> findAllWithToOneRelationships();

    @Query("select ticker from Ticker ticker left join fetch ticker.ownedBy where ticker.id =:id")
    Optional<Ticker> findOneWithToOneRelationships(@Param("id") Long id);
}
