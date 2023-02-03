package frc.robot.Util;

interface PairMapper<T, R> {
    R run(T p);
}

public class Pair<T> {
    public T left;
    public T right;

    public Pair(T left, T right) {
        this.left = left;
        this.right = right;
    }

    public <R> Pair<R> map(PairMapper<T, R> p) {
        return new Pair<R>(p.run(left), p.run(right));
    }
}
