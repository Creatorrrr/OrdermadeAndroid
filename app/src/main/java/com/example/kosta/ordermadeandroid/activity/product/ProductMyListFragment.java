package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.example.kosta.ordermadeandroid.dto.loader.ProductLoader;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kosta on 2017-06-10.
 */

public class ProductMyListFragment extends Fragment{
    private List<Product> productList;
    private ProductMyListAdapter adapter;

    public static ProductMyListFragment instance;

    synchronized public static ProductMyListFragment getInstance() {
        if (instance == null) instance = new ProductMyListFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_my_list, container, false);

        productList = new ArrayList<>();
        adapter = new ProductMyListAdapter(getActivity(), productList);

        GridView gridView = (GridView)view.findViewById(R.id.product_my_list_listView);
        gridView.setAdapter(adapter);

        // 상품관리 출력
        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(Constants.mBaseUrl + "/product/ajax/products/makerid.do")
                .build()
                .execute(new ProductLoader(productList, adapter));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = productList.get(position);
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("productId", product.getId());
                intent.putExtra("productTitle",product.getTitle());
                intent.putExtra("productImage", product.getImage());
                intent.putExtra("productContent", product.getContent());
                intent.putExtra("makerImage", product.getMaker().getImage());
                intent.putExtra("makerId", product.getMaker().getId());
                intent.putExtra("makerIntroduce", product.getMaker().getIntroduce());
                startActivity(intent);
            }
        });

        // 상품관리 콘텍스트 메뉴
        registerForContextMenu(gridView);

        // 상품 등록 버튼
        view.findViewById(R.id.product_received_registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setTitle("상품 등록");
                getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductRegisterFragment.getInstance()).commit();
            }
        });

        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("수정");
        menu.add("삭제");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Product product = productList.get(((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position);

        switch(item.getTitle().toString()) {
            case "수정":
                Intent intent = new Intent();
                intent.putExtra("product", product);
                getActivity().setIntent(intent);
                getActivity().setTitle("상품 수정");
                getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductEditFragment.getInstance()).commit();
                break;
            case "삭제":
                OkHttpUtils.initClient(CustomApplication.getClient())
                        .get()
                        .url(Constants.mBaseUrl +"/product/xml/remove.do")
                        .addParams("id", product.getId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e, int id) {
                                Log.d("p", e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if(response.equals("true")) {
                                    Toast.makeText(getActivity(), "삭제 완료", Toast.LENGTH_SHORT).show();
                                    OkHttpUtils.initClient(CustomApplication.getClient())
                                            .get()
                                            .url(Constants.mBaseUrl + "/product/ajax/products/makerid.do")
                                            .build()
                                            .execute(new ProductLoader(productList, adapter));
                                } else {
                                    Toast.makeText(getActivity(), "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }

        return true;
    }

}
