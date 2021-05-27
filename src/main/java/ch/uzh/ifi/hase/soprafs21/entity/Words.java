package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.Random;

public class Words {

    private final String[] words = { "house", "dog", "fish", "spine",
            "murder", "study", "bean", "ice cream", "train", "calculator", "bench", "drill",
            "compass", "physics", "bar", "painting", "cinema",
            "parliament", "earthquake", "volcano", "curve", "relationship", "fraction",
            "jewel", "addiction", "strawberry", "orbit", "onion", "influencer", "twilight", "horse", "summer", "ghost",
            "switzerland", "ellipse", "rib", "scotland", "color-blind" };

    public String getRandomWord() {
        Random rand = new Random();
        int index = rand.nextInt(words.length);
        return words[index];
    }

}
