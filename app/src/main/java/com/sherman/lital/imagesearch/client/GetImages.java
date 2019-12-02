package com.sherman.lital.imagesearch.client;

import com.sherman.lital.imagesearch.model.ImagesData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vlad on 9/21/2019.
 */

public interface GetImages {

    @GET("?")
    Call<ImagesData> getImages(@Query("key") String key, @Query("q") String term, @Query("page") int pageNumber, @Query("per_page") int resultPerPage);
}
