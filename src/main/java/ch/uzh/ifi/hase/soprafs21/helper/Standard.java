package ch.uzh.ifi.hase.soprafs21.helper;

import java.time.format.DateTimeFormatter;

/**
 * A helper class to standardize all the different types and formats throughout the project
 */
public class Standard {

    public int numberOfChoices = 3; // how many options the drawer can choose from

    public int minNumOfPlayers = 3; // the minimum of users that need to be present for a game

    public DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"); // how time is saved and reasoned about

    public DateTimeFormatter getDateTimeFormatter() { return this.dateTimeFormatter; }

    public int getNumberOfChoices() { return this.numberOfChoices; }

    public int getMinNumOfPlayers() { return this.minNumOfPlayers; }

}
