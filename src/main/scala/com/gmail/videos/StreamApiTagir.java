package com.gmail.videos;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by rayanral on 9/27/16.
 */
public class StreamApiTagir {

    public static void main(String[] args) {
        List<String> strings = new LinkedList<String>();
        strings.add("Some");
        strings.add("other");
        strings.add("String");
        strings.stream()
                .map(x -> x.substring(1))
                .filter(x -> x.startsWith("t"))
                .forEach(System.out::println);
    }




}
