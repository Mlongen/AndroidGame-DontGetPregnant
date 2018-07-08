package com.example.marcelolongen.snakeblock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MainCanvas extends View {
    private AnimationHelper mAnimationHelper = new AnimationHelper(this, 100);
    private ArrayList<Rect> blocks = new ArrayList<>();
    private int animationTick = 0;
    int[] blockPosition = new int[5];
    int velocity = 10;
    int left = 0;
    int area;
    int count = 0;
    private boolean hasStarted = false;


    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        area = canvas.getWidth() / 5;
        final int height = canvas.getHeight();
        final int width = canvas.getWidth();
        Paint blocksColor = new Paint();
        Paint red = new Paint();
        red.setARGB(255, 255, 0, 0);
        red.setTextSize(40);
        red.setTypeface(Typeface.MONOSPACE);
        canvas.drawText("Score " + count , width / 2 - 100, 400, red);
        canvas.drawText("Speed " + velocity , width / 2 - 100, 600, red);

        blocksColor.setARGB(255, 66, 155, 244);
        blockPosition[0] = 0;
        blockPosition[1] = area;
        blockPosition[2] =  area + area;
        blockPosition[3] = area + area + area;
        blockPosition[4] = area + area + area + area;


        Block target = new Block(left, left + (area / 2),canvas.getHeight()-500, canvas.getHeight() - 500 - (blockPosition[1] / 2));
        Rect targetRect = new Rect(left, target.top, target.right, target.bottom);

            if (animationTick % 50 == 0) {
                int random = (int) (Math.random() * 5);
                for (int i = 0; i < random; i ++) {
                    dropBlock();
                }

            }
        if (animationTick % 500 == 0) {
            int random = (int) (Math.random() * 5);
            for (int i = 0; i < random; i ++) {
                dropBlock();
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

        if (animationTick % 50 == 0) {
            velocity++;
        }
        count++;

    }

    private void checkIfBlockOnScreen(int height) {
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).bottom > height) {
                blocks.remove(this.blocks.get(i));
            }
        }
    }

    public void dropBlock() {
        int firstBlock = (int) (Math.random() * 5);
        Block block = new Block(blockPosition[firstBlock], blockPosition[firstBlock] + area, area, 0);
        Rect rectangle = new Rect(block.left, block.top, block.right, block.bottom);

        blocks.add(rectangle);
    }


    //TODO: Improve crash logic, it is not perfect
    public void checkIfHit(Block target) {
        for (int i = 0; i < blocks.size(); i++) {
                for (int j = 0; j < velocity;j++) {
                    if (target.top + j == blocks.get(i).top) {
                        if (blocks.get(i).left < target.left && target.left < blocks.get(i).right || blocks.get(i).right < target.right && target.right < blocks.get(i).left) {
                            Intent intent = new Intent(getRootView().getContext(), Finish.class);
                            intent.putExtra("score", count);
                            getContext().startActivity(intent);
                            break;
                        }
                    }
                }


         }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (!hasStarted) {
                    mAnimationHelper.start();
                    hasStarted = true;
                }
                left = (int) event.getX() - area / 2;
                invalidate();
                break;
            }
        }
        return true;
    }
}
