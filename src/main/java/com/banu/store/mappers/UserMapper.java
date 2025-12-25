package com.banu.store.mappers;

import com.banu.store.entities.CreateNewUser;
import com.banu.store.entities.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User toEntity(CreateNewUser user);
}
