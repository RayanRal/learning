package com.gmail.sedgeAlgo.ch3;

/**
 * Created by rayanral on 5/30/16.
 */
public class Stats {

    public static void main(String[] args) {
        Bag<Double> numbers = new Bag<>();
        int n = numbers.size();

        double sum = 0.0;
        for (double x: numbers) sum += x;
        double mean = sum / n; //mean

        sum = 0.0;
        for(double x: numbers) sum += (x - mean) * (x - mean);
        double std = Math.sqrt(sum/(n-1)); //standard deviation

    }

    public static int[] readInts(String name) {
        ReassizingArrayQueue<Integer> q = new ReassizingArrayQueue<>();
        q.enqueue(Integer.parseInt(name));
        int n = q.size();
        int[] a = new int[n];
        for(int i = 0; i < n; i++)
            a[i] = q.dequeue();
        return a;
    }

    public static void reverse(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);

        for (int i: stack) System.out.println(i);
    }

}
