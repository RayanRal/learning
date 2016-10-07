package com.gmail.sedgeAlgo.ch3;

import java.util.Iterator;

/**
 * Created by rayanral on 5/30/16.
 */
public class LinkedList<Item> implements Iterable<Item> {

    private Node first;

    public void enqueueLast(Item item) {
        Node node = first;
        while(node.next != null) {
            node = node.next;
        }
        node.next = new Node(item);
    }

    public int max(IntNode first) {
        int max = 0;
        IntNode current = first;
        while (current.next != null) {
            int curValue = current.item;
            if(curValue > max) max = curValue;
            current = current.next;
        }
        return max;
    }
    //                                  init at 0
    public int maxRec(IntNode node, int curMax) {
        int curValue = node.item;
        if(curValue > curMax) curMax = curValue;
        if(node.next == null) return curMax;
        else return maxRec(node.next, curMax);
    }

    public void enqueueFirst(Item item) {
        Node newFirst = new Node(item);
        newFirst.next = first;
        first = newFirst;
    }

    public Item dequeueLast() {
        Node node = first;
        while(node.next.next != null) {
            node = node.next;
        }
        Item r = node.next.item;
        node.next = null;
        return r;
    }

    public Item dequeue() {
        Item r = first.item;
        first = first.next;
        return r;
    }

    private class Node {

        Node(Item item){
            this.item = item;
        }

        Item item;
        Node next;
    }

    private class IntNode {

        IntNode(int item){
            this.item = item;
        }

        int item;
        IntNode next;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {

            private Node node;

            @Override
            public boolean hasNext() {
                return node.next != null;
            }

            @Override
            public Item next() {
                Item current = node.item;
                node = node.next;
                return current;
            }
        };
    }

}
