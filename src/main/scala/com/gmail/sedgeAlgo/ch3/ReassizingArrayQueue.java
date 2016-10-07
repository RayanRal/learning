package com.gmail.sedgeAlgo.ch3;

import java.util.Iterator;

/**
 * Created by rayanral on 5/26/16.
 */
public class ReassizingArrayQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int head = 0;
    private int tail = 0;

    ReassizingArrayQueue() {
        array = (Item[]) new Object[1];
    }

    void enqueue(Item item) {
//        if(tail + 1 == array.length && head == 0) resize(array.length * 2);
//        if(tail + 1 == array.length) tail = 0;
//        if(tail == head) resize(2);
        array[tail++] = item;
    }

    Item dequeue() {
        Item r = array[head];
        array[head++] = null;
        return r;
    }

    boolean isEmpty() {
        return head == array.length;
    }

    int size() { return array.length; }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        System.arraycopy(array, 0, temp, 0, array.length);
        array = temp;
    }

    @Override
    public Iterator<Item> iterator() {
        return null;
    }
}
