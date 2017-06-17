package com.example.kosta.ordermadeandroid.activity.product;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.util.ImageLoadingTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends Fragment {

    public ProductDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        Intent intent = getActivity().getIntent();
        String productTitle = (String)intent.getExtras().get("productTitle");
        String productImage = (String)intent.getExtras().get("productImage");
        String productContent = (String)intent.getExtras().get("productContent");
        String makerImage = (String)intent.getExtras().get("makerImage");
        Log.d("productDetail", "maker Image : "+makerImage);
        String makerId = (String)intent.getExtras().get("makerId");
        String makerIntroduce = (String)intent.getExtras().get("makerIntroduce");

        ((TextView)view.findViewById(R.id.product_detail_productTitle))
                .setText(productTitle);
        ((TextView)view.findViewById(R.id.product_detail_productContent))
                .setText(productContent);
        ImageView image = (ImageView)view.findViewById(R.id.product_detail_productImage);
        ((TextView)view.findViewById(R.id.product_detail_makerId))
                .setText(makerId);
        ((TextView)view.findViewById(R.id.product_detail_makerIntroduce))
                .setText(makerIntroduce);
        ImageView profileImage = (ImageView)view.findViewById(R.id.product_detail_profileImage);

        new ImageLoadingTask(image).execute(Constants.mBaseUrl
                +"/main/file/download.do?fileName="+productImage);

        new ImageLoadingTask(profileImage).execute(Constants.mBaseUrl
                +"/main/file/download.do?fileName="+makerImage);

        return view;
    }

}
