#!/bin/bash
# Helper: lookup APIs in the NeoForge 26.1.2 artifacts.
#   ./api-grep.sh <pattern>     -> list matching entries (in minecraft sources, or -n for neoforge sources)
#   ./api-grep.sh -c <path>     -> cat a class source from minecraft sources jar
#   ./api-grep.sh -n <pattern>  -> list matching entries in neoforge sources jar
#   ./api-grep.sh -nc <path>    -> cat a class source from neoforge sources jar
JAR=/home/goose/.gradle/caches/neoformruntime/intermediate_results/mergeWithSources_c5b71c646393237837615ce1a490bf4854e45a82_output.jar
NEO=/home/goose/.gradle/caches/modules-2/files-2.1/net.neoforged/neoforge/26.1.2.114/85d479e73d84cbdc0acbca3b9a12e81d9cb226b3/neoforge-26.1.2.114-sources.jar
case "$1" in
  -c)  unzip -p "$JAR" "$2";;
  -nc) unzip -p "$NEO" "$2";;
  -n)  unzip -l "$NEO" | grep -i "$2";;
  *)   unzip -l "$JAR" | grep -i "$1";;
esac
