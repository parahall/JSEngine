package com.academy.android.jsengine;

public class Fibonacchi {

    public int count;

    public Fibonacchi() {
        count = 0;
    }

    public int calculate(int i) {
        count++;
        if (i <= 2) {
            return 1;
        } else {
            return calculate(i - 1) + calculate(i - 2);
        }
    }
}
