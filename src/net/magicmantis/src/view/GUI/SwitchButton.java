package net.magicmantis.src.view.GUI;

import net.magicmantis.src.view.GUI.Button;
import net.magicmantis.src.view.GUI.Switch;

import java.util.concurrent.Callable;

/**
 * SwitchButton class: this subclass of button contains a reference to a master switch. When it is
 * activated, it will flip the switch to match the 'onButton' variable passed to it at creation.
 */
public class SwitchButton extends Button {

    private Switch master;
    private boolean onButton;

    public SwitchButton(int x, int y, int width, int height, String text, boolean onButton, Switch master) {
        super(x, y, width, height, text, new Callable<Void>() {
            public Void call() {
                if (onButton) master.turnOn();
                else master.turnOff();
                return null;
            }
        });
        this.onButton = onButton;
        this.master = master;
    }

    @Override
    public void update() {
        setActive((master.isOn() == onButton));
    }
}
