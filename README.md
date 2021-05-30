# SoPra FS21 - Pictionary Client

## Introduction
This is the back-end of our UZH-pictionairy-game.
It is a multiplayer draw and guessing game for up to 10 Players per game.

As a painter you choose from a set of random words and then try your best to
draw it with a variety of painting tools. Your friends will see the drawing in
real time and have to guess the word. The faster, the more points you will get.

You can choose between a classic mode which will provide you with just any random
words, and a Pokemon mode in which you will have to draw different Pokemon.

## Technologies

The back-end uses the Java Spring Framework.
The Pokemon-mode uses an external REST API to get the name of a Pokemon. To
parse the JSON-response we use the external Java library org.json.JSONObject.

## High-level components (3 most important ones)

1.
2.
3.

## Launch & Deployment

**Requirements: Java 15**

Plattform-Prefix:

-   MAC OS X: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

#### Build

```bash
# Add --continuous if you want to automatically rebuild on changes
# Add -xtest to exclude the tests
./gradlew build
```

#### Run

```bash
./gradlew bootRun
```

#### Test

```bash
./gradlew test
```

## Roadmap

* Make application securer by adding token authentication with every API call
to make it impossible to send unauthorised API-calls with a tampered client
* Add more modes (Different themes in which you have to draw words about a
certain topic, like our Pokemon mode)

## Authors and acknowledgement

This project is based on https://github.com/HASEL-UZH/sopra-fs21-template-client

**Authors of this project**

* Christen, Kilian (Github: Kilian-Christen)
* Giesch, Simon (Github: Wahlbar)
* Harambasic, Josip (Github: JosipHarambasic)
* Schmatloch, Niklas Alexander (Github: niklassc7)
* Wernli, Anthony (Github: pbofataf7)

## License

[AGPLv3](LICENSE)
