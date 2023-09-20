#!/bin/bash
FILES=$PWD/inputs/*

for f in $FILES
do
java PuzzleChecker $f
done
