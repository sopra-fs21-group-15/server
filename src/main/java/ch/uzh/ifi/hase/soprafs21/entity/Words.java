package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.Random;

public class Words {

    private final String[] words = { "house", "dog", "fish", "spine", "dream",
            "murder", "study", "bean", "ice cream", "train", "calculator",
            "bench", "drill", "laptop", "apple", "lake", "ocean", "ship",
            "compass", "physics", "bar", "painting", "cinema", "lighthouse",
            "parliament", "earthquake", "volcano", "curve", "relationship",
            "fraction", "maths", "piano", "guitar", "harmonica", "letter",
            "jewel", "addiction", "strawberry", "orbit", "onion",
            "influencer", "twilight", "horse", "summer", "ghost", "camera",
            "bottle", "banana", "lightbulb", "circus", "zoo", "table", "arm",
            "switzerland", "ellipse", "rib", "scotland", "color-blind" };

    public String getRandomWord() {
        Random rand = new Random();
        int index = rand.nextInt(words.length);
        return words[index];
    }

}
