package edu.rudcs.gridworld.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Tester {

    public static void main(String[] args) {
        new Tester().func2();
    }
    
    public void func3() {
        System.out.println(Double.POSITIVE_INFINITY);
        System.out.println(Double.POSITIVE_INFINITY+10);
        System.out.println(Double.POSITIVE_INFINITY == (Double.POSITIVE_INFINITY+10.0));
    }
    
    public void func2() {
        System.out.println(new BigDecimal(10.2));
        System.out.println(new BigDecimal("10.2"));
        System.out.println(new BigDecimal(10000));
        System.out.println(new BigDecimal("10000"));
        System.out.println(new Double(10.2)+new Double(11.2)+new Double(123.34));
    }

    public void func1() {
        Map<String, Name> names = new HashMap<String, Name>();
        names.put("1", new Name("AAA", "AAAA"));
        names.put("2", new Name("BBB", "BBBB"));
        names.put("3", new Name("CCC", "CCCC"));
        names.put("4", new Name("DDD", "DDDD"));
        names.put("5", new Name("EEE", "EEEE"));
        names.put("6", new Name("FFF", "FFFF"));

        Name n = names.get("1");
        n.first = "aaa";
        n.last = "aaaa";

        System.out.println(names.get("1"));
    }

}

class Name {
    public String last;
    public String first;

    public Name(String last, String first) {
        this.last = last;
        this.first = first;
    }

    public String toString() {
        return last + ", " + first;
    }
}
