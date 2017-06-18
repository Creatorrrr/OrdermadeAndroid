package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;

/**
 * Created by kosta on 2017-06-15.
 */

public class ProductImageUploadTest extends AppCompatActivity {

    private static int PICK_IMAGE_REQUEST=1;
    ImageView imageView;
    static final String TAG = "ProductImageUploadTest";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_register);
    }

    public void loadImageFromGallery(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        try {
            if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data){
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                int size = (int)(bitmap.getHeight()*(1024.0/bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap,1024,size,true);

                imageView=(ImageView)findViewById(R.id.productImg);
                imageView.setImageBitmap(scaled);
            } else  {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
       /* } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();*/
        } catch(Exception e){
            Toast.makeText(this, "이미지 로딩에 실패하였습니다.",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
