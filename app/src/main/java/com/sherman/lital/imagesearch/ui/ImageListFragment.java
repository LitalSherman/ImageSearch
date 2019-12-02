package com.sherman.lital.imagesearch.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sherman.lital.imagesearch.R;
import com.sherman.lital.imagesearch.model.Image;
import com.sherman.lital.imagesearch.ui.adapter.ImageListAdapter;
import com.sherman.lital.imagesearch.viewModel.SearchViewModel;

import java.util.List;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static com.sherman.lital.imagesearch.utils.Constants.EXTRA_RESULT;
import static com.sherman.lital.imagesearch.utils.Constants.PAGE_NUMBER;
import static com.sherman.lital.imagesearch.utils.Constants.SCREEN_SIZE;
import static com.sherman.lital.imagesearch.utils.Constants.THEME;

public class ImageListFragment extends Fragment implements ElementClickListener {

    private boolean isLoading = false;
    private int screenSize;
    private int pageNumber;
    private String searchTheme;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private SearchViewModel viewModel;
    private Observer<List<Image>> imageListObserver = null;
    private Observer<String> callFailedObserver = null;

    RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            //On first run no need to run fun becouse we get the first image page from search activity
            if (!isLoading && pageNumber > 1) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    viewModel.getImages(searchTheme, pageNumber, screenSize)
                            .observe(getActivity(), imageListObserver);
                    viewModel.onFailure().observe(getActivity(), callFailedObserver);
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            // after skipping first run need to increase pageNumber for next run
            if (pageNumber == 1) pageNumber++;
        }
    };

    public static ImageListFragment newInstance(Bundle bundle) {

        ImageListFragment fragment = new ImageListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_image_list, container, false);

        final List<Image> imageList = (List<Image>) getArguments().getSerializable(EXTRA_RESULT);
        screenSize = getArguments().getInt(SCREEN_SIZE, 1);
        searchTheme = getArguments().getString(THEME);
        pageNumber = getArguments().getInt(PAGE_NUMBER, 1);

        progressBar = view.findViewById(R.id.list_progress_bar);
        recyclerView = view.findViewById(R.id.image_list_RV);

        final ImageListAdapter adapter = new ImageListAdapter(imageList, this);
        layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);


        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        imageListObserver = new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                isLoading = false;
                pageNumber++;
                progressBar.setVisibility(View.GONE);
                if (images != null && images.size() > 0) {
                    imageList.addAll(images);
                    adapter.notifyItemInserted(imageList.size() - 1);
                }
            }
        };

        callFailedObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String errorMessage) {
                progressBar.setVisibility(View.GONE);
                isLoading = false;
                Log.e(SearchActivity.class.getSimpleName(), errorMessage);
                Toast.makeText(getActivity(), getString(R.string.api_failure), Toast.LENGTH_SHORT).show();
            }
        };

        return view;
    }

    @Override
    public void OnElementClick(int position) {
        ((SearchResultActivity) getActivity()).addImageGalleryFragment(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.removeOnScrollListener(recyclerViewOnScrollListener);
    }
}
