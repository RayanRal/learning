package com.gmail.sedgeAlgo.ch2;

/**
 * Created by rayanral on 5/26/16.
 */
public class StringCheck {

    public static void main(String[] args) {
        String input = args[0];
        String target = args[1];
        boolean result = checkStrings(input, target);
        System.out.println(result);
    }

    private static boolean checkStrings(String input, String target) {
        if(input.length() != target.length()) return false;
        for(int i = 0; i < input.length(); i++) {
            String fp = input.substring(0, i);
            String sp = input.substring(i, input.length());
            System.out.println(sp + fp);
            if(target.equals(sp + fp)) return true;
        }
        return false;
    }

}
