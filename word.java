package com.javarush.task.task22.task2209;

import java.util.Stack;

public class word {

    private Stack<Integer> viewed;
    private Stack<Integer> available;

    public word() {
        this.viewed = new Stack<>();
        this.available = new Stack<>();
    }

    public Stack<Integer> getViewed() {
        return viewed;
    }

    public Stack<Integer> getAvailable() {
        return available;
    }

    public void addViewed(int i) {
        this.viewed.push(i);
    }

    public void addAvailable(int i) {
        this.available.push(i);
    }

    public void moveToAvailable(){
        while(viewed.size() != 0)
        {
            available.push(viewed.pop());
        }
    }

}
