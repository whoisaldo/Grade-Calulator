#!/usr/bin/env bash
# 1) Create a “bin” folder if it doesn’t exist
mkdir -p bin

# 2) Compile all Java files under src/ into bin/
javac -d bin src/*.java

# 3) If compilation succeeded, run the program:
if [ $? -eq 0 ]; then
  echo "Compilation succeeded. Launching..."
  java -cp bin StudentGradeCalculatorGUI
else
  echo "Compilation failed—see errors above."
fi
