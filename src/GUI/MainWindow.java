package GUI;

import Handle.Handler;
import Rendering.AbstractRenderer;
import Rendering.Renderer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


/**
 * based on lwjgWindow of LWJGL-samples lvl 0
 */

public class MainWindow {
    public static int WIDTH = 600;
    public static int HEIGHT = 400;

    private AbstractRenderer renderer;
    // The window handle
    private long window;
    //handler to connect windows
    Handler handler;

    public MainWindow(AbstractRenderer renderer,Handler handler) {
        this(WIDTH, HEIGHT, renderer, handler);
    }

    public MainWindow(int width, int heigh,AbstractRenderer renderer, Handler handler) {
        this.WIDTH = width;
        this.HEIGHT = heigh;
        this.renderer = renderer;
        this.handler = handler;

        run();
    }

    public void run(){
        init();

        loop();

        //renderer.dispose(); // this is legacy, so far no idea what it does

        // Free the window callbacks and destroy the window

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    private void  init(){
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        /* ---------------------*/
        window = glfwCreateWindow(WIDTH,HEIGHT,"L system Bonsch",NULL,NULL);//NUll is 0L

        /* ---------------------*/
        if (window == NULL){
            throw new RuntimeException("Failed to create the GLFW window");
        }

        /* ---------------------*/
        //window icon
        /*
        GLFWImage.Buffer image;
        image = load_icon("icon.png");//icon needs to be 32bit,little endian,rgba

        glfwSetWindowIcon(window,image);

        */

        /* ----------------------*/

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, renderer.getGlfwKeyCallback());
        glfwSetWindowSizeCallback(window, renderer.getGlfwWindowSizeCallback());
        glfwSetMouseButtonCallback(window, renderer.getGlfwMouseButtonCallback());
        glfwSetCursorPosCallback(window, renderer.getGlfwCursorPosCallback());
        glfwSetScrollCallback(window, renderer.getGlfwScrollCallback());

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

    }

    private void loop(){
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        renderer.init();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {

            renderer.display();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        handler.options.closeOptions();

    }
}
