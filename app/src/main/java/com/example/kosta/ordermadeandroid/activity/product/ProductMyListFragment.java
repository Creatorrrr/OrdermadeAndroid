package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;

/**
 * Created by kosta on 2017-06-10.
 */

public class ProductMyListFragment extends Fragment{
    private List<Product> productList;
    private ProductMyListAdapter adapter;

    public static ProductMyListFragment instance;

    synchronized public static ProductMyListFragment newInstance() {
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
        ProductMyListLoadingTask(Constants.mBaseUrl+"/product/ajax/products/makerid.do");
        Log.d("productMyList", "ProductMyList Task Done");

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
                getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductRegisterFragment.newInstance()).commit();
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
                getActivity().setTitle("상품 수정");
                Intent intent = new Intent();
                intent.putExtra("product", product);
                getActivity().setIntent(intent);
                getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductEditFragment.newInstance()).commit();
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
                                Log.d("productDelete", e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if(response.equals("true")) {
                                    Toast.makeText(getActivity(), "삭제 완료", Toast.LENGTH_SHORT).show();
                                    ProductMyListLoadingTask(Constants.mBaseUrl+"/product/ajax/products/makerid.do");
                                } else {
                                    Toast.makeText(getActivity(), "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }

        return true;
    }

    private void ProductMyListLoadingTask (String...params){

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(params[0])
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("productMyList", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            //xml-------
                            StringReader sr = new StringReader(response);
                            InputSource is = new InputSource(sr);
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            Document doc = builder.parse(is);

                            NodeList nodeList = doc.getElementsByTagName("product");
                            Log.d("products", "--####--- ProductLoadingTask START --###---");
                            productList.clear();
                            for ( int i = 0 ; i < nodeList.getLength(); i++) {
                                Product product = new Product();
                                Node node = nodeList.item(i);

                                Element element = (Element)node;
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
                                maker.setAddress(getTagFindValue("address", "maker", element));
                                maker.setName(getTagFindValue("name", "maker", element));
                                maker.setIntroduce(getTagFindValue("introduce", "maker", element));
                                maker.setImage(getTagFindValue("image", "maker", element));
                                product.setMaker(maker);


                                Log.d("products", "product Id : "+getTagFindValue("id", "product", element));
                                Log.d("products", "product Title : "+getTagValue("title", element));
                                Log.d("products", "--####-- maker Start --####-- ");
                                Log.d("products", "--makerId :--"+getTagFindValue("id", "maker", element));
                                Log.d("products", "maker image : "+getTagFindValue("image", "consumer", element));
                                Log.d("products", "--maker Introduce--"+(getTagFindValue("introduce", "maker", element)));
                                productList.add(product);

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
                    }

                    @Override
                    public void onAfter(int id) {
                        adapter.notifyDataSetChanged();
                    }
                });
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
