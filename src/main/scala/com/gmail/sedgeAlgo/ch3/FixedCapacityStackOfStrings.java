package com.gmail.sedgeAlgo.ch3;

import java.util.NoSuchElementException;

/**
 * Created by rayanral on 5/30/16.
 */
public class FixedCapacityStackOfStrings {

    private String[] array;
    private int n = 0;

    FixedCapacityStackOfStrings(int size)  {
        array = new String[size];
    }

    public String pop() {
        if(n == 0) throw new NoSuchElementException();
        return array[--n];
    }

    public void push(String in) {
        if(n == array.length) throw new StackOverflowError();
        array[n++] = in;
    }

    public boolean isEmpty() {return n == 0; }

    public boolean isFull() {return n == array.length; }

    public int size() {return n;}
}

class FixedCapacityStack<Item> {

    private Item[] array;
    private int n = 0;

    FixedCapacityStack(int size)  {
        array = (Item[]) new Object[size];
    }

    public Item pop() {
        if(n == 0) throw new NoSuchElementException();
        return array[--n];
    }

    public void push(Item in) {
        if(n == array.length) throw new StackOverflowError();
        array[n++] = in;
    }

    public boolean isEmpty() {return n == 0; }

    public int size() {return n;}
}
