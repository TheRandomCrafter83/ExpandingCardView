package com.coderz.f1.expandingcardview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int expandedHeight = 116;

    RecyclerView recyclerView = null;
    Button button = null;

    private boolean expanded = false;
    private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        initRecycler();
        button = findViewById(R.id.button_expand);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                if (expanded) {
//                    ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
//                    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    recyclerView.setLayoutParams(lp);
                    expand(recyclerView);
                    button.setText("Show Less");
                } else {
//                    ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
//                    lp.height = pixelsToDp(expandedHeight);
//                    recyclerView.setLayoutParams(lp);
                    collapse(recyclerView);
                    button.setText("Show More");
                }
                recyclerView.invalidate();
            }
        });
    }

    private int pixelsToDp(int pixels) {
        int dp = (int) (pixels * getResources().getDisplayMetrics().density);
        return dp;
    }

    private void initRecycler() {
        bitmaps = getDumbyList(getApplicationContext());
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, bitmaps);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

    //Just some functions for testing and creating dumby data
    private ArrayList<Bitmap> getDumbyList(Context context) {
        ArrayList<Bitmap> ret = new ArrayList<Bitmap>();
        for (int i = 1; i < 16; i++) {
            Bitmap bmp = drawableToBitmap(getDrawable(context, "image" + Integer.toString(i)));
            ret.add(bmp);
        }
        return ret;
    }

    public static Drawable getDrawable(Context context, String name) {
        int resourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return AppCompatResources.getDrawable(context, resourceId);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
//-------------------------------------------------->

    public void collapse(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();
        //ValueAnimator valueAnimator = new ValueAnimator.ofInt(view.getMeasuredHeight(),pixelsToDp(expandedHeight));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(view.getMeasuredHeight(), pixelsToDp(expandedHeight))
                .setDuration(500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.height = animatedValue;
                view.setLayoutParams(lp);
                if (animatedValue == 0) {

                }
            }
        });

        valueAnimator.start();
    }

    public void expand(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();
        //ValueAnimator valueAnimator = new ValueAnimator.ofInt(view.getMeasuredHeight(),pixelsToDp(expandedHeight));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(pixelsToDp(expandedHeight),view.getMeasuredHeight())
                .setDuration(500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.height = animatedValue;
                view.setLayoutParams(lp);
                if (animatedValue == 0) {

                }
            }
        });

        valueAnimator.start();
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private ArrayList<Bitmap> images = new ArrayList<Bitmap>();

        public RecyclerAdapter(Context context, ArrayList<Bitmap> images) {
            this.images = images;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recycler_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
            holder.imageView.setImageBitmap(images.get(position));
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageview);
            }
        }
    }
}