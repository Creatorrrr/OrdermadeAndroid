package com.example.kosta.ordermadeandroid.activity.product;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.kosta.ordermadeandroid.R;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent intent = getIntent();
        final Product product = (Product) intent.getExtras().get("product");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);

        binding.setProduct(product);


        mToolbar = (Toolbar)findViewById(R.id.actionbar_productDetail);
        mToolbar.setTitle("상품 상세페이지");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Review rev0iew;

        Intent intent = getIntent();
        Product product = (Product) intent.getExtras().get("product");
        ((TextView) findViewById(R.id.productTitle)).setText(product.getTitle());
        ((TextView) findViewById(R.id.productContents)).setText(product.getContent());
        //   ((TextView) findViewById(R.id.reviews)).setText(product.getReviews().getId());

        ImageView img = new ImageView(this);
        img = (ImageView) findViewById(R.id.detailProductImg1);
        img = (ImageView) findViewById(R.id.detailProductImg2);
        img = (ImageView) findViewById(R.id.myProfileImg);

        new ImageLoadingTask(img).execute(product.getImage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //AsyncTask
    private class ImageLoadingTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewWeakReference;

        public ImageLoadingTask(ImageView img) {
            this.imageViewWeakReference = new WeakReference<ImageView>(img);
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return getRemoteImage(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewWeakReference != null) {
                ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private Bitmap getRemoteImage(final URL url) {
        Bitmap bitmap = null;
        URLConnection connection = null;

        try {
            connection = url.openConnection();
            connection.connect();

            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
