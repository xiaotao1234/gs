package com.huari.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarView extends CustomView {

    private int rectw;
    private int recth;
    String monthString = "10";
    private Paint titlePaint;
    private Paint gridPaint;
    int GridWidth;
    int GridHeight;
    int STATE_1 = 1;//无选中
    int STATE_2 = 2;//单个选中
    int STATE_3 = 3;//多个选中
    int state = 1;
    String[] topText = {"日", "一", "二", "三", "四", "五", "六"};
    private Point point;
    private Point pointTouch;
    private Paint dayBgPaint;
    private Paint gridSmallPaint;
    private Point pointTouch2;
    private int oldPosition;
    private Calendar calendar;
    private float downX;
    private float downY;
    float offsetXCanvas;
    private float offsetX;
    private float offsetY;
    private boolean scrollFlag = false;

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectw = w / 5 * 4;
        recth = h / 5 * 4;
        GridWidth = w / 7;
        GridHeight = h / 8;
        titlePaint.setTextSize(w/20);
        gridPaint.setTextSize(w/20);
        gridSmallPaint.setTextSize(w/25);
    }

    private void init() {
        pointTouch = new Point();
        pointTouch2 = new Point();

        titlePaint = new Paint();
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(50);
        titlePaint.setColor(Color.parseColor("#88DE47A6"));

        gridPaint = new Paint();
        gridPaint.setTextAlign(Paint.Align.CENTER);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(2);
        gridPaint.setTextSize(40);
        gridPaint.setColor(Color.parseColor("#2288CC"));

        gridSmallPaint = new Paint();
        gridSmallPaint.setTextAlign(Paint.Align.CENTER);
        gridSmallPaint.setAntiAlias(true);
        gridSmallPaint.setStrokeWidth(1);
        gridSmallPaint.setTextSize(30);
        gridSmallPaint.setColor(Color.WHITE);

        dayBgPaint = new Paint();
        dayBgPaint.setAntiAlias(true);
        dayBgPaint.setStyle(Paint.Style.FILL);
        dayBgPaint.setColor(Color.parseColor("#88DE47A6"));

        calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                float oldOffsetX;
//                float oldOffsetY;
                offsetX = event.getX()-downX;
                offsetY = event.getY()-downY;
                if(offsetX>(mViewWidth/35)||offsetX<(-mViewWidth/35)){
                    offsetXCanvas = offsetX;
                    scrollFlag = true;
                    invalidate();
                    state = STATE_1;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(scrollFlag == false){
                    if (state == STATE_1||state == STATE_3) {
                        if (event.getY() > GridHeight * 2) {
                            pointTouch.y = (int) (event.getY() / GridHeight);
                            pointTouch.x = (int) (event.getX() / GridWidth);
                            state = STATE_2;
                        }
                        invalidate();
                    } else if (state == STATE_2) {
                        if (event.getY() > GridHeight * 2) {
                            pointTouch2.y = (int) (event.getY() / GridHeight);
                            pointTouch2.x = (int) (event.getX() / GridWidth);
                            if (pointTouch2.x != pointTouch.x || pointTouch2.y != pointTouch.y) {
                                state = STATE_3;
                            } else {
                                state = STATE_1;
                            }
                        }
                        invalidate();
                    }
                }else{
                    scrollFlag = false;
                }
                Log.d("xiaoxiao","up");
                if(offsetX >(mViewWidth/5)){
                    calendar.add(Calendar.MONTH,-1);
                }else if(offsetX <(-mViewWidth/5)){
                    calendar.add(Calendar.MONTH,+1);
                }
                offsetXCanvas = 0;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                scrollFlag = false;
                if(offsetX >(mViewWidth/5)){
                    calendar.add(Calendar.MONTH,-1);
                }else if(offsetX <(-mViewWidth/5)){
                    calendar.add(Calendar.MONTH,+1);
                }
                offsetXCanvas = 0;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTitle(canvas);
        drawTop(canvas);
        drawCanlendar(canvas);
    }

    private void drawTitle(Canvas canvas) {
        canvas.translate(mViewWidth / 2, mViewHeight / 15);
        canvas.drawText(calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月", 0, 0, titlePaint);
    }

    private void drawTop(Canvas canvas) {
        canvas.translate(-mViewWidth / 2, mViewHeight / 8);
        for (int i = 0; i < 7; i++) {
            drawTextGrid(canvas, mViewWidth / 7 * i + mViewWidth / 16, 0, topText[i]);
        }
    }

    private void drawCanlendar(Canvas canvas) {
        canvas.translate(0+offsetXCanvas, mViewHeight / 16);
        drawOneMonth(canvas, calendar.get(Calendar.DAY_OF_WEEK), calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        Log.d("xiao", "本月第一天是：" + calendar.get(Calendar.DAY_OF_WEEK));
//        drawOneMonth(canvas,calendar.get(Calendar.DAY_OF_WEEK);
//        calendar.get(Calendar.DAY_OF_WEEK);
//        calendar.add(Calendar.DAY_OF_MONTH, +1);
//        Log.d("xiao","本月第一天是：" + calendar.get(Calendar.DAY_OF_WEEK));
//        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)-2,1);
//        Log.d("xiao","本月第一天是：" + calendar.get(Calendar.DAY_OF_WEEK));
    }

    private void drawOneMonth(Canvas canvas, int dayOfWeek, int number) {
        for (int i = 0; i < number; i++) {
            drawTextDay(canvas, dayOfWeek, number, i);
        }
    }

    private void drawTextDay(Canvas canvas, int dayOfWeek, int number, int position) {
        getPoint(dayOfWeek, position);
        int x = (dayOfWeek + position - 1) % 7;
        int y = (dayOfWeek + position - 1) / 7;
        if(scrollFlag==false){
            if (state == STATE_2) {
                if ((pointTouch.x) == x && (pointTouch.y - 2) == y) {
                    drawPointRect(canvas,dayOfWeek,position);
                    oldPosition = position;
                }
            } else if (state == STATE_3) {
                if ((pointTouch2.x) == x && (pointTouch2.y - 2) == y) {
                    for(int i=(oldPosition>position?position:oldPosition);i<=(oldPosition<position?position:oldPosition);i++){
                        drawPointRect(canvas,dayOfWeek,i);
                    }
                }
            }
        }
        getPoint(dayOfWeek, position);
        canvas.drawText(String.valueOf(position + 1), point.x + GridWidth / 2, point.y + GridHeight / 2, gridSmallPaint);
    }

    private void drawTextGrid(Canvas canvas, int x, int y, String s) {
        canvas.drawText(s, x, y, gridPaint);
    }

    private void drawPointRect(Canvas canvas,int dayOfWeek,int position){
        getPoint(dayOfWeek,position);
        Rect rect = new Rect(point.x,point.y,point.x+GridWidth,point.y+GridHeight);
        canvas.drawRect(rect,dayBgPaint);
    }

    private Point getPoint(int dayofWeek, int position) {
        point = new Point();
        int h = (position - 1 + dayofWeek) / 7;
        int w = (position - 1 + dayofWeek) % 7;
        point.x = w * GridWidth;
        point.y = h * GridHeight;
        return point;
    }
}
