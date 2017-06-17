package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.OkHttpClient;

/**
 * Created by kosta on 2017-06-10.
 */

public class ProductMyListFragment extends Fragment{

    private List<Product> products;
    private ProductMyListAdapter adapter;

    private OkHttpClient okHttpClient;
    private String productId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_my_list, container, false);
        GridView listView = (GridView)view.findViewById(R.id.product_my_list_listView);

        final AsyncTask<String, Void, Void> task = new ProductMyListLoadingTask();

        task.execute(Constants.mBaseUrl+"/product/ajax/products/makerid.do?makerId=m1");
        Log.d("productMyList", "ProductMyList Task Done");

        products = new ArrayList<>();
        adapter = new ProductMyListAdapter(getActivity(), products);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = products.get(position);
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
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
        registerForContextMenu(view.findViewById(R.id.product_my_list_listView));

        view.findViewById(R.id.product_my_list_registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductRegisterActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.product_my_list_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        //info.id, info.position 은 rowId
        /*switch (item.getItemId()) {
            case R.id.edit_item:
                Toast.makeText(getActivity(), "Edit selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_item:
                productId = ((TextView)info.targetView
                        .findViewById(R.id.myList_productId)).getText().toString();
                ProductMyListDeleteTask(productId);
                Toast.makeText(getActivity(), productId, Toast.LENGTH_SHORT).show();
                *//*Toast.makeText(getActivity()
                        , ((TextView)info.targetView.findViewById(R.id.myList_productId))
                                .getText().toString(), Toast.LENGTH_SHORT).show();*//*
                break;
        }*/

        return super.onContextItemSelected(item);
    }

    private void ProductMyListDeleteTask(String productId) {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache()
                , new SharedPrefsCookiePersistor(getActivity()));

        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        OkHttpUtils.initClient(okHttpClient)
                .get()
                .url(Constants.mBaseUrl +"/product/xml/remove.do")
                .addParams("id", productId)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Log.d("productDelete", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(response.equals("true")) {
                            Toast.makeText(getActivity(), "삭제 완료", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), ProductMyListActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private class ProductMyListLoadingTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL((String)params[0]);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));
                NodeList nodeList = doc.getElementsByTagName("product");
                Log.d("products", "--####--- ProductLoadingTask START --###---");
                for ( int i = 0 ; i < nodeList.getLength(); i++) {
                    Product product = new Product();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;
               /*     private String id;-
                    private String title;-
                    private Member maker;-
                    private String category;-
                    private String content;-
                    private String image;-
                    private int price;-
                    private int period;-
                    private int hit;-
                    private List<Review> reviews;*/
                    product.setId(getTagFindValue("id", "product", element));
                    product.setTitle(getTagValue("title", element));
                    product.setCategory(getTagValue("category", element));
                    product.setContent(getTagValue("content", element));
                    product.setPrice(Integer.parseInt(getTagValue("price", element)));
                    product.setPeriod(Integer.parseInt(getTagValue("period",element)));
                    product.setHit(Integer.parseInt(getTagValue("hit",element)));
                    product.setImage(getTagValue("image",element));
                    //product.setPage(element.getElementsByTagName("page")
                    //        .item(0).getChildNodes().item(0).getNodeValue());
                    Member maker = new Member();
                    maker.setId(getTagFindValue("id", "maker", element));
                    maker.setEmail(getTagFindValue("email", "maker", element));
                    maker.setAddress(getTagFindValue("address", "consumer", element));
                    maker.setName(getTagFindValue("name", "consumer", element));
                    maker.setIntroduce(getTagFindValue("introduce", "consumer", element));
                    maker.setImage(getTagFindValue("image", "consumer", element));
                    product.setMaker(maker);


                    Log.d("products", "product Id : "+getTagFindValue("id", "product", element));
                    Log.d("products", "product Title : "+getTagValue("title", element));
                    Log.d("products", "--####-- maker Start --####-- ");
                    Log.d("products", "----------"+(getTagFindValue("id", "maker", element)));
                    Log.d("products", "product category : "+getTagValue("category", element));
                    Log.d("products", "----------"+(getTagFindValue("email", "maker", element)));
                    products.add(product);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node value = (Node) nodeList.item(0);
        return value.getNodeValue();
    }
    private static String getTagFindValue(String tag, String className, Element element) {
        NodeList elementList = element.getElementsByTagName(tag);
        for ( int i = 0 ; i < elementList.getLength() ; i++){
            if ( className.equals(elementList.item(i).getParentNode().getNodeName())){
                return elementList.item(i).getChildNodes().item(0).getNodeValue();
            }
        }
        return null;
    }
}
