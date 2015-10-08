package edu.rudcs.gridworld.map;

public enum ActorType {

    ROAD('0'), WALL('1'), SHADOW('2'), START('s'), GOAL('g');

    private char value;

    private ActorType(char value) {
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
