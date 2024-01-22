package com.mycompany.myapp.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Ticker} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerDTO implements Serializable {

    private Long id;

    private String currency;

    private String description;

    private String displaySymbol;

    private String figi;

    private String isin;

    private String mic;

    private String shareClassFIGI;

    private String symbol;

    private String symbol2;

    private String type;

    private UserDTO ownedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getIsin() {
        return isin;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public void setDisplaySymbol(String displaySymbol) {
        this.displaySymbol = displaySymbol;
    }

    public String getFigi() {
        return figi;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public String getShareClassFIGI() {
        return shareClassFIGI;
    }

    public void setShareClassFIGI(String shareClassFIGI) {
        this.shareClassFIGI = shareClassFIGI;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol2() {
        return symbol2;
    }

    public void setSymbol2(String symbol2) {
        this.symbol2 = symbol2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserDTO getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(UserDTO ownedBy) {
        this.ownedBy = ownedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TickerDTO)) {
            return false;
        }

        TickerDTO tickerDTO = (TickerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tickerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TickerDTO{" +
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
            ", ownedBy=" + getOwnedBy() +
            "}";
    }
}
