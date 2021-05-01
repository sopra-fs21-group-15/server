package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Drawing;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawingGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawingPostDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

public interface DrawingDTOMapper {

    DrawingDTOMapper INSTANCE = Mappers.getMapper(DrawingDTOMapper.class);

    @Mapping(source = "timestamp", target = "timestamp")
    LocalDateTime convertDrawingPostDTOtoEntity(DrawingPostDTO drawingPostDTO);

    @Mapping(source = "brushStrokes", target = "drawing")
    DrawingGetDTO convertEntityToDrawingGetDTO(Drawing drawing);
}
