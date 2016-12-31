# Navi
Multiplayer online RTS/MOBA set in space. Primary object is to experiment with neural networks and machine learning in game AI.

Players control space ships and attempt to destroy opponents base. Various structures on each team spawn units that will help in the attack.

In the current stage of the game, the AI is very simple and simply moves to attack the closest target. The goal is to equip the NPCs with
neural networks and train them over the course of many battle to see if any interesting strategies or behaviors arise.

#File Structure
All source files are found in the net.magicmantis.src package, this contains the following folders:

Model - Primary game classes (entities, ship, player, etc.)
View - Interface and UI classes (screen, HUD, menu, etc.)
Services - Controllers and Utility classes that are used throughout the project
Server - Classes pertaining to the server application and managing network communications
Exceptions - Contains relevant exceptions for errors

It also contains Space.java, the primary runpoint for the application. This game requires the LWJGL to run.