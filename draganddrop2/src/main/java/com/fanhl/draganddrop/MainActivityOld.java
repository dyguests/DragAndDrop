package com.fanhl.draganddrop;

import android.animation.LayoutTransition;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class MainActivityOld extends ActionBarActivity {

    private LinearLayout topLeft;
    private LinearLayout topRight;
    private LinearLayout bottomLeft;
    private LinearLayout bottomRight;

    private LayoutTransition mTransition;
    private View myimage1;
    private View myimage2;
    private View myimage3;
    private View myimage4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assginViews();
    }

    private void assginViews() {
        myimage1 = findViewById(R.id.myimage1);
        myimage2 = findViewById(R.id.myimage2);
        myimage3 = findViewById(R.id.myimage3);
        myimage4 = findViewById(R.id.myimage4);

        myimage1.setOnTouchListener(new MyTouchListener());
        myimage2.setOnTouchListener(new MyTouchListener());
        myimage3.setOnTouchListener(new MyTouchListener());
        myimage4.setOnTouchListener(new MyTouchListener());

        topLeft = (LinearLayout) findViewById(R.id.topleft);
        topRight = (LinearLayout) findViewById(R.id.topright);
        bottomLeft = (LinearLayout) findViewById(R.id.bottomleft);
        bottomRight = (LinearLayout) findViewById(R.id.bottomright);

        topLeft.setOnDragListener(new MyDragListener());
        topRight.setOnDragListener(new MyDragListener());
        bottomLeft.setOnDragListener(new MyDragListener());
        bottomRight.setOnDragListener(new MyDragListener());

        mTransition = new LayoutTransition();

        mTransition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

            }
        });

        topLeft.setLayoutTransition(mTransition);
//        topRight.setLayoutTransition(mTransition);
//        bottomLeft.setLayoutTransition(mTransition);
//        bottomRight.setLayoutTransition(mTransition);
    }


    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }


    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();

                    owner.removeView(view);

                    LinearLayout container = (LinearLayout) v;

                    addViewAutoIndex(container, view, event.getX());

//                    view.setVisibility(View.VISIBLE);
                    Log.d("横向布局用", "event.getX():" + event.getX());
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }

        /**
         * 横向布局用
         *
         * @param container
         * @param view
         * @param x
         */
        private void addViewAutoIndex(LinearLayout container, View view, float x) {
            int childCount = container.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = container.getChildAt(i);
                float childX = child.getX() + child.getWidth() / 2;//FIXME 还要算上margin
                if (x <= childX) {
                    Log.d("横向布局用", "viewX:" + x + " <= childX:" + childX);
                    container.addView(view, i);
                    return;
                }
            }

            container.addView(view);
            view.setVisibility(View.VISIBLE);
        }
    }
}
