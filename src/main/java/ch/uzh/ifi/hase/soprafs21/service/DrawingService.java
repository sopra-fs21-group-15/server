package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.entity.Drawing;
import ch.uzh.ifi.hase.soprafs21.entity.Round;
import ch.uzh.ifi.hase.soprafs21.repository.BrushStrokeRepository;
import ch.uzh.ifi.hase.soprafs21.repository.DrawingRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DrawingService {

    private final RoundRepository roundRepository;

    private final DrawingRepository drawingRepository;

    private final BrushStrokeRepository brushStrokeRepository;

    @Autowired
    public DrawingService(@Qualifier("drawingRepository")DrawingRepository drawingRepository, RoundRepository roundRepository, BrushStrokeRepository brushStrokeRepositroy) {
        this.drawingRepository = drawingRepository;
        this.roundRepository = roundRepository;
        this.brushStrokeRepository = brushStrokeRepositroy;
    }

    // get all the drawings
    public List<Drawing> getDrawing() {
        return this.drawingRepository.findAll();
    }

    // get a specific drawing
    public Drawing getDrawing(Long drawingId) {
        List<Drawing> drawings = getDrawing();

        Drawing drawingFound = null;
        for (Drawing i : drawings) {
            if (drawingId.equals(i.getId())) {
                drawingFound = i;
            }
        }

        return drawingFound;
    }

    // get all brush strokes
    public List<BrushStroke> getBrushStroke() { return this.brushStrokeRepository.findAll(); }

    // get a specific brush stroke
    public BrushStroke getBrushStroke(Long brushStrokeId) {
        List<BrushStroke> brushStrokes = getBrushStroke();

        BrushStroke brushStrokeFound = null;
        for (BrushStroke i : brushStrokes) {
            if (brushStrokeId.equals(i.getId())) {
                brushStrokeFound = i;
            }
        }

        return brushStrokeFound;
    }

    // get the latest brushStrokes past a certain time
    public ArrayList<BrushStroke> getDrawing(Long drawingId, LocalDateTime timeStemp) {
        Drawing drawing = getDrawing(drawingId);
        int index = 0;
        //drawing.get(index).getTimeStamp().isBefore(timeStamp)
        Long brushStrokeId = drawing.getBrushStrokeIds().get(index);
        BrushStroke brushStroke = getBrushStroke(brushStrokeId);
        LocalDateTime value = brushStroke.getTimeStamp();
        while(value.isBefore(timeStemp)) {
            index++;
        }

        ArrayList<Long> brushStrokeIds = new ArrayList<Long>(drawing.getBrushStrokeIds().subList( index,drawing.getBrushStrokeIds().size()-1) );
        ArrayList<BrushStroke> temp = new ArrayList<BrushStroke>();
        for (Long i : brushStrokeIds) {
            temp.add(getBrushStroke(i));
        }
        return temp;

    }

    // get all rounds
    public List<Round> getRounds() {
        return this.roundRepository.findAll();
    }

    // get a specific round
    public Round getRound(Long roundId) {
        List<Round> rounds = getRounds();

        Round roundFound = null;
        for (Round i : rounds) {
            if (roundId.equals(i.getId())) {
                roundFound = i;
            }
        }

        return roundFound;
    }

    // add a brushstroke to a picture
    public void addStroke(Long drawingId, BrushStroke brushStroke) {
        Drawing drawing = getDrawing(drawingId);

        brushStroke = brushStrokeRepository.save(brushStroke);
        brushStrokeRepository.flush();

        drawing.add(brushStroke.getId());

        drawing = drawingRepository.save(drawing);
        drawingRepository.flush();
    }


    /*
    public Drawing getDrawing(LocalDateTime timeStamp) {
        int index = 0;
        while(brushStrokes.get(index).getTimeStamp().isBefore(timeStamp)) {
            index++;
        }
        ArrayList<BrushStroke> temp = new ArrayList<BrushStroke>(brushStrokes.subList(index,brushStrokes.size()-1));
        Drawing value = new Drawing();
        value.setBrushStrokes(temp);
        value.setDrawerId(0);
        return value;
    }*/
}
