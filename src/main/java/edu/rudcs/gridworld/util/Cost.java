package edu.rudcs.gridworld.util;

import java.math.BigDecimal;

public class Cost implements Comparable<Cost> {

    private static final BigDecimal INFINITE_VALUE = new BigDecimal("100000");

    public static final Cost INFINITE = new Cost(INFINITE_VALUE);

    private BigDecimal val;

    public Cost(BigDecimal val) {
        this.val = val;
    }

    public Cost(String val) {
        this.val = new BigDecimal(val);
    }

    public Cost(int val) {
        this.val = new BigDecimal(val);
    }

    private static boolean outOfBound(BigDecimal c1, BigDecimal c2) {
        return c1.compareTo(INFINITE_VALUE) >= 0
                && c2.compareTo(INFINITE_VALUE) >= 0;
    }

    public boolean gt(BigDecimal c) {
        if (outOfBound(this.val, c)) {
            return false;
        }
        return this.val.compareTo(c) > 0;
    }

    public boolean gt(Cost c) {
        return gt(c.val);
    }

    public boolean geq(BigDecimal c) {
        if (outOfBound(this.val, c)) {
            return true;
        }
        return this.val.compareTo(c) >= 0;
    }

    public boolean geq(Cost c) {
        return geq(c.val);
    }

    public boolean lt(BigDecimal c) {
        if (outOfBound(this.val, c)) {
            return false;
        }
        return this.val.compareTo(c) < 0;
    }

    public boolean lt(Cost c) {
        return lt(c.val);
    }

    public boolean leq(BigDecimal c) {
        if (outOfBound(this.val, c)) {
            return true;
        }
        return this.val.compareTo(c) <= 0;
    }

    public boolean leq(Cost c) {
        return leq(c.val);
    }

    public boolean eq(BigDecimal c) {
        if (outOfBound(this.val, c)) {
            return true;
        }
        return this.val.compareTo(c) == 0;
    }

    public boolean eq(Cost c) {
        return eq(c.val);
    }

    public boolean neq(BigDecimal c) {
        return !eq(c);
    }

    public boolean neq(Cost c) {
        return !eq(c);
    }

    public Cost add(BigDecimal c) {
        BigDecimal tmp = this.val.add(c);
        if (tmp.compareTo(INFINITE_VALUE) >= 0) {
            return INFINITE;
        }
        return new Cost(this.val.add(c));
    }

    public Cost add(Cost c) {
        return add(c.val);
    }

    public static int compare(Cost c1, Cost c2) {
        if (outOfBound(c1.val, c2.val)) {
            return 0;
        }
        return c1.val.compareTo(c2.val);
    }

    public static Cost min(Cost c1, Cost c2) {
        return c1.lt(c2) ? c1 : c2;
    }

    @Override
    public String toString() {
        if (this.val.compareTo(INFINITE_VALUE) >= 0) {
            return "INF";
        }
        return this.val.toPlainString();
    }

    @Override
    public int compareTo(Cost c) {
        if (outOfBound(this.val, c.val)) {
            return 0;
        }
        return this.val.compareTo(c.val);
    }
}
