package com.example.marcelolongen.snakeblock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MainCanvas extends View {
    private AnimationHelper mAnimationHelper = new AnimationHelper(this, 50);
    private ArrayList<Rect> blocks = new ArrayList<>();
    private int animationTick = 0;
    int[] blockPosition = new int[5];
    int velocity = 10;
    int left = 0;
    int left2 = 0;
    int left3 = 0;
    int left4 = 0;
    int area;
    int count = 0;
    int lives = 4;
    int width = getWidth();
    private boolean hasStarted = false;
    private boolean moving = false;

    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        area = canvas.getWidth() / 5;
        int height = canvas.getHeight();
        width = canvas.getWidth();
        Paint blocksColor = new Paint();
        Paint red = new Paint();
        red.setARGB(255, 255, 0, 0);
        red.setTextSize(40);
        red.setTypeface(Typeface.MONOSPACE);
        canvas.drawText("Score " + count , width / 2 - 100, 400, red);
        canvas.drawText("Speed " + velocity , width / 2 - 100, 600, red);
        canvas.drawText(lives + "" , left, canvas.getHeight()-600, red);
        blocksColor.setARGB(255, 66, 155, 244);
        blockPosition[0] = 0;
        blockPosition[1] = area;
        blockPosition[2] =  area + area;
        blockPosition[3] = area + area + area;
        blockPosition[4] = area + area + area + area;


        Block target = new Block(left, left + (area / 2),canvas.getHeight()-450, canvas.getHeight() - 500 - (blockPosition[1] / 2));
        Rect targetRect = new Rect(left, target.top, target.right, target.bottom);


        drawCharacters(canvas);



        if (animationTick % 30 == 0) {
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
//        canvas.drawRect(targetRect,randomTarg);

        checkIfBlockOnScreen(height);
        animationTick++;

        if (animationTick % 50 == 0) {
            velocity++;
        }
        count++;

    }

    private void drawCharacters(Canvas canvas) {
        if (animationTick % 15 == 0) {
            moving = true;
        }
        if (animationTick % 30 == 0) {
            moving = false;
        }

        if (moving) {
            Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character_left);
            canvas.drawBitmap(character, left, canvas.getHeight()-500, null);
        } else {
            Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character_right);
            canvas.drawBitmap(character, left, canvas.getHeight()-500, null);
        }

        if (lives > 1) {
            if (moving) {
                Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character2_right);
                canvas.drawBitmap(character, left2, canvas.getHeight()-400, null);
            } else {
                Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character2_left);
                canvas.drawBitmap(character, left2, canvas.getHeight()-400, null);
            }

        }
        if (lives > 2) {
            if (moving) {
                Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character3_left);
                canvas.drawBitmap(character, left3, canvas.getHeight()-300, null);
            } else {
                Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character3_right);
                canvas.drawBitmap(character, left3, canvas.getHeight()-300, null);
            }
        }
        if (lives > 3) {
            if (moving) {
                Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character4_left);
                canvas.drawBitmap(character, left4, canvas.getHeight()-200, null);
            } else {
                Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character4_right);
                canvas.drawBitmap(character, left4, canvas.getHeight()-200, null);
            }
        }
    }

    private void checkIfBlockOnScreen(int height) {
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).bottom > height) {

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
                    if (target.top - j == blocks.get(i).top) {
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
        float  pos = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (!hasStarted) {
                    mAnimationHelper.start();
                    hasStarted = true;
                }
                if (left  < pos) {
                    left += width/70 + velocity;
                    left2 += width/68 + velocity;
                    left3 += width/72 + velocity;
                    left4 += width/68 + velocity;
                } if (left  > pos) {
                    left -= width/70 + velocity;
                    left2 -= width/68 + velocity;
                    left3 -= width/72 + velocity;
                    left4 -= width/68 + velocity;
                }
                invalidate();
                break;
            }

        }
        return true;
    }
}
