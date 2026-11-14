#!/bin/bash
# Compila o jar com o Java 21 e as bibliotecas do proprio Prism (sem Gradle).
set -euo pipefail
cd "$(dirname "$0")"
L=~/EtherLake/serviços/moonshine/minecraft-prism/libraries
MODS=~/EtherLake/serviços/moonshine/minecraft-prism/instances/1.21.1/minecraft/mods
J=$(ls -d /usr/lib/jvm/temurin-21*/bin | head -1)
CP="$L/net/minecraft/client/1.21.1-20240808.144430/client-1.21.1-20240808.144430-srg.jar:$L/net/fabricmc/sponge-mixin/0.16.4+mixin.0.8.7/sponge-mixin-0.16.4+mixin.0.8.7.jar:$L/net/neoforged/fancymodloader/loader/4.0.45/loader-4.0.45.jar:$(ls $MODS/shine-*.jar | head -1):$(ls $MODS/WorldEditCUI-*.jar | head -1)"
rm -rf build && mkdir -p build/classes
"$J/javac" --release 21 -proc:none -nowarn -cp "$CP" -d build/classes $(find src/main/java -name "*.java")
cp -r src/main/resources/. build/classes/
"$J/jar" --create --file build/shine-portals-compat-1.1.0.jar -C build/classes .
echo "OK: build/shine-portals-compat-1.1.0.jar"
