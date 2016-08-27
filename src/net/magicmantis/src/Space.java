package net.magicmantis.src;


import net.magicmantis.src.view.Screen;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Space class: This class is the entry point for launching the lwjgl application.
 */
public class Space {
	
	public static int WIDTH = 1600;
	public static int HEIGHT = 900;

    public static void main(String[] args) {
        //ensure path variables support lwjgl
        if (System.getProperty("org.lwjgl.librarypath") == null) {
            Path path = Paths.get("libs/");
            String librarypath = path.toAbsolutePath().toString();
            System.setProperty("org.lwjgl.librarypath", librarypath);
        }

        //create a new OpenGL window
        new Screen().run(WIDTH, HEIGHT);
    }
}
