package net.magicmantis.src.services;

import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * TextEngine class: this class contains functions for drawing text in OpenGL.
 */

public class TextEngine {

    public static final double textAspectRatio = 3d / 2d;

    public static String[] namebank = {"Ramirez", "Jady", "Goose", "Flyboy", "Thunder", "Tropico", "Bugs", "Fatso", "Captain",
            "Cosmo", "Badger", "Aslan", "Syndrome", "Gator", "Nutsbe", "Frog", "Wiki", "Paps", "Razor", "Vampire", "Matta", "Mikey",
            "Zaire", "Yak", "X", "Wolf", "Warbird", "Ahab", "Rodeo", "Dusty", "Thirsty", "Soda", "Slave", "Rouge", "Bat",
            "Uncle Sam", "Fish", "Stark", "Meat", "Data", "Wheatly", "Clank"};


    /**
     * Draw a Single character. (currently must be a capital letter)
     *
     * @param c      - character to draw.
     * @param x      - x location for the bottom left corner of the character
     * @param y      - y location for the bottom left corner of the character
     * @param width  - width of the character
     * @param height - height of the character
     * @param xView  - horizontal offset due to camera position
     * @param yView  - vertical offset due to comera position
     * @param color  - color to draw the character
     * @param size   - thickness of the lines used to draw the character
     */
    public static void drawCharacter(char c, double x, double y, double width,
                                     double height, int xView, int yView, Color color, double size) {
        //get the color from the entity
        int col[] = {color.getRed(), color.getGreen(), color.getBlue()};
        GL11.glColor3d(col[0] / 255f, col[1] / 255f, col[2] / 255f);
        GL11.glLineWidth((float) size);

        x -= xView;
        y -= yView;

        glBegin(GL_LINE_LOOP);
        switch (c) {
            case 'A':
                glVertex2d(x, y);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + width, y);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + width / 4, y + height / 2);
                break;
            case 'B':
                glVertex2d(x, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 8);
                glVertex2d(x + width, y + 3 * height / 8);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + width, y + 5 * height / 8);
                glVertex2d(x + width, y + 7 * height / 8);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x, y + height);
                break;
            case 'C':
                glVertex2d(x + width, y + height);
                glVertex2d(x + width / 4, y + height);
                glVertex2d(x, y + 3 * height / 4);
                glVertex2d(x, y + height / 4);
                glVertex2d(x + width / 4, y);
                glVertex2d(x + width, y);
                glVertex2d(x + width / 4, y);
                glVertex2d(x, y + height / 4);
                glVertex2d(x, y + 3 * height / 4);
                glVertex2d(x + width / 4, y + height);
                break;
            case 'D':
                glVertex2d(x, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 4);
                glVertex2d(x + width, y + 3 * height / 4);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x, y + height);
                break;
            case 'E':
                glVertex2d(x, y);
                glVertex2d(x + width, y);
                glVertex2d(x, y);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width, y + height / 2);
                glVertex2d(x, y + height / 2);
                glVertex2d(x, y + height);
                glVertex2d(x + width, y + height);
                glVertex2d(x, y + height);
                break;
            case 'F':
                glVertex2d(x, y);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width, y + height / 2);
                glVertex2d(x, y + height / 2);
                glVertex2d(x, y + height);
                glVertex2d(x + width, y + height);
                glVertex2d(x, y + height);
                break;
            case 'G':
                glVertex2d(x + width, y + 3 * height / 4);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x + width / 4, y + height);
                glVertex2d(x, y + 3 * height / 4);
                glVertex2d(x, y + height / 4);
                glVertex2d(x + width / 4, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 4);
                glVertex2d(x + width, y + height / 2);
                glVertex2d(x + width / 2, y + height / 2);
                glVertex2d(x + width, y + height / 2);
                glVertex2d(x + width, y + height / 4);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width / 4, y);
                glVertex2d(x, y + height / 4);
                glVertex2d(x, y + 3 * height / 4);
                glVertex2d(x + width / 4, y + height);
                glVertex2d(x + 3 * width / 4, y + height);
                break;
            case 'H':
                glVertex2d(x, y);
                glVertex2d(x, y + height);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width, y + height / 2);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width, y);
                glVertex2d(x + width, y + height / 2);
                glVertex2d(x, y + height / 2);
                break;
            case 'J':
                glVertex2d(x + width, y + height);
                glVertex2d(x + width, y + height / 4);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width / 4, y);
                glVertex2d(x, y + height / 4);
                glVertex2d(x + width / 4, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 4);
                break;
            case 'I':
                glVertex2d(x, y);
                glVertex2d(x + width, y);
                glVertex2d(x + width / 2, y);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + width, y + height);
                glVertex2d(x, y + height);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + width / 2, y);
                break;
            case 'K':
                glVertex2d(x, y);
                glVertex2d(x, y + height);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width, y + height);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width, y);
                glVertex2d(x, y + height / 2);
                break;
            case 'L':
                glVertex2d(x, y + height);
                glVertex2d(x, y);
                glVertex2d(x + width, y);
                glVertex2d(x, y);
                break;
            case 'M':
                glVertex2d(x, y);
                glVertex2d(x, y + height);
                glVertex2d(x + width / 2, y);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width, y);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width / 2, y);
                glVertex2d(x, y + height);
                break;
            case 'N':
                glVertex2d(x, y);
                glVertex2d(x, y + height);
                glVertex2d(x + width, y);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width, y);
                glVertex2d(x, y + height);
                break;
            case 'O':
                glVertex2d(x + width, y + 3 * height / 4);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x + width / 4, y + height);
                glVertex2d(x, y + 3 * height / 4);
                glVertex2d(x, y + height / 4);
                glVertex2d(x + width / 4, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 4);
                break;
            case 'P':
                glVertex2d(x, y);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + width, y + 5 * height / 8);
                glVertex2d(x + width, y + 7 * height / 8);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x, y + height);
                break;
            case 'Q':
                glVertex2d(x + width, y + 3 * height / 4);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x + width / 4, y + height);
                glVertex2d(x, y + 3 * height / 4);
                glVertex2d(x, y + height / 4);
                glVertex2d(x + width / 4, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + 7 * width / 8, y + height / 8);
                glVertex2d(x + 3 * width / 4, y + height / 4);
                glVertex2d(x + width, y);
                glVertex2d(x + 7 * width / 8, y + height / 8);
                glVertex2d(x + width, y + height / 4);
                break;
            case 'R':
                glVertex2d(x, y);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + width, y + 5 * height / 8);
                glVertex2d(x + width, y + 7 * height / 8);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x, y + height);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width, y);
                glVertex2d(x, y + height / 2);
                break;
            case 'S':
                glVertex2d(x, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 8);
                glVertex2d(x + width, y + 3 * height / 8);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + 1 * width / 4, y + height / 2);
                glVertex2d(x, y + 5 * height / 8);
                glVertex2d(x, y + 7 * height / 8);
                glVertex2d(x + 1 * width / 4, y + height);
                glVertex2d(x + width, y + height);
                glVertex2d(x + 1 * width / 4, y + height);
                glVertex2d(x, y + 7 * height / 8);
                glVertex2d(x, y + 5 * height / 8);
                glVertex2d(x + 1 * width / 4, y + height / 2);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + width, y + 3 * height / 8);
                glVertex2d(x + width, y + height / 8);
                glVertex2d(x + 3 * width / 4, y);
                break;
            case 'T':
                glVertex2d(x, y + height);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + width / 2, y);
                glVertex2d(x + width / 2, y + height);
                break;
            case 'U':
                glVertex2d(x, y + height);
                glVertex2d(x, y + height / 4);
                glVertex2d(x + width / 4, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 4);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width, y + height / 4);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width / 4, y);
                glVertex2d(x, y + height / 4);
                break;
            case 'V':
                glVertex2d(x, y + height);
                glVertex2d(x + width / 2, y);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width / 2, y);
                break;
            case 'W':
                glVertex2d(x, y + height);
                glVertex2d(x + width / 4, y);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + width / 4, y);
                break;
            case 'X':
                glVertex2d(x, y);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width / 2, y + height / 2);
                glVertex2d(x + width, y);
                glVertex2d(x, y + height);
                glVertex2d(x + width / 2, y + height / 2);
                break;
            case 'Y':
                glVertex2d(x, y + height);
                glVertex2d(x + width / 2, y + height / 2);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width / 2, y + height / 2);
                glVertex2d(x + width / 2, y);
                glVertex2d(x + width / 2, y + height / 2);
                break;
            case 'Z':
                glVertex2d(x, y + height);
                glVertex2d(x + width, y + height);
                glVertex2d(x, y);
                glVertex2d(x + width, y);
                glVertex2d(x, y);
                glVertex2d(x + width, y + height);
                break;
            case '0':
                for (int i = 0; i < 360; i++) {
                    double rad = i * 3.14159f / 180.0f;
                    double xOff = Math.cos(rad) * width / 2f;
                    double yOff = Math.sin(rad) * height / 2f;
                    glVertex2d(x + width / 2 + xOff, y + height / 2 + yOff);
                }
                break;
            case '1':
                glVertex2d(x + width / 4, y + height * 7 / 8);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + width / 2, y);
                glVertex2d(x + width, y);
                glVertex2d(x, y);
                glVertex2d(x + width / 2, y);
                glVertex2d(x + width / 2, y + height);
                break;
            case '2':
                glVertex2d(x, y + height * 5 / 8);
                glVertex2d(x + width * 1 / 8, y + height * 6 / 8);
                glVertex2d(x + width * 2 / 8, y + height * 7 / 8);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + width * 6 / 8, y + height * 7 / 8);
                glVertex2d(x + width * 7 / 8, y + height * 6 / 8);
                glVertex2d(x + width, y + height * 5 / 8);
                glVertex2d(x + width, y + height * 4 / 8);
                glVertex2d(x, y);
                glVertex2d(x + width, y);
                glVertex2d(x, y);
                glVertex2d(x + width, y + height * 4 / 8);
                glVertex2d(x + width, y + height * 5 / 8);
                glVertex2d(x + width * 7 / 8, y + height * 6 / 8);
                glVertex2d(x + width * 6 / 8, y + height * 7 / 8);
                glVertex2d(x + width / 2, y + height);
                glVertex2d(x + width * 2 / 8, y + height * 7 / 8);
                glVertex2d(x + width * 1 / 8, y + height * 6 / 8);
                break;
            case '3':
                glEnd();
                glBegin(GL_LINE_LOOP);
                glVertex2d(x + width * 3 / 4, y);
                glVertex2d(x, y);
                glVertex2d(x + width * 3 / 4, y);
                glVertex2d(x + width, y + height / 8);
                glVertex2d(x + width, y + height * 3 / 8);
                glVertex2d(x + width * 3 / 4, y + height / 2);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width * 3 / 4, y + height / 2);
                glVertex2d(x + width, y + height * 5 / 8);
                glVertex2d(x + width, y + height * 7 / 8);
                glVertex2d(x + width * 3 / 4, y + height);
                glVertex2d(x, y + height);
                glVertex2d(x + width * 3 / 4, y + height);
                glVertex2d(x + width, y + height * 7 / 8);
                glVertex2d(x + width, y + height * 5 / 8);
                glVertex2d(x + width * 3 / 4, y + height / 2);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width * 3 / 4, y + height / 2);
                glVertex2d(x + width, y + height * 3 / 8);
                glVertex2d(x + width, y + height / 8);
                glVertex2d(x + width * 3 / 4, y);
                glVertex2d(x, y);
                break;
            case '4':
                glVertex2d(x + width * 3 / 4, y);
                glVertex2d(x + width * 3 / 4, y + height);
                glVertex2d(x + width * 3 / 4, y + height / 2);
                glVertex2d(x, y + height / 2);
                glVertex2d(x, y + height);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width, y + height / 2);
                glVertex2d(x + width * 3 / 4, y + height / 2);
                break;
            case '5':
                glVertex2d(x, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 8);
                glVertex2d(x + width, y + 3 * height / 8);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x, y + height / 2);
                glVertex2d(x, y + height);
                glVertex2d(x + width, y + height);
                glVertex2d(x, y + height);
                glVertex2d(x, y + height / 2);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + width, y + 3 * height / 8);
                glVertex2d(x + width, y + height / 8);
                glVertex2d(x + 3 * width / 4, y);
                break;
            case '6':
                glVertex2d(x + width, y + 3 * height / 4);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x + width / 4, y + height);
                glVertex2d(x, y + 3 * height / 4);
                glVertex2d(x, y + height / 4);
                glVertex2d(x + width / 4, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 4);
                glVertex2d(x + width * 3 / 4, y + height / 2);
                glVertex2d(x + width * 1 / 4, y + height / 2);
                glVertex2d(x, y + height / 4);
                glVertex2d(x + width * 1 / 4, y + height / 2);
                glVertex2d(x + width * 3 / 4, y + height / 2);
                glVertex2d(x + width, y + height / 4);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width / 4, y);
                glVertex2d(x, y + height / 4);
                glVertex2d(x, y + 3 * height / 4);
                glVertex2d(x + width / 4, y + height);
                glVertex2d(x + 3 * width / 4, y + height);
                break;
            case '7':
                glVertex2d(x, y + height);
                glVertex2d(x + width, y + height);
                glVertex2d(x + width / 2, y);
                glVertex2d(x + width, y + height);
                break;
            case '8':
                glVertex2d(x + width / 4, y);
                glVertex2d(x + 3 * width / 4, y);
                glVertex2d(x + width, y + height / 8);
                glVertex2d(x + width, y + 3 * height / 8);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + width / 4, y + height / 2);
                glVertex2d(x + 3 * width / 4, y + height / 2);
                glVertex2d(x + width, y + 5 * height / 8);
                glVertex2d(x + width, y + 7 * height / 8);
                glVertex2d(x + 3 * width / 4, y + height);
                glVertex2d(x + width / 4, y + height);
                glVertex2d(x, y + height * 7 / 8);
                glVertex2d(x, y + height * 5 / 8);
                glVertex2d(x + width / 4, y + height / 2);
                glVertex2d(x, y + height * 3 / 8);
                glVertex2d(x, y + height * 1 / 8);
                break;
            case '9':
                glVertex2d(x + width * 3 / 4, y);
                glVertex2d(x + width, y + height / 2);
                glVertex2d(x + width, y + height * 7 / 8);
                glVertex2d(x + width * 7 / 8, y + height);
                glVertex2d(x + width / 8, y + height);
                glVertex2d(x, y + height * 7 / 8);
                glVertex2d(x, y + height * 5 / 8);
                glVertex2d(x + width / 8, y + height / 2);
                glVertex2d(x + width, y + height / 2);
                break;
            case '-':
                glVertex2d(x, y + height / 2);
                glVertex2d(x + width, y + height / 2);
                break;
            case '%':
                for (int i = 0; i < 360; i++) {
                    double rad = i * 3.14159f / 180.0f;
                    double xOff = Math.cos(rad) * width / 4f;
                    double yOff = Math.sin(rad) * height / 4f;
                    glVertex2d(x + width / 4 + xOff, y + height * 3 / 4 + yOff);
                }
                glEnd();
                glBegin(GL_LINES);
                glVertex2d(x, y);
                glVertex2d(x + width, y + height);
                glEnd();
                glBegin(GL_LINE_LOOP);
                for (int i = 0; i < 360; i++) {
                    double rad = i * 3.14159f / 180.0f;
                    double xOff = Math.cos(rad) * width / 4f;
                    double yOff = Math.sin(rad) * height / 4f;
                    glVertex2d(x + width * 3 / 4 + xOff, y + height / 4 + yOff);
                }
                break;
            case ' ':
                break;
            default:
                glVertex2d(x + width / 4, y + height / 4);
                glVertex2d(x + width * 3 / 4, y + height / 4);
                glVertex2d(x + width * 3 / 4, y + height * 3 / 4);
                glVertex2d(x + width / 4, y + height * 3 / 4);
                break;
        }
        glEnd();
    }

    /**
     * Draw a string of text.
     *
     * @param text    - text to be draw
     * @param x       - x location to draw text
     * @param y       - y location to draw text
     * @param width   - width of the text box
     * @param height  - height of the text box
     * @param xView   - horizontal offset due to camera position
     * @param yView   - vertical offset due to camera position
     * @param stretch - true will force the letters to stretch to fill the text box, false will keep an aspect ratio for readability
     * @param color   - color to draw the text
     * @param size    - thickness of the lines used to draw the text
     */
    public static void drawText(String text, double x, double y, double width, double height, int xView, int yView, boolean stretch, Color color, double size) {
        //only uppercase characters can be draw currently (temporary)
        text = text.toUpperCase();

        //stretch the characters to fill the space provided
        if (stretch) {
            for (int i = 0; i < text.length(); i++) {
                drawCharacter(text.charAt(i), x + (i * width / text.length()), y, width / text.length() - 3, height - 1, xView, yView, color, size);
            }
        } else {
            //calculate new width and height to fit the preferred aspect ratio
            double adjustedWidth = width / text.length();
            double adjustedHeight = height;

            if ((adjustedHeight / adjustedWidth) > textAspectRatio) adjustedHeight = adjustedWidth * textAspectRatio;
            else adjustedWidth = adjustedHeight / textAspectRatio;
            adjustedWidth *= text.length();

            for (int i = 0; i < text.length(); i++) {
                drawCharacter(text.charAt(i), x + (i * adjustedWidth / text.length()) + (width - adjustedWidth) / 2,
                        y + (height - adjustedHeight) / 2, adjustedWidth / text.length() - 3, adjustedHeight - 1,
                        xView, yView, color, size);
            }
        }
    }

    public static void drawText(String text, double x, double y, double width, double height, int xView, int yView, boolean stretch) {
        drawText(text, x, y, width, height, xView, yView, stretch, Color.WHITE, 1);
    }

    public static void drawText(String text, double x, double y, double width, double height, boolean stretch) {
        drawText(text, x, y, width, height, 0, 0, stretch, Color.WHITE, 1);
    }
}
