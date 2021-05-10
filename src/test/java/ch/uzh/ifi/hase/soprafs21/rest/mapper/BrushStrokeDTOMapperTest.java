package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.rest.dto.BrushStrokePutDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class BrushStrokeDTOMapperTest {
    @Test
    public void testBrushStroke__fromBrushStrokePutDTO_toBrushstroke_success() {
    //create BrushstrokePutDTO
        BrushStrokePutDTO brushStrokePutDTO = new BrushStrokePutDTO();
        brushStrokePutDTO.setColour("Black");
        brushStrokePutDTO.setSize(10);
        brushStrokePutDTO.setX(5);
        brushStrokePutDTO.setY(25);

        //MAP PutDTO --> Brushstrocke Entity
        BrushStroke brushStroke = BrushStrokeDTOMapper.INSTANCE.convertBrushStrokePutDTOtoEntity(brushStrokePutDTO);

        //check Entity
        assertEquals(brushStrokePutDTO.getSize(), brushStroke.getSize());
        assertEquals(brushStrokePutDTO.getX(),brushStroke.getX());
        assertEquals(brushStrokePutDTO.getY(), brushStroke.getY());
        assertEquals(brushStrokePutDTO.getColour(), brushStroke.getColour());

    }

}
