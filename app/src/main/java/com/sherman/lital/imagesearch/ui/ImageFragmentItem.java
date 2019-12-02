package com.sherman.lital.imagesearch.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sherman.lital.imagesearch.R;

import static com.sherman.lital.imagesearch.utils.Constants.IMAGE;

public class ImageFragmentItem extends Fragment{

    public static ImageFragmentItem newInstance(Bundle bundle) {
        ImageFragmentItem fragment = new ImageFragmentItem();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.image_item, container, false);

        String image = getArguments().getString(IMAGE);

        ImageView imageView = view.findViewById(R.id.image);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(getContext())
                .load(image)
                .into(imageView);
        return view;
    }

}
