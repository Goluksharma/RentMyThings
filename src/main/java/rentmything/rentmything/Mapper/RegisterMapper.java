package rentmything.rentmything.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rentmything.rentmything.Dto.RegisterDto;
import rentmything.rentmything.model.User;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    // For creating new users — ignore fields handled manually or by JPA
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(RegisterDto dto);

    // For updating existing users safely — ignore all fields that shouldn’t be
    // touched
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateUserFromDto(RegisterDto dto, @MappingTarget User user);
}
