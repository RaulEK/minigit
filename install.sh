#!/usr/bin/env bash
./gradlew build
sudo cp client/build/libs/client.jar /usr/local/lib/minigit.jar
echo 'java -cp /usr/local/lib/minigit.jar client.Main "$@"' | sudo tee /usr/local/bin/minigit
sudo chmod +x /usr/local/bin/minigit
