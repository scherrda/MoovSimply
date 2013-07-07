package fr.duchesses.moov.models;

import lombok.Data;

@Data
public class Direction implements Comparable {

    private String direction;
    private String time;

    public Direction() {
        super();
    }

    public Direction(String direction, String time) {
        this.direction = direction;
        this.time = time;
    }

    @Override
    public int compareTo(Object o) {
        Direction otherDirection = (Direction) o;
        return this.time.compareTo(otherDirection.getTime());
    }
}
