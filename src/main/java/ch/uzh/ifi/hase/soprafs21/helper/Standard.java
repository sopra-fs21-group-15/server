package ch.uzh.ifi.hase.soprafs21.helper;

import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

/**
 * A helper class to standardize all the different types and formats throughout the project
 */
public class Standard {

    public int numberOfChoices = 3; // how many options the drawer can choose from
    public int minNumOfPlayers = 2; // the minimum of users that need to be present for a game
    public int timeToSelect = 20; // [s] time to select the words before a random word is chosen for you
    public int convertToLocalTimeOffSet = 11; // the off set we have if we want to transform the format into localtime

    public DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"); // how time is saved and reasoned about

    // access number of choices
    public int getNumberOfChoices() { return this.numberOfChoices; }
    public void setNumberOfChoices(int numberOfChoices) { this.numberOfChoices = numberOfChoices; }

    // access minimum number of players for a game
    public int getMinNumOfPlayers() { return this.minNumOfPlayers; }
    public void setMinNumOfPlayers(int minNumOfPlayers) { this.minNumOfPlayers = minNumOfPlayers; }

    // access time to select an option
    public int getTimeToSelect() { return this.timeToSelect; }
    public void setTimeToSelect(int timeToSelect) { this.timeToSelect = timeToSelect; }

    // access to the convert to localtime off set
    public int getConvertToLocalTimeOffSet() { return this.convertToLocalTimeOffSet; }
    public void setConvertToLocalTimeOffSet(int convertToLocalTimeOffSet) { this.convertToLocalTimeOffSet = convertToLocalTimeOffSet; }

    // access the general formatting of time
    public DateTimeFormatter getDateTimeFormatter() { return this.dateTimeFormatter; }
    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) { this.dateTimeFormatter = dateTimeFormatter; }

}
