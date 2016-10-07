package com.gmail.sedgeAlgo.ch3;

import java.util.Iterator;

/**
 * Created by rayanral on 5/26/16.
 */
public class Stack<Item> implements Iterable<Item> {

    private Item[] array;
    private int n = 0;

    Stack() {
        array = (Item[]) new Object[10];
    }

    public void push(Item item) {
        if(n == array.length) resize(n * 2);
        array[n++] = item;
    }

    public Item pop() {
        Item item = array[--n];
        array[n] = null;
        if(n > 0 && n < array.length / 4) resize(n / 2);
        return item;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        System.arraycopy(array, 0, temp, 0, array.length);
        array = temp;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            @Override
            public boolean hasNext() {
                return array[n + 1] != null;
            }

            @Override
            public Item next() {
                return pop();
            }
        };
    }
}
