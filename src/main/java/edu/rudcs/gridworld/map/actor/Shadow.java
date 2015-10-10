package edu.rudcs.gridworld.map.actor;

import java.awt.Color;

public class Shadow extends StaticActor {

    public Shadow(int direction) {
        setColor(new Color(210, 255, 210));
        setDirection(direction);
    }

}
