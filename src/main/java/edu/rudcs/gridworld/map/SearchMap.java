package edu.rudcs.gridworld.map;

import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.map.actor.Goal;
import edu.rudcs.gridworld.map.actor.Wall;

public class SearchMap {

    private static final int MAX_STEPS = 100000;

    protected int rows;
    protected int cols;
    protected Agent agent;
    protected Map<ActorType, List<Location>> objects;
    protected ActorWorld world;

    public SearchMap() {
        rows = 0;
        cols = 0;
        agent = new Agent();
        objects = new HashMap<ActorType, List<Location>>();
        world = null;
    }

    private Actor toActor(ActorType at) {
        switch (at) {
        case WALL:
            return new Wall();
        case START:
            return agent;
        case GOAL:
            return new Goal();
        default:
            return null;
        }
    }

    public void loadAgent(Agent agent) {
        this.agent = agent;
    }

    public void loadMap(String path) {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            String line = br.readLine();
            String[] values = line.split(" ");
            assert values.length == 2;
            rows = Integer.parseInt(values[0]);
            cols = Integer.parseInt(values[1]);
            objects.clear();
            for (int i = 0; i < rows; i++) {
                line = br.readLine();
                values = line.split(" ");
                for (int j = 0; j < cols; j++) {
                    ActorType at = ActorType.fromChar(values[j].charAt(0));
                    if (!ActorType.ROAD.equals(at)) {
                        if (!objects.containsKey(at)) {
                            objects.put(at, new ArrayList<Location>());
                        }
                        objects.get(at).add(new Location(i, j));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    public void loadWorld() {
        world = new ActorWorld(new BoundedGrid<Actor>(rows, cols));
        for (ActorType at : objects.keySet()) {
            List<Location> locs = objects.get(at);
            for (Location l : locs) {
                Actor actor = toActor(at);
                if (actor == null) {
                    continue;
                }
                world.add(l, actor);
            }
        }
    }

    public List<Location> getGoal() {
        return objects.containsKey(ActorType.GOAL) ? objects
                .get(ActorType.GOAL) : null;
    }

    public List<Location> getStart() {
        return objects.containsKey(ActorType.START) ? objects
                .get(ActorType.START) : null;
    }

    public void show() {
        if (world != null) {
            world.show();
        }
    }

    public void run() {
        int steps = 0;
        while (!agent.isEnd() && steps <= MAX_STEPS) {
            agent.act();
        }
    }
}
