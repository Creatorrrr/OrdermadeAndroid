package com.example.kosta.ordermadeandroid.activity.main;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Product;

import org.w3c.dom.Document;
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
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private List<Product> productData;
    private MainProductAdapter mainProductAdapter;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView)view.findViewById(R.id.hitProduct_for_main_listView);

        final AsyncTask<String, Void, Void> task = new ProductForMainLoadingTask();
        task.execute("http://10.0.2.2:8080/ordermade/product/xml/main/category/hit.do?category=FUNITURE&page=10");
        Log.d("c", "--###-- MainProduct Task above me --##--");

        productData = new ArrayList<>();
        mainProductAdapter = new MainProductAdapter(getActivity(), productData);

        listView.setAdapter(mainProductAdapter);

        /*view.findViewById(R.id.main_productBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
            }
        });*/

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
                Log.d("c", "-------loadingTask start");

                for (int i = 0; i < nodeList.getLength(); i++){
                    Product product = new Product();
                    Node node = nodeList.item(i);



                    product.setCategory(node.getChildNodes()
                            .item(0).getFirstChild().getNodeValue());
                    product.setContent(node.getChildNodes()
                            .item(1).getFirstChild().getNodeValue());
                    product.setHit(Integer.parseInt(node.getChildNodes()
                            .item(2).getFirstChild().getNodeValue()));
                    product.setId(node.getChildNodes()
                            .item(3).getFirstChild().getNodeValue());
                    product.setImage(node.getChildNodes()
                            .item(4).getFirstChild().getNodeValue());

//                    Member maker = new Member();
//                    maker.setId(node.getChildNodes().item(5)
//                            .getChildNodes().item(0).getFirstChild().getNodeValue());
//                    maker.setEmail(node.getChildNodes().item(5)
//                            .getChildNodes().item(1).getFirstChild().getNodeValue());
//                    maker.setAddress(node.getChildNodes().item(5)
//                            .getChildNodes().item(2).getFirstChild().getNodeValue());
//                    maker.setName(node.getChildNodes().item(5)
//                            .getChildNodes().item(3).getFirstChild().getNodeValue());
//                    maker.setIntroduce(node.getChildNodes().item(5)
//                            .getChildNodes().item(4).getFirstChild().getNodeValue());
//                    maker.setImage(node.getChildNodes().item(5)
//                            .getChildNodes().item(5).getFirstChild().getNodeValue());
//                    product.setMaker(maker);
//
//                    product.setPeriod(Integer.parseInt(node.getChildNodes()
//                            .item(6).getFirstChild().getNodeValue()));
//                    product.setPrice(Integer.parseInt(node.getChildNodes()
//                            .item(7).getFirstChild().getNodeValue()));
//                    product.setTitle(node.getChildNodes()
//                            .item(8).getFirstChild().getNodeValue());

                    productData.add(product);
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
            mainProductAdapter.notifyDataSetChanged();
        }
    }

}
