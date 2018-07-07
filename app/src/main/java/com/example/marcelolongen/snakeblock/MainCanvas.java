package com.example.marcelolongen.snakeblock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainCanvas extends View {
    private ArrayList<Rect> blocks = new ArrayList<>();
    private int animationTick = 0;
    int[] blockPosition = new int[5];
    int velocity = 5;


    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int area = canvas.getWidth() / 5;
        final int height = canvas.getHeight();

        Paint blocksColor = new Paint();
        blocksColor.setARGB(255, 66, 155, 244);

        blockPosition[0] = 0;
        blockPosition[1] = area;
        blockPosition[2] =  area + area;
        blockPosition[3] = area + area + area;
        blockPosition[4] = area + area + area + area;

        Block target = new Block(canvas.getWidth()/2 - 25, canvas.getWidth() / 2 + 25,canvas.getHeight() - 100, canvas.getHeight() -50);
        Rect targetRect = new Rect(target.left, target.top, target.right, target.bottom);

            if (animationTick % 50 == 0) {
                int random = (int) (Math.random() * 4);
                if (random == 0) {
                    drawOne();
                } else if (random == 1) {
                    drawTwo();
                } else if (random == 2) {
                    drawThree();
                } else {
                    drawFour();
                }

        }

        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).bottom += velocity;
            blocks.get(i).top += velocity;
            canvas.drawRect(blocks.get(i), blocksColor);
            checkIfHit(target);

        }
        Paint randomTarg = new Paint();
        randomTarg.setARGB(255, 255, 0, 0);
        canvas.drawRect(targetRect,randomTarg);

        checkIfBlockOnScreen(height);
        animationTick++;

    }

    private void checkIfBlockOnScreen(int height) {
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).bottom > height) {
                blocks.remove(this.blocks.get(i));
            }
        }
    }

    public void drawOne() {
        Block block = new Block(blockPosition[(int) (Math.random() * 5)]);
        Rect rectangle = new Rect(block.left, block.top, block.right, block.bottom);

        blocks.add(rectangle);
    }

    public void drawTwo() {
        int firstBlock = (int) (Math.random() * 5);
        int secondBlock = (int) (Math.random() * 5);

        Block block1 = new Block(blockPosition[firstBlock]);
        Block block2 = new Block(blockPosition[secondBlock]);
        Rect rectangle1 = new Rect(block1.left, block1.top, block1.right, block1.bottom);
        Rect rectangle2 = new Rect(block2.left, block2.top, block2.right, block2.bottom);

        blocks.add(rectangle1);
        blocks.add(rectangle2);
    }

    public void drawThree() {
        int firstBlock = (int) (Math.random() * 5);
        int secondBlock = (int) (Math.random() * 5);
        int thirdBlock = (int) (Math.random() * 5);


        Block block1 = new Block(blockPosition[firstBlock]);
        Block block2 = new Block(blockPosition[secondBlock]);
        Block block3 = new Block(blockPosition[thirdBlock]);
        Rect rectangle1 = new Rect(block1.left, block1.top, block1.right, block1.bottom);
        Rect rectangle2 = new Rect(block2.left, block2.top, block2.right, block2.bottom);
        Rect rectangle3 = new Rect(block3.left, block3.top, block3.right, block3.bottom);


        blocks.add(rectangle1);
        blocks.add(rectangle2);
        blocks.add(rectangle3);
    }

    public void drawFour() {
        int firstBlock = (int) (Math.random() * 5);
        int secondBlock = (int) (Math.random() * 5);
        int thirdBlock = (int) (Math.random() * 5);
        int fourthBlock = (int) (Math.random() * 5);

        Block block1 = new Block(blockPosition[firstBlock]);
        Block block2 = new Block(blockPosition[secondBlock]);
        Block block3 = new Block(blockPosition[thirdBlock]);
        Block block4 = new Block(blockPosition[fourthBlock]);
        Rect rectangle1 = new Rect(block1.left, block1.top, block1.right, block1.bottom);
        Rect rectangle2 = new Rect(block2.left, block2.top, block2.right, block2.bottom);
        Rect rectangle3 = new Rect(block3.left, block3.top, block3.right, block3.bottom);
        Rect rectangle4 = new Rect(block4.left, block4.top, block4.right, block4.bottom);

        blocks.add(rectangle1);
        blocks.add(rectangle2);
        blocks.add(rectangle3);
        blocks.add(rectangle4);
    }


    public void checkIfHit(Block target) {
        for (int i = 0; i < blocks.size(); i++) {

                for (int j = 0; j < velocity;j++) {
                    if (target.top + j == blocks.get(i).bottom) {
                        if (blocks.get(i).left < target.left && target.left < blocks.get(i).right || blocks.get(i).right < target.right && target.right < blocks.get(i).left) {
                            Toast.makeText(getContext(), "hit", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }


         }
    }


}
