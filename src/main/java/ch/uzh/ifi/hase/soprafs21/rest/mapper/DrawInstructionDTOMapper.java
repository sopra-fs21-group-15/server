package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.DrawInstruction;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawInstructionPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawInstructionGetDTO;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DrawInstructionDTOMapper {

    DrawInstructionDTOMapper INSTANCE = Mappers.getMapper(DrawInstructionDTOMapper.class);

    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "x", target = "x")
    @Mapping(source = "y", target = "y")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "colour", target = "colour")
    DrawInstruction convertDrawInstructionPutDTOtoEntity(DrawInstructionPutDTO drawInstructionPutDTO);

    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "x", target = "x")
    @Mapping(source = "y", target = "y")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "colour", target = "colour")
    DrawInstructionGetDTO convertEntityToDrawInstructionGetDTO(DrawInstruction drawInstruction);
}
