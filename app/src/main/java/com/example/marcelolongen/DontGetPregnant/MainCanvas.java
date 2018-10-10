package com.example.marcelolongen.DontGetPregnant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Objects;

import static android.graphics.Bitmap.createScaledBitmap;

public class MainCanvas extends View {


    private final ArrayList<Circle>  lives = new ArrayList<>();
    private final ArrayList<Circle> blocks = new ArrayList<>();
    private static int animationTick = 1;
    private final int[] blockPosition = new int[5];
    private int velocity = 10;
    private int left = 0;

    private int area;
    private int count = 0;
    private static int liveCount = 1;
    private int width = 0;
    private int height = 0;
    private boolean hasStarted = false;
    private boolean moving = false;
    private static int showMessage = 0;

    private int skipBlocks = 0;
    private final Bitmap block = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
    private final Bitmap pill = BitmapFactory.decodeResource(getResources(), R.drawable.pill);
    private final Bitmap character1_left = BitmapFactory.decodeResource(getResources(), R.drawable.sperm_left);
    private final Bitmap character1_right = BitmapFactory.decodeResource(getResources(), R.drawable.sperm_right);
    private final Bitmap character2_left = BitmapFactory.decodeResource(getResources(), R.drawable.sperm_pro_left);
    private final Bitmap character2_right = BitmapFactory.decodeResource(getResources(), R.drawable.sperm_pro_right);

    private Bitmap resizedBlock;
    private Bitmap resizedPill;
    private Bitmap characterL;
    private Bitmap characterLPRO;
    private Bitmap characterR;
    private Bitmap characterRPRO;
    private static int helperScore = 0;
    private final AnimationHelper mAnimationHelper = new AnimationHelper(this, 100);

    private AlertDialog alertDialog;


    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        area = getWidth() / 5;
        height = getHeight();
        width = getWidth();

        resizeSprites();

        blockPosition[0] = 0;
        blockPosition[1] = area;
        blockPosition[2] =  area * 2;
        blockPosition[3] = area * 3;
        blockPosition[4] = area * 4;

        helperScore = count;

        showDialog();

        left = (width/2) - 20;
        @SuppressLint("DrawAllocation") Block target = new Block(left,
                left + (area / 3) ,height - (int)(height / 5.5) + 30, height - height/ 6 - (blockPosition[1] / 2));


        //create elements
        createBlocks();
        createPills();

        // draw elements
        drawCharacters(canvas);
        drawPills(canvas, resizedPill, resizedBlock);

        // run checks
        incrementersAndDecrementers();
        checkIfElementOnScreen();
        checkIfHit();
        checkIfEat(target);

    }

    private void showDialog() {
        if (animationTick == 1) {
            @SuppressLint("DrawAllocation") AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.tutorial_dialog, null);
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
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            alertDialog.getWindow().setLayout(getWidth() * 3/4, getHeight()/2);



        }
    }

    private void incrementersAndDecrementers() {
        showMessage--;
        if (skipBlocks > 0) {
            skipBlocks--;
        }
        if (animationTick % 150 == 0 && velocity < 30) {
            velocity++;
        }
        animationTick++;
        count++;
    }

    private void resizeSprites() {
        resizedBlock = createScaledBitmap(block, area, area, false);
        resizedPill = createScaledBitmap(pill, area/3, height /22, false);
        characterL = createScaledBitmap(character1_left, area/3, height / 10, false);
        characterLPRO = createScaledBitmap(character2_left, area/3, height / 10, false);
        characterR = createScaledBitmap(character1_right, area/3, height / 10, false);
        characterRPRO = createScaledBitmap(character2_right, area/3, height / 10, false);
    }

    private void createBlocks() {
        if (velocity < 29) {
            if (animationTick % 40 == 0) {
                int random = (int) (Math.random() * 5);
                for (int i = 0; i < random; i ++) {
                    createBlockNew();
                }
            }
        } else {
            if (animationTick % 15 == 0) {
                int random = (int) (Math.random() * 5);
                for (int i = 0; i < random; i ++) {
                    createBlockNew();

                }
            }
        }
    }

    private void createPills() {
        if (animationTick % 500 == 0 && liveCount < 3) {
            int liveLocation = (int) (Math.random() * width);
            Circle live = new Circle(liveLocation, width/37);
            lives.add(live);

        }
    }

    private void drawPills(Canvas canvas, Bitmap resizedPill, Bitmap resizedBlock) {
        for (int i = 0; i < lives.size();i++) {
            lives.get(i).y += velocity;
            canvas.drawBitmap(resizedPill, lives.get(i).x, lives.get(i).y, null);

        }
        @SuppressLint("DrawAllocation") Paint red = new Paint();
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).y += velocity;
            canvas.drawCircle(blocks.get(i).x, blocks.get(i).y, blocks.get(i).radius, red);
            canvas.drawBitmap(resizedBlock, blocks.get(i).x - area /2, blocks.get(i).y - area/ 2,null);

            }
    }


    private void drawCharacters(Canvas canvas) {
        if (animationTick % 10 == 0) {
            moving = true;
        }
        if (animationTick % 20 == 0) {
            moving = false;
        }
        if (liveCount == 1 && moving) {
            canvas.drawBitmap(characterL, left, (float) (height - height / 5.5), null);

        } else if (liveCount == 1){
            canvas.drawBitmap(characterR, left, (float) (height - height / 5.5 - 10), null);

        } else if (liveCount > 1 && moving) {
            canvas.drawBitmap(characterLPRO, left, (float) (height - height / 5.5), null);

        } else if (liveCount > 1) {
            canvas.drawBitmap(characterRPRO, left, (float) (height - height / 5.5 - 10), null);

        }


    }

    private void checkIfElementOnScreen() {
        for (int i = 0; i < blocks.size(); i++){
            if (blocks.get(i).y > height) {
                blocks.remove(blocks.get(i));
            }
        }
        for (int i = 0; i < lives.size(); i++){
            if (lives.get(i).y > height) {
                lives.remove(lives.get(i));
            }
        }
    }

    private void createBlockNew() {
        int firstBlock = (int) (Math.random() * 5);
        Circle circle = new Circle(blockPosition[firstBlock] + area / 2, area / 2);
        blocks.add(circle);
    }



    private void checkIfHit() {
        float spermTip = (float) (height - height / 7);
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).x + blocks.get(i).radius >= left + 10 &&
                    blocks.get(i).x - blocks.get(i).radius<= left + (area/3) &&
                    blocks.get(i).y - blocks.get(i).radius <= spermTip &&
                    blocks.get(i).y + blocks.get(i).radius >= spermTip && skipBlocks == 0) {

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
    private void checkIfEat(Block target) {
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
        String displayed;

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