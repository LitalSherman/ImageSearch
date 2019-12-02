package com.sherman.lital.imagesearch.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sherman.lital.imagesearch.client.GetImages;
import com.sherman.lital.imagesearch.client.RetrofitClientInstance;
import com.sherman.lital.imagesearch.model.Hit;
import com.sherman.lital.imagesearch.model.Image;
import com.sherman.lital.imagesearch.model.ImagesData;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sherman.lital.imagesearch.utils.Constants.RESULT_PET_PAGE;

public class SearchViewModel extends ViewModel {

    private static String key = "13692950-596c2932628dbe9b41d5629b4";
    private GetImages client = RetrofitClientInstance.getRetrofitInstance().create(GetImages.class);
    private MutableLiveData<List<Image>> imagesLiveData;
    private MutableLiveData<String> callFailedLiveData;
    private int imageFixedHeight;

    public LiveData<List<Image>> getImages(String theme, int pageNumber, int screenSize) {
        if (imagesLiveData == null) {
            imagesLiveData = new MutableLiveData<>();
        }
        imageFixedHeight = getImageFixedHeight(screenSize);
        loadImages(theme, pageNumber);
        return imagesLiveData;
    }

    public LiveData<String> onFailure() {
        if (callFailedLiveData == null) {
            callFailedLiveData = new MutableLiveData<>();
        }
        return callFailedLiveData;
    }

    private void loadImages(final String theme, int pageNumber) {
        Call<ImagesData> call = client.getImages(key, theme, pageNumber, RESULT_PET_PAGE);
        call.enqueue(new Callback<ImagesData>() {
            @Override
            public void onResponse(Call<ImagesData> call, Response<ImagesData> response) {
                ImagesData imagesData = response.body();
                if (response.body() != null) {
                    List<Image> images = new ArrayList<>();
                    for (Hit hit : imagesData.getHits()) {
                        int newWidth = getImageNewWidth(imageFixedHeight, hit.getImageHeight(), hit.getImageWidth());
                        Image image = new Image(hit.getPreviewURL(), hit.getWebformatURL(), imageFixedHeight, newWidth);
                        images.add(image);
                    }
                    imagesLiveData.setValue(images);
                }
            }

            @Override
            public void onFailure(Call<ImagesData> call, Throwable t) {
                callFailedLiveData.setValue(t.getMessage());
            }
        });
    }

    private int getImageNewWidth(int imageFixedHeight, int imgOrgHeight, int imgOrgWidth) {
        return Math.round(((float) imageFixedHeight / imgOrgHeight) * imgOrgWidth);
    }

    private int getImageFixedHeight(int screenSize) {
        return Math.round(screenSize / 10);
    }
}
