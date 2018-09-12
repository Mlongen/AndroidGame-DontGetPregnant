package com.example.marcelolongen.DontGetPregnant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainCanvas extends View {


    private ArrayList<Rect> blocks = new ArrayList<>();
    private ArrayList<Lives>  lives = new ArrayList<>();
    public static int animationTick = 1;
    int[] blockPosition = new int[5];
    int velocity = 10;
    int left = 0;

    int area;
    public int count = 0;
    private static int liveCount = 1;
    private int width = 0;
    private int height = 0;
    private boolean hasStarted = false;
    private boolean moving = false;
    private static int showMessage = 0;

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
    private static int helperScore = 0;
    private AnimationHelper mAnimationHelper = new AnimationHelper(this, 50);

    private AlertDialog alertDialog;


    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        area = canvas.getWidth() / 5;
        height = canvas.getHeight();
        width = canvas.getWidth();
        Bitmap resizedPill = Bitmap.createScaledBitmap(pill, area/3, height /22, false);
        characterL = Bitmap.createScaledBitmap(character1_left, area/3, height / 10, false);
        characterLPRO = Bitmap.createScaledBitmap(character2_left, area/3, height / 10, false);
        characterR = Bitmap.createScaledBitmap(character1_right, area/3, height / 10, false);
        characterRPRO = Bitmap.createScaledBitmap(character2_right, area/3, height / 10, false);
        helperScore = count;


        @SuppressLint("DrawAllocation") Paint blocksColor = new Paint();
        @SuppressLint("DrawAllocation") Paint red = new Paint();

        blocksColor.setARGB(255, 66, 155, 244);
        blockPosition[0] = 0;
        blockPosition[1] = area;
        blockPosition[2] =  area * 2;
        blockPosition[3] = area * 3;
        blockPosition[4] = area * 4;


        if (animationTick == 1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View dialogView = inflater.inflate(R.layout.tutorial_dialog, null);
            Button okHint = dialogView.findViewById(R.id.okHint);
            builder.setView(dialogView)
                    .setCancelable(false);
            okHint.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAnimationHelper.start();
                    alertDialog.dismiss();
                }
            });
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            alertDialog.getWindow().setLayout(canvas.getWidth() * 3/4, canvas.getHeight()/2);

            left = (width/2) - 20;
            showMessage--;
        }




        Block target = new Block(left,
                left + (area / 3) ,height - (int)(height / 5.5) + 30, height - (int)(height/ 6) - (blockPosition[1] / 2));

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
            Lives live = new Lives(liveLocation, width/37);
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
            canvas.drawBitmap(characterL, left, canvas.getHeight()- (int)(height / 5.5), null);

        } else if (liveCount == 1 && !moving){
            canvas.drawBitmap(characterR, left, canvas.getHeight() - (int)(height / 5.5) - 10, null);

        } else if (liveCount > 1 && moving) {
            canvas.drawBitmap(characterLPRO, left, canvas.getHeight()-(int)(height / 5.5), null);

        } else if (liveCount > 1 && !moving) {
            canvas.drawBitmap(characterRPRO, left, canvas.getHeight()-(int)(height / 5.5) - 10, null);

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
            if (blocks.get(i).bottom > height / 3) {
                for (int j = 0; j < velocity;j++) {
                    if (target.top + j == blocks.get(i).top && skipBlocks == 0) {
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
                lives.remove(lives.get(i));

                liveCount++;
                showMessage += 200;
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
                } if (left  > pos) {
                    left -= width/70 + velocity;
                }
                break;
            }

        }
        return true;
    }

    public static String getHelperScore() {
        String displayed = null;

        if (showMessage > 0 && liveCount == 2) {
            displayed = ("YOU GOT PROTECTION");
            showMessage--;
        } else if (showMessage > 0 && liveCount == 3) {
            displayed = ("EXTRA PROTECTION");
            showMessage--;
        } else if (showMessage > 0 && liveCount == 1) {
            displayed = ("YOU ARE NOT PROTECTED");
            showMessage--;
        } else {
            if (helperScore < 150) {
                displayed = ("Avoid the eggs!");
            } else {
                if (liveCount == 2) {
                    displayed = ("Protection lv: 1     Score: " + helperScore);
                } else if (liveCount == 3){
                    displayed = ("Protection lv: 2     Score: " + helperScore);
                } else {
                    displayed = ("Score: " + helperScore);
                }

            }

        }

        return displayed;
    }
}