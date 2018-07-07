package com.example.marcelolongen.snakeblock;

public class Block {
    public int left;
    public int right;
    public int top;
    public int bottom;


    public Block(int left) {
        this.left = left;
        this.right = left + 150;
        this.bottom = 150;
        this.top = 0;
    }

    public Block(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }
}
