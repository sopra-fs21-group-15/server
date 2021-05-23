package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.rest.dto.BrushStrokePutDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrushStrokeDTOMapper {

    BrushStrokeDTOMapper INSTANCE = Mappers.getMapper(BrushStrokeDTOMapper.class);

    @Mapping(source = "x", target = "x")
    @Mapping(source = "y", target = "y")
    @Mapping(source = "timeStamp", target = "timeStamp")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "colour", target = "colour")
    BrushStroke convertBrushStrokePutDTOtoEntity(BrushStrokePutDTO brushStrokePutDTO);
}
