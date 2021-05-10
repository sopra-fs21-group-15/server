package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.helper.TimeStamp;
import ch.uzh.ifi.hase.soprafs21.rest.dto.TimeStringGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TimeDTOMapper {

    TimeDTOMapper INSTANCE = Mappers.getMapper(TimeDTOMapper.class);

    @Mapping(source = "timeString", target = "timeString")
    TimeStamp convertTimeStringGeTDTOtoEntity(TimeStringGetDTO timeStringGetDTO);
}
