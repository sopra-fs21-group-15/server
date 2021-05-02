package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.entity.Drawing;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawingGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawingPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.NestedString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface DrawingDTOMapper {

    DrawingDTOMapper INSTANCE = Mappers.getMapper(DrawingDTOMapper.class);

    @Mapping(source = "timestamp", target = "timestamp")
    NestedString convertDrawingPostDTOtoEntity(DrawingPostDTO drawingPostDTO);

    @Mapping(source = "x", target = "x")
    @Mapping(source = "y", target = "y")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "colour", target = "colour")
    DrawingGetDTO convertEntityToDrawingGetDTO(BrushStroke brushStroke);
}
