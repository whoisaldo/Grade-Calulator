@echo off
REM 1) Create a “bin” folder if it doesn’t exist
if not exist bin (
  mkdir bin
)

REM 2) Compile all Java files under src/ into bin/
javac -d bin src\*.java
if ERRORLEVEL 1 (
  echo Compilation failed—see errors above.
  pause
  exit /b 1
)

REM 3) Run the program
echo Compilation succeeded. Launching...
java -cp bin StudentGradeCalculatorGUI
pause
