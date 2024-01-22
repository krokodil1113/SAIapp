package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ticker.
 */
@Entity
@Table(name = "ticker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ticker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "description")
    private String description;

    @Column(name = "display_symbol")
    private String displaySymbol;

    @Column(name = "figi")
    private String figi;

    @Column(name = "mic")
    private String mic;

    @Column(name = "share_class_figi")
    private String shareClassFIGI;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "symbol_2")
    private String symbol2;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User ownedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ticker id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Ticker currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return this.description;
    }

    public Ticker description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplaySymbol() {
        return this.displaySymbol;
    }

    public Ticker displaySymbol(String displaySymbol) {
        this.setDisplaySymbol(displaySymbol);
        return this;
    }

    public void setDisplaySymbol(String displaySymbol) {
        this.displaySymbol = displaySymbol;
    }

    public String getFigi() {
        return this.figi;
    }

    public Ticker figi(String figi) {
        this.setFigi(figi);
        return this;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }

    public String getMic() {
        return this.mic;
    }

    public Ticker mic(String mic) {
        this.setMic(mic);
        return this;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public String getShareClassFIGI() {
        return this.shareClassFIGI;
    }

    public Ticker shareClassFIGI(String shareClassFIGI) {
        this.setShareClassFIGI(shareClassFIGI);
        return this;
    }

    public void setShareClassFIGI(String shareClassFIGI) {
        this.shareClassFIGI = shareClassFIGI;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Ticker symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol2() {
        return this.symbol2;
    }

    public Ticker symbol2(String symbol2) {
        this.setSymbol2(symbol2);
        return this;
    }

    public void setSymbol2(String symbol2) {
        this.symbol2 = symbol2;
    }

    public String getType() {
        return this.type;
    }

    public Ticker type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getOwnedBy() {
        return this.ownedBy;
    }

    public void setOwnedBy(User user) {
        this.ownedBy = user;
    }

    public Ticker ownedBy(User user) {
        this.setOwnedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticker)) {
            return false;
        }
        return getId() != null && getId().equals(((Ticker) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticker{" +
            "id=" + getId() +
            ", currency='" + getCurrency() + "'" +
            ", description='" + getDescription() + "'" +
            ", displaySymbol='" + getDisplaySymbol() + "'" +
            ", figi='" + getFigi() + "'" +
            ", mic='" + getMic() + "'" +
            ", shareClassFIGI='" + getShareClassFIGI() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", symbol2='" + getSymbol2() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
