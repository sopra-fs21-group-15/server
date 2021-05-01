package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.Random;

public class Words {

    private final String[] words = { "house", "dog", "end", "salmon","virtue", "spine", "represent", "situation",
            "murder", "study", "bean", "cream", "train", "calculation", "stereotype", "bench", "drill", "solution",
            "orientation", "polish", "tribute", "physical", "club", "frame", "provoke", "equip", "lounge", "cinema",
            "ministry", "earthquake", "revive", "oh", "concession", "curve", "freighter", "relationship", "fraction",
            "jewel", "addicted", "dose", "berry", "orbit", "influence", "twilight", "eaux", "summer", "ghostwriter",
            "rough", "elapse", "rib", "scatter", "color-blind" };

    public String getRandomWord() {
        Random rand = new Random();
        int index = rand.nextInt(words.length);
        return words[index];
    }

}
