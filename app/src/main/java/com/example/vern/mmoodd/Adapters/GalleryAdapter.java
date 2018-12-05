package com.example.vern.mmoodd.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.vern.mmoodd.R;

import java.util.List;

public class GalleryAdapter extends BaseAdapter{

    private List<Uri> imageList;
    private Context context;
    private View gridView;

    public GalleryAdapter(List<Uri> img, Context mContext)
    {
        this.imageList = img;
        this.context = mContext;
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        final Uri image = imageList.get(position);
        gridView = new View (context);

        gridView = inflater.inflate(R.layout.single_image, null);

        ImageView imageView = gridView.findViewById(R.id.imageGrid);
//        Bitmap bitmap;

//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image);
//            imageView.setImageBitmap(bitmap);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        imageView.setImageURI(image);
        return gridView;
    }



}
