package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Ticker;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.TickerDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ticker} and its DTO {@link TickerDTO}.
 */
@Mapper(componentModel = "spring")
public interface TickerMapper extends EntityMapper<TickerDTO, Ticker> {
    @Mapping(target = "ownedBy", source = "ownedBy", qualifiedByName = "userLogin")
    TickerDTO toDto(Ticker s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
