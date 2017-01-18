package net.magicmantis.src.view.GUI;

import net.magicmantis.src.services.TextEngine;

import java.util.concurrent.Callable;

/**
 * Switch class: similar to button, but used to dictate whether an option should be on or off.
 * Creates two SwitchButtons, which behave similarly to Buttons, but only draw as active when
 * the linked Switch instance's 'on' variable matches their 'onButton' value.
 */
public class Switch extends GUIElement {

    private String text;
    private boolean on;
    private SwitchButton onButton, offButton;
    private Callable<Void> onAction, offAction;

    public Switch(int x, int y, int width, int height, String text, boolean currentValue, Callable<Void> onAction, Callable<Void> offAction) {
        super(x, y, width, height);
        this.text = text;
        this.on = currentValue;
        this.onAction = onAction;
        this.offAction = offAction;
        onButton = new SwitchButton(x + width + 10, y, width, height, "On", true, this);
        offButton = new SwitchButton(x + width * 2 + 20, y, width, height, "Off", false, this);
    }

    public void update() {
        onButton.update();
        offButton.update();
    }

    public void turnOn() {
        this.on = true;
        try {
            onAction.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOff() {
        this.on = false;
        try {
            offAction.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    public void mouseEvent(int button) {
        onButton.mouseEvent(button);
        offButton.mouseEvent(button);
    }

    public void draw() {
        TextEngine.drawText(text, x - width / 2, y - height / 2, width, height / 2, false);
        onButton.draw();
        offButton.draw();
    }

}
