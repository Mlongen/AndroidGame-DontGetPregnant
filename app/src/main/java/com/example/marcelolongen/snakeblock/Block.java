package com.example.marcelolongen.snakeblock;

public class Block {
    public int left;
    public int right;
    public int top;
    public int bottom;


    public Block(int left) {
        this.left = left;
        this.right = left + 150;
        this.bottom = 0;
        this.top = 150;
    }


}
