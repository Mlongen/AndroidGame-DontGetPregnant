package com.example.marcelolongen.snakeblock;

import android.annotation.SuppressLint;
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
    private ArrayList<Lives>  lives = new ArrayList<>();
    private int animationTick = 1;
    int[] blockPosition = new int[5];
    int velocity = 10;
    int left = 0;
    int left2 = 0;
    int left3 = 0;
    int left4 = 0;
    int area;
    int count = 0;
    int liveCount = 1;
    private int width = 0;
    private int height = 0;
    private boolean hasStarted = false;
    private boolean moving = false;
    private int showMessage = 0;

    private int skipBlocks = 0;
    private Bitmap pill = BitmapFactory.decodeResource(getResources(), R.drawable.pill);
    private Bitmap character1_left = BitmapFactory.decodeResource(getResources(), R.drawable.sperm_left);
    private Bitmap character1_right = BitmapFactory.decodeResource(getResources(), R.drawable.sperm_right);
    private Bitmap character2_left = BitmapFactory.decodeResource(getResources(), R.drawable.sperm_pro_left);
    private Bitmap character2_right = BitmapFactory.decodeResource(getResources(), R.drawable.sperm_pro_right);

    private Bitmap block = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
    private MotionEvent event;
    private Bitmap characterL;
    private Bitmap characterLPRO;
    private Bitmap characterR;
    private Bitmap characterRPRO;


    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap resizedPill = Bitmap.createScaledBitmap(pill, 80, 90, false);
        characterL = Bitmap.createScaledBitmap(character1_left, 70, 180, false);
        characterLPRO = Bitmap.createScaledBitmap(character2_left, 70, 180, false);
        characterR = Bitmap.createScaledBitmap(character1_right, 70, 180, false);
        characterRPRO = Bitmap.createScaledBitmap(character2_right, 70, 180, false);

        area = canvas.getWidth() / 5;
        height = canvas.getHeight();
        width = canvas.getWidth();
        @SuppressLint("DrawAllocation") Paint blocksColor = new Paint();
        @SuppressLint("DrawAllocation") Paint red = new Paint();
        @SuppressLint("DrawAllocation") Paint white = new Paint();
        @SuppressLint("DrawAllocation") Paint white2 = new Paint();

        blocksColor.setARGB(255, 66, 155, 244);
        blockPosition[0] = 0;
        blockPosition[1] = area;
        blockPosition[2] =  area + area;
        blockPosition[3] = area + area + area;
        blockPosition[4] = area + area + area + area;


        red.setARGB(255, 255, 0, 0);
        white.setARGB(255, 255, 255, 255);
        white2.setARGB(255, 255, 255, 255);
        red.setTextSize(60);
        white.setTextSize(60);
        white2.setTextSize(50);
        red.setTypeface(Typeface.MONOSPACE);
        white.setTypeface(Typeface.MONOSPACE);
        white2.setTypeface(Typeface.MONOSPACE);
        canvas.drawText("Score " + count , area * 2 , 70, white2);
        canvas.drawText("Speed " + velocity , area * 2, 160, white2);


        if (animationTick == 1) {
            canvas.drawText("TOUCH TO START" , area + 60, 300, white);
            showMessage--;
        }
        if (animationTick < 150) {
            canvas.drawText("AVOID THE EGGS" , area + 60, 500, white);
            showMessage--;
        }

        if (showMessage > 0 && liveCount == 2) {
            canvas.drawText("YOU GOT PROTECTION" , area , 300, white);
            showMessage--;
        } else if (showMessage > 0 && liveCount == 3) {
            canvas.drawText("EXTRA PROTECTION" , area + 50, 300, white);
            showMessage--;
        } else if (showMessage > 0 && liveCount == 1) {
            canvas.drawText("YOU ARE NOT PROTECTED" , area, 300, white);
            showMessage--;
        }



        Block target = new Block(left,
                left + (area / 3) + 20,canvas.getHeight()-450, canvas.getHeight() - 500 - (blockPosition[1] / 2));



        drawCharacters(canvas);

        if (skipBlocks > 0) {
            skipBlocks--;
        }





        if (velocity < 29) {
            if (animationTick % 40 == 0) {
                int random = (int) (Math.random() * 5);
                for (int i = 0; i < random; i ++) {
                    createBlock();
                }
            }
        } else {
            if (animationTick % 15 == 0) {
                int random = (int) (Math.random() * 5);
                for (int i = 0; i < random; i ++) {
                        createBlock();


                }
            }
        }


        drawBlocks(canvas, red, target);



        if (animationTick % 500 == 0 && liveCount < 3) {
            int liveLocation = (int) (Math.random() * width);
            Lives live = new Lives(liveLocation);
            lives.add(live);

        }

        for (int i = 0; i < lives.size();i++) {
            lives.get(i).y += velocity;
            canvas.drawBitmap(resizedPill, lives.get(i).x, lives.get(i).y, null);

        }

        Paint randomTarg = new Paint();
        randomTarg.setARGB(255, 255, 0, 0);


        checkIfBlockOnScreen(height);


        animationTick++;

        if (animationTick % 150 == 0 && velocity < 30) {
            velocity++;
        }
        count++;

        checkIfEat(target);

    }

    private void drawBlocks(Canvas canvas, Paint red, Block target) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).bottom += velocity;
            blocks.get(i).top += velocity;
            canvas.drawBitmap(block, null, blocks.get(i), red);
            checkIfHit(target);

        }
    }

    private void drawCharacters(Canvas canvas) {
        if (animationTick % 15 == 0) {
            moving = true;
        }
        if (animationTick % 30 == 0) {
            moving = false;
        }
        if (liveCount == 1 && moving) {
            canvas.drawBitmap(characterL, left, canvas.getHeight()-550, null);
            canvas.drawBitmap(characterL, left2 - 20, canvas.getHeight()-500, null);
            canvas.drawBitmap(characterL, left3 + 30, canvas.getHeight()-500, null);
        } else if (liveCount == 1 && !moving){
            canvas.drawBitmap(characterR, left, canvas.getHeight()-540, null);
            canvas.drawBitmap(characterR, left2 - 20, canvas.getHeight()-490, null);
            canvas.drawBitmap(characterR, left3 + 30, canvas.getHeight()-490, null);
        } else if (liveCount > 1 && moving) {
            canvas.drawBitmap(characterLPRO, left, canvas.getHeight()-550, null);
            canvas.drawBitmap(characterLPRO, left2 - 20, canvas.getHeight()-500, null);
            canvas.drawBitmap(characterLPRO, left3 + 30, canvas.getHeight()-500, null);
        } else if (liveCount > 1 && !moving) {
            canvas.drawBitmap(characterRPRO, left, canvas.getHeight()-540, null);
            canvas.drawBitmap(characterRPRO, left2 - 20, canvas.getHeight()-490, null);
            canvas.drawBitmap(characterRPRO, left3 + 30, canvas.getHeight()-490, null);
        }

    }

    private void checkIfBlockOnScreen(int height) {
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).bottom > height) {
                blocks.remove(blocks.get(i));
            }
        }
    }

    public void createBlock() {
        int firstBlock = (int) (Math.random() * 5);
        Block block = new Block(blockPosition[firstBlock], blockPosition[firstBlock] + area, area, 0);
        Rect rectangle = new Rect(block.left, block.top, block.right, block.bottom);

        blocks.add(rectangle);
    }


    public void checkIfHit(Block target) {
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).bottom > height / 2) {
                for (int j = 0; j < velocity;j++) {
                    if (target.top - j == blocks.get(i).top && skipBlocks == 0) {
                        if (blocks.get(i).left < target.left && target.left < blocks.get(i).right ||
                                blocks.get(i).left < target.right && target.right < blocks.get(i).right) {
                            if (liveCount > 1){
                                skipBlocks +=velocity;
                                liveCount--;
                                showMessage +=80;
                            } else {
                                Intent intent = new Intent(getRootView().getContext(), Finish.class);
                                intent.putExtra("score", count);
                                getContext().startActivity(intent);
                                break;
                            }
                        }
                    }
                }
            }
         }
    }
    public void checkIfEat(Block target) {
        for (int i = 0; i < lives.size();i++) {
            if (lives.get(i).x + lives.get(i).radius >= target.left &&
                    lives.get(i).x - lives.get(i).radius <= target.right &&
                    lives.get(i).y + lives.get(i).radius <= target.top &&
                    lives.get(i).y - lives.get(i).radius >= target.bottom) {
                System.out.println("yo");
                lives.remove(lives.get(i));

                liveCount++;
                showMessage += 300;
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.event = event;
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
                break;
            }

        }
        return true;
    }

}
