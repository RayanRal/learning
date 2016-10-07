package com.gmail.sedgeAlgo.ch3;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rayanral on 5/31/16.
 */
public class RandomBag<Item> implements Iterable<Item> {

    private Item[] array;
    private int current;

    public RandomBag(int size) {
        array = (Item[]) new Object[size];
    }

    public boolean isEmpty() {
        return current == 0;
    }

    public int size() {
        return current;
    }

    void add(Item item) {
        array[current++] = item;
    }


    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {

            private Item[] randomArray = (Item[]) new Object[array.length];

//            public Iterator() {
//                List<Item> temp = Arrays.asList(array);
//                Collections.shuffle(temp);
//                randomArray = (Item[]) temp.toArray();
//            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Item next() {
                return null;
            }
        };
    }
}
