package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainFragment;
import com.example.kosta.ordermadeandroid.activity.product.ProductListAdapter;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Product;

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

/**
 * Created by kosta on 2017-06-16.
 */

public class ProductListFragment extends Fragment {
    private List<Product> products;
    private ProductListAdapter productListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        GridView listView = (GridView) view.findViewById(R.id.product_list_listView);

        final AsyncTask<String, Void, Void> task = new ProductForMainLoadingTask();
        task.execute(Constants.mBaseUrl+"/product/ajax/products/category.do?category=ACCESSORY&page=1");
        Log.d("productList", "--###-- MainProduct Task above me --##--");

        products = new ArrayList<>();
        productListAdapter = new ProductListAdapter(getActivity(), products);

        listView.setAdapter(productListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = products.get(position);
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

        return view;
    }

    private class ProductForMainLoadingTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL((String)params[0]);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));
                NodeList nodeList = doc.getElementsByTagName("product");
                Log.d("productList", "-------loadingTask start > ProductListFragment");

                for (int i = 0; i < nodeList.getLength(); i++){
                    Product product = new Product();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;
                    product.setCategory(getTagValue("category", element));
                    //Log.d("productList", "--###-- category --##--"+getTagValue("category", element));
                    product.setContent(getTagValue("content", element));
                    //Log.d("productList", "--###-- content --##--"+getTagValue("content", element));
                    product.setHit(Integer.parseInt(getTagValue("hit", element)));
                    //Log.d("productList", "--###-- hit --##--"+getTagValue("hit", element));
                    product.setId(getTagFindValue("id", "product", element));
                    //Log.d("productList", "--###-- id --##--"+getTagFindValue("id", "product", element));
                    product.setImage(getTagValue("image", element));
                    //Log.d("productList", "--###-- image --##--"+getTagValue("image", element));

                    Member maker = new Member();
                    maker.setId(getTagFindValue("id", "maker", element));
                    maker.setEmail(getTagFindValue("email", "maker", element));
                    maker.setAddress(getTagFindValue("address", "maker", element));
                    maker.setName(getTagFindValue("name", "maker", element));
                    maker.setIntroduce(getTagFindValue("introduce", "maker", element));
                    //Log.d("productList", "--###-- introduce --##--"+getTagFindValue("introduce", "maker", element));
                    maker.setImage(getTagFindValue("image", "maker", element));
                    //Log.d("productList", "--###-- introduce --##--"+getTagFindValue("image", "maker", element));
                    product.setMaker(maker);

                    product.setPeriod(Integer.parseInt(getTagValue("period", element)));
                    product.setPrice(Integer.parseInt(getTagValue("price", element)));
                    product.setTitle(getTagValue("title", element));

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
            productListAdapter.notifyDataSetChanged();
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
