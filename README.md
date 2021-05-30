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

1. [GameController](src/main/java/ch/uzh/ifi/hase/soprafs21/controller/GameController.java) The game controller handles almost
   all the API calls that are made during the game and is vital to the app and its functionality. Some of the most used
   calls get evaluated and answered in this class. One of which is called *update* and it returns all the information
   about the game someone could ask for. Like which of the player is leading, what is the current word everybody is
   trying to guess and who is drawing right now. The most used API call is being handled here as well. It is the call
   requesting the drawing itself.
2. [GameService](src/main/java/ch/uzh/ifi/hase/soprafs21/service/GameService.java)
   While the controller handles all the requests and calls the real work gets done by the services. One of the most
   interconnected services is the GameService. If it is not doing it itself it ordered another service to do it. Among
   executing all the task that the controller throws at it, the service also runs the game in the background on its own.
   Making sure the rounds end on time and even the drawer gets his/her fair share of points.
3. [LobbyService](src/main/java/ch/uzh/ifi/hase/soprafs21/service/LobbyService.java) While the GameService is needed once the game
   starts, the LobbyService does all the work that gets us there. Among handling and executing the request send by its
   own controller it makes sure everything stays in order. People get the lobby they requested as long as all
   restrictions for privacy are met.

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
