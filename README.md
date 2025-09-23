# Turn-Based Card Game

A Java-based turn-based card battler with animated combat, recursive game loops, and dynamic entity interactions. Built with Swing for rendering and a custom animation engine.  

## Gameplay Overview
- Player and enemy take turns playing cards.  
- Cards trigger attacks, buffs, and effects.  
- Each attack launches an animation (e.g. fireball, sword slash).  
- Entities (Player/Enemy) update states like health, movement, and death dynamically.  

## Features
- Recursive game loop (~30 FPS) controlling logic and rendering.  
- Custom animation framework — entities trigger and queue animations.  
- GUI layering:  
  - Display GUI for board state.  
  - Glass pane for interactable elements.  
- Entity system: Player + multiple Enemy types (Goblin, etc.).  
- Attack plane: Queues and renders attack animations in real-time.  

## Tech Stack
- **Language**: Java  
- **GUI**: Swing  
- **Rendering**: `paintComponent(Graphics2D)` with transformations  
- **Animation**: Frame-based sprite updates (`startAnimation`, `updateAni`)  
- **Testing**: JUnit (white-box, black-box, unit testing)  

## Future Work
### After Migration to LibGDX
- Refined deck-building mechanics.  
- Expanded enemy roster and AI logic.  
- Sound effects and music integration.  
- Save/load system.  

## Learning Highlights
This project demonstrates:  
- Game loop design in Java.  
- Custom animation system within Swing.  
- Object-oriented architecture for games.  
- Testing strategies for complex, recursive systems.  
