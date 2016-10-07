package com.gmail.sedgeAlgo.ch3;

/**
 * Created by rayanral on 5/30/16.
 */
public class DoubleLinkedList<Item> {

    private Node first;

    public void enqueueFirst(Item item) {
        Node oldFirst = first;
        first = new Node(item, null, oldFirst);
    }

    public Item dequeueFirst() {
        Item r = first.item;
        first.next.previous = null;
        first = first.next;
        return r;
    }

    public Item dequeueLast() {
        Node node = first;
        while(node.next != null) node = node.next;
        Item r = node.item;
        node.previous.next = null;
        return r;
    }

    public void enqueueLast(Item item) {
        Node node = first;
        while(node.next != null) node = node.next;
        node.next = new Node(item, node, null);
    }


    private class Node {

        Node(Item item){
            this.item = item;
        }

        Node(Item item, Node prev, Node next) {
            this.item = item;
            this.previous = prev;
            this.next = next;
        }

        Item item;
        Node next;
        Node previous;
    }

}
