package net.magicmantis.src.services;

import net.magicmantis.src.view.Game;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * InputHandler: provides functions for providing callback functions to the window.
 */

public class InputHandler {

    public static Game game;

    public static void setGame(Game setGame) {
        game = setGame;
    }

    public static GLFWKeyCallback getKeyCallback() {
        GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS)
                    game.keyPressed(key);
                else if (action == GLFW_RELEASE)
                    game.keyReleased(key);
            }
        };
        return keyCallback;
    }

    public static GLFWMouseButtonCallback getMouseButtonCallback() {
        GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                game.mouseEvent(button, action);
            }
        };
        return mouseButtonCallback;
    }

}
