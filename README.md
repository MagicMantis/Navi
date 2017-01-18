# Project Navi
Project Navi is a Multiplayer online RTS/MOBA set in space. The reason I made the game was to improve my client-server network coding skills, as well as to experiment with neural networks and machine learning in game AI.

Players control space ships and attempt to destroy opponents base. Various structures on each team spawn units that will help in the attack. The player can manipulate the battle by attacking or distracting enemy units in such a way that give his units an advantage.

The primary features of this game are the model design and the network capabilities.

- Model design: project Navi was designed originally to be a framework for further Java games. The class heirarchy allows for new game objects to be added very easily. To explain in more detail: all game objects in a given level are subclasses of the class Entity. This class holds various information that is universal to all game objects, including x and y-coordinates, and abstract methods such as draw and update. Now take the Target class. The target class extends Entity, adding information such adds health functionality. Next look at the Ship class. This class extends Target, meaning it inherits universal Entity information, Target health and team information, and implements Ship functionality such as acceleration, rotation, and firing weapons. Lastly, we look at the Drone class. This class extends Ship, gaining all the functionality of a ship and simply adding artificial intelligence that uses the interfaces provided by its parents classes. A player controlled ship is easily created by subclassing the Ship class and allow user input to interface with the ships methods. The benefit of this system is that one can use previously implemented functionality to the degree required by a new object without having to reimplement it for each class.
- Network Capabilities: similarly, the network interface was designed with modularity in mind. All communication between the client and server applications follows a strict protocol outlined by the ServerController interface. This inteface outlines the methods than can be used to communicate with the server. The benefit of this is that not only is the code more organized, you can changed the server implementation simply by adding a new implementation of the ServerController class (instead of having to change the code everywhere that server communication is neccessary).

In the current stage of the game, the AI is very simple and simply moves to attack the closest target. The goal is to equip the NPCs with neural networks and train them over the course of many battles to see if any interesting strategies or behaviors arise.

#Contents
---------
* src
  - exceptions - contains relevant exceptions for server communication errors
  - model - primary game classes (entities, ship, player, etc.)
  - server - classes pertaining to the server application and managing network communications
  - services - controller and utility classes that are used throughout the project
  - view - interface and UI classes (screen, HUD, menu, etc.)
  - Space.java - primary runpoint for the application
* .gitignore - prevents library files from being tracked by git
* README.md - file outlining the application and 
* config.properties - settings for Project Navi (use this to set server ip)



Requirements
------------

Requires Java Runtime Environment and LWJGL 3+.


Controls
--------

Right and Left Arrow Keys ------------	Rotate ship left and right <br>
Up and Down Arrow Keys ---------------	Accelerate ship forwards and backwards <br>
Space Bar ----------------------------	Inertial Dampeners (decelerates ship)<br>
D key --------------------------------	Fire Weapon<br>
Mouse (click and hold minimap) -------	Unlock camera from player ship<br>


Source Code
-----------

All source code can be found at https://www.github.com/MagicMantis/Navi


License Info
------------

See licences for LWJGL, KyroNet, and Gson libraries.

Copyright C 2017 All Rights Reserved.

