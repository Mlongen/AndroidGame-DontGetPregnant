package com.example.marcelolongen.snakeblock;

public class Block {
    public int left;
    public int right;
    public int top;
    public int bottom;
    public int lives;




    public Block(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.lives = (int) (Math.random() * 12);
    }
}
