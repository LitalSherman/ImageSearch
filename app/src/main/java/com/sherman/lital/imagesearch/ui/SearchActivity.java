package com.sherman.lital.imagesearch.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sherman.lital.imagesearch.R;
import com.sherman.lital.imagesearch.model.Image;
import com.sherman.lital.imagesearch.viewModel.SearchViewModel;

import java.io.Serializable;
import java.util.List;

import static com.sherman.lital.imagesearch.utils.Constants.EXTRA_RESULT;
import static com.sherman.lital.imagesearch.utils.Constants.PAGE_NUMBER;
import static com.sherman.lital.imagesearch.utils.Constants.SCREEN_SIZE;
import static com.sherman.lital.imagesearch.utils.Constants.THEME;

public class SearchActivity extends AppCompatActivity {
    private SearchViewModel viewModel;
    private Observer<List<Image>> imageListObserver = null;
    private Observer <String> callFailedObserver = null;
    private EditText findImageET;
    private ProgressBar progressBar;
    private int screenSize;
    private int pageNumber =1;
    private String searchTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        findImageET = findViewById(R.id.find_image_ET);
        progressBar = findViewById(R.id.progress_bar);

        imageListObserver = new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                progressBar.setVisibility(View.GONE);
                if (images != null && images.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                    intent.putExtra(EXTRA_RESULT, (Serializable) images);
                    intent.putExtra(SCREEN_SIZE, getScreenSize());
                    intent.putExtra(THEME, getSearchTheme());
                    intent.putExtra(PAGE_NUMBER, pageNumber);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_api_result), Toast.LENGTH_SHORT).show();
                }
            }
        };

        callFailedObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Log.e(SearchActivity.class.getSimpleName(), errorMessage);
                Toast.makeText(getApplicationContext(), getString(R.string.api_failure), Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serch_button_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            String text = findImageET.getText().toString();
            if(!text.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                setScreenSize(getDisplayContentHeight());
                setSearchTheme(findImageET.getText().toString());
                viewModel.getImages(getSearchTheme(), pageNumber, getScreenSize())
                        .observe(this, imageListObserver);
                viewModel.onFailure().observe(this, callFailedObserver);

            }else {
                Toast.makeText(getApplicationContext(), getString(R.string.empty_text_search), Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public int getDisplayContentHeight() {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int screenHeight = 0, actionBarHeight = 0, statusBarHeight = 0;

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize }
        );
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        windowManager.getDefaultDisplay().getSize(size);
            screenHeight = size.y;
        return screenHeight - statusBarHeight - actionBarHeight;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }

    public String getSearchTheme() {
        return searchTheme;
    }

    public void setSearchTheme(String searchTheme) {
        this.searchTheme = searchTheme;
    }
}
