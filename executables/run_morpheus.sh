#!/bin/bash

echo "Welcome to MorpheuS music generation."
echo
echo "To run this, you need a java runtime environment installed. You also need the two included jar files in this folder."
echo
echo "Note that you can run the instructions in this file manually and control the generation more. You can see options by running 'java -jar omnisia.jar -h' and 'java -jar PMusicOR.jar -h'."
echo
echo "You also need a musicxml file (non-compressed), with .xml extension. Secondly, you also need a .mid file. Both of these files should have the same name pre-extension and should be placed in the active path."
echo

read -p "Please enter the name (without extension) here: " filename
read -p "Please enter the number of iterations: " iters
read -p "Please enter the key base pitch (major), e.g. C: " key

# Execute omnisia.jar
java -jar omnisia.jar -i "${filename}.mid" -draw

# Get the name of the last created folder
last_folder=$(ls -td */ | head -n 1)

# Move the file to the current folder
mv "${last_folder}${filename}-chrom.cos" .

# Run PMusicOR.jar
java -jar PMusicOR.jar -inputfile "./$filename" -iters "$iters" -windowLength 8 -key "$key"

echo "Music generation completed."
