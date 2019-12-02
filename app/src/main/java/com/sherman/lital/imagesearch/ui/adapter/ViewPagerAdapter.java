package com.sherman.lital.imagesearch.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sherman.lital.imagesearch.model.Image;
import com.sherman.lital.imagesearch.ui.ImageFragmentItem;

import java.util.List;

import static com.sherman.lital.imagesearch.utils.Constants.IMAGE;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Image> imageList;

    public ViewPagerAdapter(FragmentManager fm, List<Image> list) {
        super(fm);
        this.imageList = list;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE, imageList.get(position).getWebformatURL());
        return ImageFragmentItem.newInstance(bundle);
    }

    @Override
    public int getCount() {
        return imageList == null ? 0 : imageList.size() ;
    }

}
