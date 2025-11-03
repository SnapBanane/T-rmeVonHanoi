# Türme von Hanoi (Towers of Hanoi)

A Java implementation of the Towers of Hanoi puzzle using the basis library.

## Authors
- Collien
- Celine
- Timo
- Julius

## Project Structure

```
T-rmeVonHanoi/
├── src/main/java/          # Source files
│   ├── Main.java           # Entry point
│   ├── index.java          # Main game logic
│   └── MeinKnopf.java      # Custom button class
├── lib/                    # Libraries
│   └── basis.jar          # Basis GUI library
├── Türme_von_Hanoi/        # Original BlueJ files (kept for reference)
└── build.gradle            # Build configuration
```

## How to Run in IntelliJ IDEA

### Option 1: Using IntelliJ's Build System
1. Open the project in IntelliJ IDEA (File > Open > select this folder)
2. Wait for IntelliJ to index the project
3. Right-click on `src/main/java/Main.java`
4. Select "Run 'Main.main()'"

### Option 2: Using Gradle (if you have it installed)
1. Open Terminal in IntelliJ (Alt+F12)
2. Run: `./gradlew run` (or `gradlew.bat run` on Windows)

### Option 3: Create Run Configuration
1. Go to Run > Edit Configurations...
2. Click + > Application
3. Name: "Towers of Hanoi"
4. Main class: `Main`
5. Module: `T-rmeVonHanoi`
6. Click OK
7. Click the Run button (green triangle)

## Development

The project uses:
- Java 17 or higher
- basis.jar library for GUI components
- Standard Java Swing for graphics

## Game Instructions

1. Click on a disc (1-4) to select it
2. Click on a tower button to move the selected disc to that tower
3. You can only place smaller discs on top of larger discs
4. Click "Ende" to exit the game

## Version
0.0.1

