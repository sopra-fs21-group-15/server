package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.entity.Drawing;
import ch.uzh.ifi.hase.soprafs21.entity.Round;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.repository.BrushStrokeRepository;
import ch.uzh.ifi.hase.soprafs21.repository.DrawingRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RoundRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

@Service
@Transactional
public class DrawingService {

    private final RoundRepository roundRepository;

    private final DrawingRepository drawingRepository;

    private final BrushStrokeRepository brushStrokeRepository;

    @Autowired
    public DrawingService(@Qualifier("drawingRepository")DrawingRepository drawingRepository, RoundRepository roundRepository, BrushStrokeRepository brushStrokeRepository) {
        this.drawingRepository = drawingRepository;
        this.roundRepository = roundRepository;
        this.brushStrokeRepository = brushStrokeRepository;
    }

    // get all the drawings
    public List<Drawing> getDrawings() {
        return this.drawingRepository.findAll();
    }

    // get a specific drawing
    public Drawing getDrawing(Long drawingId) {
        List<Drawing> drawings = getDrawings();

        Drawing drawingFound = null;
        for (Drawing i : drawings) {
            if (drawingId.equals(i.getId())) {
                drawingFound = i;
            }
        }

        // if we do not find the drawing
        String nonExistingDrawing = "This drawing does not exist. Please search for an existing drawing.";
        if (drawingFound == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nonExistingDrawing);
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

    /** Get the latest brushstrokes of a drawing past a certain time
     * The method is used to update the picture users see in the front end. They send us the time at which they last
     * updated and we send them all the information they have missed so far.
     *
     * @param drawing = the drawing they would like to see
     * @param timeStamp = the time from which onward they need the information
     * @return a sorted list of all the need brush strokes starting from latest to newest
     */
    public List<BrushStroke> getDrawing(Drawing drawing, LocalDateTime timeStamp) {
        int maxIndex = drawing.getBrushStrokes().size(); // get the size of the drawing
        List<BrushStroke> result = new ArrayList<>(); // get the basic return value

        if(!drawing.getBrushStrokes().isEmpty()) { // if the drawing is not empty we can look for the right part
            int index = 0; // initiate the index

            // setting everything up to iterate over the indexes to find the point after which the needed brush strokes are listed
            DateTimeFormatter formatter = new Standard().getDateTimeFormatter();
            BrushStroke brushStroke = drawing.getBrushStrokes().get(index);
            LocalDateTime value = LocalDateTime.parse(brushStroke.getTimeStamp(), formatter);

            // iterate over the saved brush strokes in drawing
            while (index < maxIndex && (value.isBefore(timeStamp) || value.isEqual(timeStamp))) {
                index++;
                if (index < maxIndex) {
                    brushStroke = drawing.getBrushStrokes().get(index);
                    value = LocalDateTime.parse(brushStroke.getTimeStamp(), formatter);
                }
            }

            // get a sublist past the critical point
            result = drawing.getBrushStrokes().subList(index, maxIndex);

            // sort the list and then return it
            try {
                Collections.sort(result);
            } catch (ClassCastException e) {
                System.out.println("You tried to sort a list with objects that can not be compared with one another.");
            }
        }

        return result;
    }

    /** Add a new brush stroke to an existing drawing while at the same time saving it in the repository
     * and sorting the list within the drawing just to make sure it did not mix up the order of the brushstrokes.
     *
     * @param drawing = the drawing we would like to add brush stroke to
     * @param brushStroke = the brush stroke we are supposed to add
     */
    public void addStroke(Drawing drawing, BrushStroke brushStroke) {
        brushStroke = brushStrokeRepository.saveAndFlush(brushStroke); // save in repository

        drawing.getBrushStrokes().add(brushStroke); // add to drawing
        // for safety we check if it works
        try {
            Collections.sort(drawing.getBrushStrokes()); // sort list within drawing
        } catch (ClassCastException e) {
            System.out.println("You tried to sort a list with objects that can not be compared with one another.");
        }

        drawingRepository.saveAndFlush(drawing); // update the drawing in the repository
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
