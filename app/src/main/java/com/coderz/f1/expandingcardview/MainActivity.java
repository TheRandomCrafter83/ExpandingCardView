package com.coderz.f1.expandingcardview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView = null;
    Button button = null;

    private boolean expanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        initRecycler();
        button = findViewById(R.id.button_expand);
        button.setOnClickListener(v -> {
            expanded = !expanded;
            if (expanded) {
                expand(recyclerView);
                button.setText(R.string.show_less);
            } else {
                collapse(recyclerView);
                button.setText(R.string.show_more);
            }
            recyclerView.invalidate();
        });
    }

    private int pixelsToDp() {
        return (int) (116 * getResources().getDisplayMetrics().density);
    }

    private void initRecycler() {
        ArrayList<Bitmap> bitmaps = getDummyList(getApplicationContext());
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(bitmaps);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

    //Just some functions for testing and creating dummy data
    private ArrayList<Bitmap> getDummyList(Context context) {
        ArrayList<Bitmap> ret = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            Bitmap bmp = drawableToBitmap(getDrawable(context, "image" + i));
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
        //ValueAnimator valueAnimator = new ValueAnimator.ofInt(view.getMeasuredHeight(),pixelsToDp(expandedHeight));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(view.getMeasuredHeight(), pixelsToDp())
                .setDuration(500);

        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = animatedValue;
            view.setLayoutParams(lp);
        });

        valueAnimator.start();
    }

    public void expand(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //ValueAnimator valueAnimator = new ValueAnimator.ofInt(view.getMeasuredHeight(),pixelsToDp(expandedHeight));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(pixelsToDp(),view.getMeasuredHeight())
                .setDuration(500);

        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = animatedValue;
            view.setLayoutParams(lp);
        });

        valueAnimator.start();
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private final ArrayList<Bitmap> images;

        public RecyclerAdapter(ArrayList<Bitmap> images) {
            this.images = images;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recycler_item, parent, false);
            return new ViewHolder(view);
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