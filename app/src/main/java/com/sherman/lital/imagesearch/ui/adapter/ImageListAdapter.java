package com.sherman.lital.imagesearch.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sherman.lital.imagesearch.R;
import com.sherman.lital.imagesearch.model.Image;
import com.sherman.lital.imagesearch.ui.ElementClickListener;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    private List<Image> images;
    private ElementClickListener clickListener;

    public ImageListAdapter(List<Image> imageList, ElementClickListener listener) {
        this.images = imageList;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListAdapter.ViewHolder holder, final int position) {
        Image image = images.get(position);
        ImageView imageView = holder.image;

        imageView.getLayoutParams().height = getDimensionInDp(image.getNewHeight(), imageView);
        imageView.getLayoutParams().width = getDimensionInDp(image.getNewWidth(), imageView);
        imageView.requestLayout();

        Glide.with(imageView.getContext())
                .load(image.getPreviewURL())
                .into(imageView);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.OnElementClick(position);
            }
        });
    }

    private int getDimensionInDp(int dimensionInPixel, View view) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, view.getResources().getDisplayMetrics());
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            view = itemView;
        }
    }
}
