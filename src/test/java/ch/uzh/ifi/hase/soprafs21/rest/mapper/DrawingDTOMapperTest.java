package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawingGetDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DrawingDTOMapperTest {


    @Test
    public void testGetDrawing_fromBrushstroke_toDrawingGetDTO_success() {
        // create brushstroke
        BrushStroke brushStroke = new BrushStroke();
        brushStroke.setX(5);
        brushStroke.setY(10);
        brushStroke.setSize(15);
        brushStroke.setTimeStamp("a");
        brushStroke.setColour("BLACK");

        // MAP -> Create DrawingGetDTO
        DrawingGetDTO drawingGetDTO = DrawingDTOMapper.INSTANCE.convertEntityToDrawingGetDTO(brushStroke);

        // check content
        assertEquals(brushStroke.getSize(), drawingGetDTO.getSize());
        assertEquals(brushStroke.getX(), drawingGetDTO.getX());
        assertEquals(brushStroke.getY(), drawingGetDTO.getY());
        assertEquals(brushStroke.getColour(), drawingGetDTO.getColour().toString());
        assertEquals(brushStroke.getTimeStamp(), drawingGetDTO.getTimeStamp());

    }
}
