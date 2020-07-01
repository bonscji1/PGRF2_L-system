package Rendering;


import Handle.Handler;
import Lsystem.AlphabetForDraw;
import Lsystem.LSystemMain;
import transforms.Mat4RotXYZ;
import transforms.Point3D;
import utils.GLCamera;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import transforms.Vec3D;

import java.nio.DoubleBuffer;
import java.util.HashMap;

import static utils.GluUtils.gluPerspective;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer extends AbstractRenderer {


    private float dx, dy, ox, oy;
    private float zenit, azimut;

    private float trans, deltaTrans = 0;
    private float initialSpeed = 0.005f;
    private float speedUp = 1.02f;

    private float uhel = 0;

    private boolean mouseButton1 = false;
    private boolean per = true, move = false;

    private Point3D innerDrawPoint, resetPoint;//draw to this point from 0,0,0


    private HashMap<Character, AlphabetForDraw> drawInstructions;
    private StringBuilder pattern;

    private GLCamera camera;

    private Handler handler;


    public Renderer(Handler handler) {
        super();
        this.handler = handler;

        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    // We will detect this in our rendering loop
                    glfwSetWindowShouldClose(window, true);
                //-------------------------------------------------------------------------------
                if (action == GLFW_RELEASE) {//do something on release
                    trans = 0;
                    deltaTrans = 0;

                }

                if (action == GLFW_PRESS) {//do something on press
                    switch (key) {
                        case GLFW_KEY_P:
                            per = !per;
                            break;
                        case GLFW_KEY_M:
                            move = !move;
                            break;
                        case GLFW_KEY_O:
                            handler.options.setVisible(true);
                            break;
                        case GLFW_KEY_W:
                        case GLFW_KEY_S:
                        case GLFW_KEY_A:
                        case GLFW_KEY_D:
                            deltaTrans = initialSpeed;
                            break;
                        case GLFW_KEY_KP_SUBTRACT:
                            handler.changed = true;
                            handler.iteratorN -= 1;
                            break;
                        case GLFW_KEY_KP_ADD:
                            handler.changed = true;
                            handler.iteratorN += 1;
                            break;

                    }
                }

                switch (key) {//do something as long as key is pressed
                    case GLFW_KEY_W:
                        camera.up(trans);
                        if (deltaTrans < initialSpeed)
                            deltaTrans = initialSpeed;
                        else
                            deltaTrans *= speedUp;
                        break;

                    case GLFW_KEY_S:
                        camera.down(trans);
                        if (deltaTrans < initialSpeed)
                            deltaTrans = initialSpeed;
                        else
                            deltaTrans *= speedUp;
                        break;

                    case GLFW_KEY_A:
                        camera.left(trans);
                        if (deltaTrans < initialSpeed)
                            deltaTrans = initialSpeed;
                        else
                            deltaTrans *= speedUp;
                        break;

                    case GLFW_KEY_D:
                        camera.right(trans);
                        if (deltaTrans < initialSpeed)
                            deltaTrans = initialSpeed;
                        else
                            deltaTrans *= speedUp;
                        break;
                }
            }
        };

        glfwMouseButtonCallback = new GLFWMouseButtonCallback() {

            @Override
            public void invoke(long window, int button, int action, int mods) {
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                double x = xBuffer.get(0);
                double y = yBuffer.get(0);

                mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    ox = (float) x;
                    oy = (float) y;
                }
            }

        };

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                if (mouseButton1) {
                    dx = (float) x - ox;
                    dy = (float) y - oy;
                    ox = (float) x;
                    oy = (float) y;
                    zenit -= dy / width * 180;
                    if (zenit > 90)
                        zenit = 90;
                    if (zenit <= -90)
                        zenit = -90;
                    azimut += dx / height * 180;
                    azimut = azimut % 360;
                    camera.setAzimuth(Math.toRadians(azimut));
                    camera.setZenith(Math.toRadians(zenit));
                    dx = 0;
                    dy = 0;
                }
            }
        };

        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                camera.forward(0.25f * dy);

            }
        };
    }

    @Override
    public void init() {
        super.init();

        //set background color
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glFrontFace(GL_CW);
        glPolygonMode(GL_FRONT, GL_FILL);
        glPolygonMode(GL_BACK, GL_FILL);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera = new GLCamera();
        camera.setPosition(new Vec3D(0));
        camera.setFirstPerson(false);
        camera.setRadius(5);

        handler.changed = true;
        handler.iteratorN = 0;
        handler.start = new StringBuilder("");
        resetPoint = new Point3D(0, 1, 0);
        innerDrawPoint = resetPoint;

    }


    @Override
    public void display() {
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);


        //create and draw text
        String text = "[O] open options, current N is " + handler.iteratorN;
        textRenderer.clear();
        textRenderer.addStr2D(3, 20, text);
        textRenderer.draw();

        //this is for moving camera
        trans += deltaTrans;

        //fancy fancy not important
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        if (per)
            gluPerspective(45, width / (float) height, 0.1f, 500.0f);
        else
            glOrtho(-20 * width / (float) height,
                    20 * width / (float) height,
                    -20, 20, 0.1f, 500.0f);

        if (move) {
            uhel++;
        }

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        camera.setMatrix();
        glRotatef(uhel, 0, 1, 0);

        //actual drawing ----------------------------------------------------------------------------------------------------

        if (handler.changed) {//this will deal with L system, dont want to do it every round, only if i changed something

            //SETUP

            HashMap<Character, String> rules = handler.getRules();
            drawInstructions = handler.getDrawRules();

            StringBuilder start = handler.start;

            LSystemMain ls = new LSystemMain(start, rules, handler.iteratorN);
            pattern = ls.getResult();

            handler.changed = false;
            if (handler.iteratorN < 0) {
                handler.iteratorN = 0;
            }
        }


        //i also have Point where i start to draw
        Point3D currentLocation = new Point3D(0, 0, 0);
        Point3D newLocation; //location i want to draw line to and then switch to current

        //helpful things i can play with
        Character patternLetter;
        AlphabetForDraw instruction;
        int drawLineWidth = 1;
        Point3D drawLenghtModifier = new Point3D(1, 1, 1);


        for (int i = 0; i < pattern.length(); i++) {
            patternLetter = pattern.charAt(i);
            instruction = drawInstructions.get(patternLetter);


            if (instruction == null) {
                System.err.println("No instruction for " + patternLetter);
            } else {
                switch (instruction.getType()) {
                    case DRAW:
                        newLocation = new Point3D(
                                instruction.getIntX() * drawLenghtModifier.getX() + currentLocation.getX(),
                                instruction.getIntY() * drawLenghtModifier.getY() + currentLocation.getY(),
                                instruction.getIntZ() * drawLenghtModifier.getZ() + currentLocation.getZ());
                        // setting line width
                        glLineWidth(drawLineWidth);
                        // setting line type
                        // glEnable(GL_LINE_STIPPLE);
                        // setting line look
                        //glLineStipple(1, (short) (drawLineMode ^ 2));

                        // setting drawing type

                        glBegin(GL_LINES); // lines, set bz 2 points
                        // geometry and color
                        glColor3f(0.0f, 0.5f, 1.0f);
                        glVertex3f((float) currentLocation.getX(), (float) currentLocation.getY(), (float) currentLocation.getZ());
                        glColor3f(0.0f, 0.5f, 1.0f);
                        glVertex3f((float) newLocation.getX(), (float) newLocation.getY(), (float) newLocation.getZ());
                        glEnd();

                        //glDisable(GL_LINE_STIPPLE);


                        currentLocation = newLocation;
                        break;
                    case MOVE:
                        newLocation = new Point3D(
                                instruction.getIntX() * drawLenghtModifier.getX() + currentLocation.getX(),
                                instruction.getIntY() * drawLenghtModifier.getY() + currentLocation.getY(),
                                instruction.getIntZ() * drawLenghtModifier.getZ() + currentLocation.getZ());
                        currentLocation = newLocation;
                        break;
                    case CHANGE_DRAW_LENGTH:
                        drawLenghtModifier = new Point3D(
                                drawLenghtModifier.getX() + instruction.getIntX(),
                                drawLenghtModifier.getY() + instruction.getIntY(),
                                drawLenghtModifier.getZ() + instruction.getIntZ()
                        );
                        break;
                    case DRAW_INNER:
                        newLocation = new Point3D(
                                innerDrawPoint.getX() + currentLocation.getX(),
                                innerDrawPoint.getY() + currentLocation.getY(),
                                innerDrawPoint.getZ() + currentLocation.getZ());

                        // setting line width
                        glLineWidth(drawLineWidth);


                        glBegin(GL_LINES);
                        glColor3f(0.0f, 0.5f, 1.0f);
                        glVertex3f((float) currentLocation.getX(), (float) currentLocation.getY(), (float) currentLocation.getZ());
                        glColor3f(0.0f, 0.5f, 1.0f);
                        glVertex3f((float) newLocation.getX(), (float) newLocation.getY(), (float) newLocation.getZ());
                        glEnd();

                        currentLocation = newLocation;
                        break;

                    case CHANGE_INNER_VECTOR://change inner draw by adding to it
                        newLocation = new Point3D(
                                instruction.getIntX() * drawLenghtModifier.getX(),
                                instruction.getIntY() * drawLenghtModifier.getY(),
                                instruction.getIntZ() * drawLenghtModifier.getZ());
                        innerDrawPoint = innerDrawPoint.add(newLocation);
                        break;
                    case CHANGE_INNER_ANGLE:
                        Mat4RotXYZ rotationRadians = new Mat4RotXYZ(Math.toRadians(instruction.getIntX()), Math.toRadians(instruction.getIntY()), Math.toRadians(instruction.getIntZ()));
                        innerDrawPoint = innerDrawPoint.mul(rotationRadians);
                        break;
                    default:
                        System.err.println("unsupported operation type");
                        break;
                }

            }
            innerDrawPoint = resetPoint;
        }


        //no idea what this is but it works

        glDisable(GL_VERTEX_ARRAY);
        glDisable(GL_COLOR_ARRAY);
        glDisable(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);

    }

}

