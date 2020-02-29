# Styx Reactor

Welcome to **Styx Reactor** - a round-based multiplayer strategy game!
This is a clone of the discontinued game _Atlas Reactor_, because we still want to play it.

## running

    mvn clean jooby:run

## building

    mvn clean package

## docker

     docker build . -t totally-not-atlasreactor-server
     docker run -p 8080:8080 -it totally-not-atlasreactor-server
