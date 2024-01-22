package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Ticker} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TickerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TickerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter currency;

    private StringFilter description;

    private StringFilter displaySymbol;

    private StringFilter figi;

    private StringFilter mic;

    private StringFilter shareClassFIGI;

    private StringFilter symbol;

    private StringFilter symbol2;

    private StringFilter type;

    private LongFilter ownedById;

    private Boolean distinct;

    public TickerCriteria() {}

    public TickerCriteria(TickerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.currency = other.currency == null ? null : other.currency.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.displaySymbol = other.displaySymbol == null ? null : other.displaySymbol.copy();
        this.figi = other.figi == null ? null : other.figi.copy();
        this.mic = other.mic == null ? null : other.mic.copy();
        this.shareClassFIGI = other.shareClassFIGI == null ? null : other.shareClassFIGI.copy();
        this.symbol = other.symbol == null ? null : other.symbol.copy();
        this.symbol2 = other.symbol2 == null ? null : other.symbol2.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.ownedById = other.ownedById == null ? null : other.ownedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TickerCriteria copy() {
        return new TickerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public StringFilter currency() {
        if (currency == null) {
            currency = new StringFilter();
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getDisplaySymbol() {
        return displaySymbol;
    }

    public StringFilter displaySymbol() {
        if (displaySymbol == null) {
            displaySymbol = new StringFilter();
        }
        return displaySymbol;
    }

    public void setDisplaySymbol(StringFilter displaySymbol) {
        this.displaySymbol = displaySymbol;
    }

    public StringFilter getFigi() {
        return figi;
    }

    public StringFilter figi() {
        if (figi == null) {
            figi = new StringFilter();
        }
        return figi;
    }

    public void setFigi(StringFilter figi) {
        this.figi = figi;
    }

    public StringFilter getMic() {
        return mic;
    }

    public StringFilter mic() {
        if (mic == null) {
            mic = new StringFilter();
        }
        return mic;
    }

    public void setMic(StringFilter mic) {
        this.mic = mic;
    }

    public StringFilter getShareClassFIGI() {
        return shareClassFIGI;
    }

    public StringFilter shareClassFIGI() {
        if (shareClassFIGI == null) {
            shareClassFIGI = new StringFilter();
        }
        return shareClassFIGI;
    }

    public void setShareClassFIGI(StringFilter shareClassFIGI) {
        this.shareClassFIGI = shareClassFIGI;
    }

    public StringFilter getSymbol() {
        return symbol;
    }

    public StringFilter symbol() {
        if (symbol == null) {
            symbol = new StringFilter();
        }
        return symbol;
    }

    public void setSymbol(StringFilter symbol) {
        this.symbol = symbol;
    }

    public StringFilter getSymbol2() {
        return symbol2;
    }

    public StringFilter symbol2() {
        if (symbol2 == null) {
            symbol2 = new StringFilter();
        }
        return symbol2;
    }

    public void setSymbol2(StringFilter symbol2) {
        this.symbol2 = symbol2;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getOwnedById() {
        return ownedById;
    }

    public LongFilter ownedById() {
        if (ownedById == null) {
            ownedById = new LongFilter();
        }
        return ownedById;
    }

    public void setOwnedById(LongFilter ownedById) {
        this.ownedById = ownedById;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TickerCriteria that = (TickerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(description, that.description) &&
            Objects.equals(displaySymbol, that.displaySymbol) &&
            Objects.equals(figi, that.figi) &&
            Objects.equals(mic, that.mic) &&
            Objects.equals(shareClassFIGI, that.shareClassFIGI) &&
            Objects.equals(symbol, that.symbol) &&
            Objects.equals(symbol2, that.symbol2) &&
            Objects.equals(type, that.type) &&
            Objects.equals(ownedById, that.ownedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            currency,
            description,
            displaySymbol,
            figi,
            mic,
            shareClassFIGI,
            symbol,
            symbol2,
            type,
            ownedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TickerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (currency != null ? "currency=" + currency + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (displaySymbol != null ? "displaySymbol=" + displaySymbol + ", " : "") +
            (figi != null ? "figi=" + figi + ", " : "") +
            (mic != null ? "mic=" + mic + ", " : "") +
            (shareClassFIGI != null ? "shareClassFIGI=" + shareClassFIGI + ", " : "") +
            (symbol != null ? "symbol=" + symbol + ", " : "") +
            (symbol2 != null ? "symbol2=" + symbol2 + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (ownedById != null ? "ownedById=" + ownedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
