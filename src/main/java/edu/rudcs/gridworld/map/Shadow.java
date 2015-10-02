package edu.rudcs.gridworld.map;

import java.awt.Color;

public class Shadow extends StaticObject {

    public Shadow(int direction) {
        setColor(new Color(210, 255, 210));
        setDirection(direction);
    }

}
