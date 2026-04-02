package wingstars.backend.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.shortName;
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return Role.fromShortName(dbData);
    }
}
