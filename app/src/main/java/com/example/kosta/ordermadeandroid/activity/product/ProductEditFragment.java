package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.example.kosta.ordermadeandroid.util.ImageLoadingTask;
import com.example.kosta.ordermadeandroid.util.URI2RealPath;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

public class ProductEditFragment extends Fragment {
    private static final int PICK_FROM_ALBUM = 1;

    private EditText title;
    private ImageView image;
    private EditText content;
    private EditText price;
    private EditText period;
    private Spinner categorySpinner;
    private String imageSrc;

    private Product product;

    private static ProductEditFragment instance;

    synchronized public static ProductEditFragment newInstance() {
        if(instance == null) instance = new ProductEditFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_edit, container, false);

        title = (EditText)view.findViewById(R.id.product_edit_title);
        content = (EditText)view.findViewById(R.id.product_edit_content);
        price = (EditText)view.findViewById(R.id.product_edit_price);
        period = (EditText)view.findViewById(R.id.product_edit_period);
        image = (ImageView)view.findViewById(R.id.product_edit_image);
        categorySpinner = (Spinner)view.findViewById(R.id.product_edit_category);

        product = (Product)getActivity().getIntent().getExtras().get("product");
        //getActivity().getIntent().removeExtra("product");

        title.setText(product.getTitle());
        content.setText(product.getContent());
        price.setText(product.getPrice());
        period.setText(product.getPeriod() + "");
        new ImageLoadingTask(image).execute(Constants.mBaseUrl + "/main/file/download.do?fileName=" + product.getImage());
        switch (product.getCategory()) {
            case "ACCESSORY":
                categorySpinner.setSelection(Constants.SELECT_ACCESSORY);
                break;
            case "CLOTHING":
                categorySpinner.setSelection(Constants.SELECT_CLOTHING);
                break;
            case "DIGITAL":
                categorySpinner.setSelection(Constants.SELECT_DIGITAL);
                break;
            case "FUNITURE":
                categorySpinner.setSelection(Constants.SELECT_FUNITURE);
                break;
            case "KITCHEN":
                categorySpinner.setSelection(Constants.SELECT_KITCHEN);
                break;
            case "SPORT":
                categorySpinner.setSelection(Constants.SELECT_SPORT);
                break;
        }

        //이미지 클릭할때
        view.findViewById(R.id.product_edit_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//sd카드에서 불러오기
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });


        // 취소버튼
        view.findViewById(R.id.product_edit_cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductMyListFragment.newInstance()).commit();
            }
        });

        // 상품 수정 버튼
        view.findViewById(R.id.product_edit_modifyBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageSrc == null) {
                    sendFormData(product.getImage());
                } else {
                    imageUpload(imageSrc);
                }
            }
        });

        return view;
    }

    //이미지 업로드 및 경로 받기
    public void imageUpload(String imageSrc){
        File image = new File(imageSrc);

        OkHttpUtils.initClient(CustomApplication.getClient())
                .post()
                .url(Constants.mBaseUrl + "/main/file/upload.do")
                .addFile("upload", "product" + image.getName().substring(image.getName().lastIndexOf(".")), image)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("productRegister", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        Log.d("productRegister", response);
                        //이미지 받은후 Form데이터에 넣고 보내기.
                        if(response == "fail"){
                            Toast.makeText(getActivity().getApplication(), "이미지 업로드 실패 하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                        sendFormData(response);
                    }
                });

    }

    //상품등록 데이터에 업로된 이미지 파일명을 넣어 보낸다.
    public void sendFormData(String uploadFileName) {
        if (uploadFileName == "fail") uploadFileName = "";

        OkHttpUtils.initClient(CustomApplication.getClient())
                .post()
                .url(Constants.mBaseUrl + "/product/xml/modify.do")
                .addParams("id", product.getId())
                .addParams("title", title.getText().toString())
                .addParams("content", content.getText().toString())
                .addParams("image", uploadFileName)
                .addParams("price", price.getText().toString())
                .addParams("period", period.getText().toString())
                .addParams("category", categorySpinner.getSelectedItem().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("a", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        if (response.equals("true")) {
                            Toast.makeText(getActivity().getApplication(), "상품 수정 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplication(), "상품 수정 실패", Toast.LENGTH_SHORT).show();
                        }
                        getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductMyListFragment.newInstance()).commit();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_FROM_ALBUM: //로컬에 있는 이미지 경로
                Log.d("a","-----------"+data.getData());//content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F57/ACTUAL/1625851485
                image.setImageURI(data.getData());
                imageSrc =new URI2RealPath().getRealPathFromURI(getActivity().getApplication(),data.getData());
                Log.d("a","-----------"+imageSrc);
                break;
        }
    }
}
