package ch.uzh.ifi.hase.soprafs21.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.io.*;
import java.net.*;

public class PokémonController {

        public static String getHTML(String urlToRead) throws Exception {
                StringBuilder result = new StringBuilder();
                URL url = new URL(urlToRead);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                try (var reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                        for (String line; (line = reader.readLine()) != null; ) {
                            result.append(line);
                        }
                    }
                return result.toString();
        }
}

