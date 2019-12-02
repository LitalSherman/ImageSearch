package com.sherman.lital.imagesearch.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherman.lital.imagesearch.R;
import com.sherman.lital.imagesearch.model.Image;
import com.sherman.lital.imagesearch.ui.adapter.ViewPagerAdapter;

import java.util.List;

import static com.sherman.lital.imagesearch.utils.Constants.EXTRA_RESULT;
import static com.sherman.lital.imagesearch.utils.Constants.POSITION;

public class ImageGalleryFragment extends Fragment {

    public static ImageGalleryFragment newInstance(Bundle bundle) {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.fragment_image_gallery, container, false);

        List<Image> imageList = (List<Image>) getArguments().getSerializable(EXTRA_RESULT);
        int position = getArguments().getInt(POSITION);

        ViewPager viewPager = view.findViewById(R.id.image_gallery_VP);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), imageList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        return view;
    }

}
