package com.sherman.lital.imagesearch.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sherman.lital.imagesearch.R;

import java.io.Serializable;
import java.util.List;

import static com.sherman.lital.imagesearch.utils.Constants.EXTRA_RESULT;
import static com.sherman.lital.imagesearch.utils.Constants.PAGE_NUMBER;
import static com.sherman.lital.imagesearch.utils.Constants.POSITION;
import static com.sherman.lital.imagesearch.utils.Constants.SCREEN_SIZE;
import static com.sherman.lital.imagesearch.utils.Constants.THEME;

public class SearchResultActivity extends AppCompatActivity {

    private Serializable serializableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Intent intent = this.getIntent();
            serializableList = intent.getSerializableExtra(EXTRA_RESULT);
            int screenSize = intent.getIntExtra(SCREEN_SIZE, 1);
            int pageNumber = intent.getIntExtra(PAGE_NUMBER, 1);
            String searchTheme = intent.getStringExtra(THEME);
            if (serializableList != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_RESULT, serializableList);
                bundle.putInt(SCREEN_SIZE, screenSize);
                bundle.putInt(PAGE_NUMBER, pageNumber);
                bundle.putString(THEME, searchTheme);
                addImageListFragment(bundle);
            }
        }else {
            serializableList = savedInstanceState.getSerializable(EXTRA_RESULT);
        }
    }

    public void addImageListFragment(Bundle bundle) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ImageListFragment.newInstance(bundle), ImageListFragment.class.getSimpleName())
                .addToBackStack(ImageListFragment.class.getSimpleName()).commit();
    }

    public void addImageGalleryFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putSerializable(EXTRA_RESULT, serializableList);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ImageGalleryFragment.newInstance(bundle), ImageGalleryFragment.class.getSimpleName())
                .addToBackStack(ImageGalleryFragment.class.getSimpleName()).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fr = getSupportFragmentManager();
                List<Fragment> fragments = fr.getFragments();
                if (fragments.size() > 1 && fragments.contains(fr.findFragmentByTag(ImageGalleryFragment.class.getSimpleName()))) {
                    fr.popBackStack();
                } else {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fr = getSupportFragmentManager();
        if (fr.getFragments().contains(fr.findFragmentByTag(ImageListFragment.class.getSimpleName()))) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(EXTRA_RESULT, serializableList);
    }
}
