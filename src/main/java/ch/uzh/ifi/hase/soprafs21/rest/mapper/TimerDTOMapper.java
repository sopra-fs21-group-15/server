package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Timer;
import ch.uzh.ifi.hase.soprafs21.rest.dto.TimerGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TimerDTOMapper {

    Timer INSTANCE = Mappers.getMapper(Timer.class);

    @Mapping(source = "timeSpan", target = "timeSpan")
    @Mapping(source = "start", target = "start")
    TimerGetDTO convertEntityToTimerGetDTO(Timer timer);
}
