package com.kali_corporation.healthfitnessplus.sevice.general;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kali_corporation.healthfitnessplus.R;
import com.kali_corporation.healthfitnessplus.activity.CalculaterActivity;
import com.kali_corporation.healthfitnessplus.sevice.utils.TypefaceManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.MyRestaurant_ViewHolder> implements Filterable {
    ArrayList<All_Home> FilteredList = new ArrayList<>();
    Activity activity;
    public ArrayList<All_Home> allVideo;
    ArrayList<All_Home> all_videohistoryArrayList;
    Context context;
    boolean isFilter = false;
    TypefaceManager typefaceManager;

    public class MyRestaurant_ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_home;
        RelativeLayout rl_home;
        TextView tv_title_home;

        public MyRestaurant_ViewHolder(View view) {
            super(view);
            this.tv_title_home = (TextView) view.findViewById(R.id.tv_title_home);
            this.image_home = (ImageView) view.findViewById(R.id.image_home);
            this.rl_home = (RelativeLayout) view.findViewById(R.id.rl_home);
            this.tv_title_home.setTypeface(Home_Adapter.this.typefaceManager.getLight());
        }
    }

    public Home_Adapter(Activity activity2, ArrayList<All_Home> arrayList, Context context2) {
        this.activity = activity2;
        this.context = context2;
        this.all_videohistoryArrayList = arrayList;
        this.allVideo = arrayList;
        this.typefaceManager = new TypefaceManager(context2.getAssets(), context2);
    }

    public MyRestaurant_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyRestaurant_ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_home, viewGroup, false));
    }

    public void onBindViewHolder(final MyRestaurant_ViewHolder myRestaurant_ViewHolder, int i) {
        try {
            myRestaurant_ViewHolder.tv_title_home.setText(((All_Home) this.all_videohistoryArrayList.get(i)).title);
//            GradientDrawable gradientDrawable = (GradientDrawable) myRestaurant_ViewHolder.rl_home.getBackground();
//            StringBuilder sb = new StringBuilder();
//            sb.append("#");
//            sb.append(((All_Home) this.all_videohistoryArrayList.get(i)).color);
//            gradientDrawable.setColor(Color.parseColor(sb.toString()));
            try {
                Resources resources = this.context.getResources();

                myRestaurant_ViewHolder.image_home.setImageDrawable(resources.getDrawable(resources.getIdentifier(this.all_videohistoryArrayList.get(i).image, "drawable", this.context.getPackageName())));

            } catch (Exception e) {
                e.printStackTrace();
            }
            myRestaurant_ViewHolder.rl_home.setTag(Integer.valueOf(i));
            myRestaurant_ViewHolder.rl_home.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    int pos = ((All_Home) Home_Adapter.this.all_videohistoryArrayList.get(((Integer) view.getTag()).intValue())).f2614id;
                   Intent intent=new Intent(context, CalculaterActivity.class);
                   intent.putExtra("pos",pos);
                   context.startActivity(intent);
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.all_videohistoryArrayList.size();
    }

    public Filter getFilter() {
        return new Filter() {

            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Home_Adapter.this.all_videohistoryArrayList = (ArrayList) filterResults.values;
                Home_Adapter.this.notifyDataSetChanged();
            }


            public FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.values = Home_Adapter.this.allVideo;
                    filterResults.count = Home_Adapter.this.allVideo.size();
                    Home_Adapter.this.isFilter = false;
                } else {
                    Home_Adapter.this.FilteredList.clear();
                    Home_Adapter.this.isFilter = true;
                    for (int i = 0; i < Home_Adapter.this.allVideo.size(); i++) {
                        All_Home all_Home = (All_Home) Home_Adapter.this.allVideo.get(i);
                        if (all_Home.title.toString().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("pos->");
                            sb.append(String.valueOf(i));
                            Log.d("pos->", sb.toString());
                            Home_Adapter.this.FilteredList.add(all_Home);
                        }
                    }
                    filterResults.values = Home_Adapter.this.FilteredList;
                    filterResults.count = Home_Adapter.this.FilteredList.size();
                }
                return filterResults;
            }
        };
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
