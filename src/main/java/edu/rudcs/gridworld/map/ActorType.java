package edu.rudcs.gridworld.map;

public enum ActorType {

    ROAD("0"), WALL("1"), AGENT("s"), GOAL("e"), SHADOW("2");

    private String value;

    private ActorType(String value) {
        this.value = value;
    }

    public static ActorType fromString(String value) {
        for (ActorType t : ActorType.values()) {
            if (value.equals(t.value)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

}
