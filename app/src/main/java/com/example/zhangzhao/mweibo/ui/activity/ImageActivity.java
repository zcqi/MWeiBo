package com.example.zhangzhao.mweibo.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhangzhao.mweibo.R;
import com.example.zhangzhao.mweibo.model.Image;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {
    PhotoViewAttacher mAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        List<Image> images = (List<Image>) getIntent().getSerializableExtra("images");

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        ImagePagerAdapter adapter = new ImagePagerAdapter(images);
        viewPager.setAdapter(adapter);
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private List<Image> images;

        public ImagePagerAdapter(List<Image> images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = ImageActivity.this;

            PhotoView imageView = new PhotoView(context);
            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(context).load(images.get(position).getLarge_pic()).into(imageView);

            ((ViewPager) container).addView(imageView, 0);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
}