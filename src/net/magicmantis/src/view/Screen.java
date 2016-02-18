package net.magicmantis.src.view;

import net.magicmantis.src.services.InputHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Screen {

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseCallback;

    private static int fps;
    private int framesThisSecond;
    private long secondStartTime;

    // The window handle
    private long window;
    private Game game;

    private int WIDTH, HEIGHT;

    public void run(int w, int h) {
        WIDTH = w;
        HEIGHT = h;

        try {
            init();
            loop();

            // Release window and window callbacks
            glfwDestroyWindow(window);
            game.stop();
            keyCallback.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Navi", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        //init fps for fps counter
        fps = 0;

        //create the game
        game = new Game(this, WIDTH, HEIGHT);
        Thread thread = new Thread(game);
        thread.start();

        // Setup callbacks
        InputHandler.setGame(game);
        glfwSetKeyCallback(window, keyCallback = InputHandler.getKeyCallback());
        glfwSetMouseButtonCallback(window, mouseCallback = InputHandler.getMouseButtonCallback());

        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

    }

    private void loop() throws InterruptedException {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        //set up drawing
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        secondStartTime = System.nanoTime();
        framesThisSecond = 0;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // draw quad
            glColor3f(0f,0f,0f);
            glBegin(GL_QUADS);
            glVertex2d(0, 0);
            glVertex2d(0, HEIGHT);
            glVertex2d(WIDTH, HEIGHT);
            glVertex2d(WIDTH, 0);
            glEnd();

            game.draw();
            framesThisSecond++;
            if (System.nanoTime()-secondStartTime > 1000000000) {
                secondStartTime = System.nanoTime();
                fps = framesThisSecond;
                framesThisSecond = 0;
            }

            glfwSwapBuffers(window); // swap the color buffers

            //get the mouse location
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

            glfwGetCursorPos(window, x, y);
            Game.mouseX = (int) x.get();
            Game.mouseY = HEIGHT - (int) y.get();

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static int getFPS() {
        return fps;
    }

}