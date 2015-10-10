package edu.rudcs.gridworld.agent;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import edu.rudcs.gridworld.map.Shadow;
import edu.rudcs.gridworld.map.Wall;

public class Agent extends Actor {

    public static final Color PATH_COLOR = new Color(255, 255, 153);

    protected final Logger log = Logger.getLogger(Agent.class.getName());
    
    protected boolean end = false;

    public Agent() {
        //setColor(new Color(0, 221, 0));
        setColor(Color.RED);
        try {
            FileHandler fileHander = new FileHandler("log/" + new Date());
            log.addHandler(fileHander);
            fileHander.setFormatter(new SimpleFormatter());
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void info(Object format, Object... args) {
        log.info(String.format(format.toString(), args));
    }

    protected void warn(Object format, Object... args) {
        log.warning(String.format(format.toString(), args));
    }

    protected void printf(Object format, Object... args) {
        System.out.println(String.format(format.toString(), args));
    }

    @Override
    public Agent clone() {
        return new Agent();
    }
    
    public boolean isEnd() {
        return end;
    }

    public void turnLeft() {
        setDirection(getDirection() + Location.LEFT);
    }

    public void turnRight() {
        setDirection(getDirection() + Location.RIGHT);
    }

    public void turnBack() {
        setDirection(getDirection() + Location.HALF_CIRCLE);
    }

    public boolean canMove() {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return false;
        }
        Location loc = getLocation();
        Location nextLoc = loc.getAdjacentLocation(getDirection());
        if (!grid.isValid(nextLoc)) {
            return false;
        }
        Actor nextActor = grid.get(nextLoc);
        return nextActor == null || !(nextActor instanceof Wall);
    }

    public void move(int direction) {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(direction);
        if (grid.isValid(next)) {
            moveTo(next);
        } else {
            removeSelfFromGrid();
        }
    }

    @Override
    public void moveTo(Location loc) {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        Location oldLoc = getLocation();
        super.moveTo(loc);
        Shadow shadow = new Shadow(Location.NORTH);
        shadow.putSelfInGrid(grid, oldLoc);
    }

    public void forward() {
        move(getDirection());
    }

    public String hash(Location loc) {
        return loc.getRow() + "," + loc.getCol();
    }

    @Override
    public void act() {
        if (canMove()) {
            forward();
        } else {
            turnRight();
        }
    }

}
