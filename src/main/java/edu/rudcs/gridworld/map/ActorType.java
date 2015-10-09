package edu.rudcs.gridworld.map;

public enum ActorType {

    ROAD('0'), WALL('1'), SHADOW('2'), START('s'), GOAL('g');

    private char value;

    private ActorType(char value) {
        this.value = value;
    }

    public static ActorType fromChar(char value) {
        for (ActorType at : ActorType.values()) {
            if (at.value == value) {
                return at;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public char toChar() {
        return this.value;
    }

}
