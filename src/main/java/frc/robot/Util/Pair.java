package frc.robot.Util;

/**
 * This class represents a pair of values
 */
public class Pair<T> {
    public T left;
    public T right;

    public Pair(T left, T right) {
        this.left = left;
        this.right = right;
    }

    public Pair(T val) {
        this(val, val);
    }

    public <To> Pair<To> map(Mapper<T, To> mapper) {
        return new Pair<To>(mapper.map(left), mapper.map(right));
    }
}
