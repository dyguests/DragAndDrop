package com.fanhl.draganddrop;

import android.animation.LayoutTransition;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private LinearLayout topLeft;
    private LinearLayout topRight;
    private LinearLayout bottomLeft;
    private LinearLayout bottomRight;

    private LayoutTransition mTransition;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        textView = new TextView(this);
//        textView.setText("DEMO");

        assginViews();
    }

    private void assginViews() {
        findViewById(R.id.myimage1).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.myimage2).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.myimage3).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.myimage4).setOnTouchListener(new MyTouchListener());

        topLeft = (LinearLayout) findViewById(R.id.topleft);
        topRight = (LinearLayout) findViewById(R.id.topright);
        bottomLeft = (LinearLayout) findViewById(R.id.bottomleft);
        bottomRight = (LinearLayout) findViewById(R.id.bottomright);

        topLeft.setOnDragListener(new MyDragListener());
        topRight.setOnDragListener(new MyDragListener());
        bottomLeft.setOnDragListener(new MyDragListener());
        bottomRight.setOnDragListener(new MyDragListener());

        mTransition = new MyLayoutTransition();


        topLeft.setLayoutTransition(mTransition);
        topRight.setLayoutTransition(mTransition);
        bottomLeft.setLayoutTransition(mTransition);
        bottomRight.setLayoutTransition(mTransition);
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
                    View view = (View) event.getLocalState();
                    ViewGroup srcContainer = (ViewGroup) view.getParent();

                    LinearLayout destContainer = (LinearLayout) v;
                    int addViewIndex = getAddViewIndex(destContainer, view, event.getX());

                    //FIXME 问题就在这里!!!! 因为有动画效果, 下面两段代码没法连着走
                    srcContainer.removeView(view);
                    destContainer.addView(view, addViewIndex);
                    view.setVisibility(View.VISIBLE);
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
        private int getAddViewIndex(LinearLayout container, View view, float x) {
            int childCount = container.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = container.getChildAt(i);
                float childX = child.getX() + child.getWidth() / 2;//FIXME 还要算上margin
                if (x <= childX) {
                    Log.d("横向布局用", "viewX:" + x + " <= childX:" + childX);
                    return i;
                }
            }

            return childCount;
        }
    }

    private class MyLayoutTransition extends LayoutTransition {
        {
            addTransitionListener(new LayoutTransition.TransitionListener() {
                @Override
                public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {

                }

                @Override
                public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                    //FIXME 这里怎么写啊,没法写啊!!!!真的没法写啊
                }
            });
        }
    }
}
