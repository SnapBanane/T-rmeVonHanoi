@echo off
echo Compiling Towers of Hanoi...
javac -cp "lib\basis.jar" -d out src\main\java\*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running Towers of Hanoi...
java -cp "out;lib\basis.jar" Main
pause

