package com.example.kosta.ordermadeandroid.dto.loader;

import android.util.Log;
import android.widget.BaseAdapter;

import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.example.kosta.ordermadeandroid.dto.Request;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;

/**
 * Created by kosta on 2017-06-16.
 */

public class ProductLoader extends DTOLoader {
    private List<Product> productList;
    private BaseAdapter adapter;
    private boolean append;

    public ProductLoader(List<Product> productList) {
        this.productList = productList;
        this.append = false;
    }

    public ProductLoader(List<Product> productList, BaseAdapter adapter) {
        this.productList = productList;
        this.adapter = adapter;
        this.append = false;
    }

    public ProductLoader(List<Product> productList, BaseAdapter adapter, boolean append) {
        this.productList = productList;
        this.adapter = adapter;
        this.append = append;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d("p", e.getMessage());
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));

            Log.d("p", "-------ProductLoader start");

            NodeList nodeList = doc.getElementsByTagName("product");

            if(!append) {
                productList.clear();
            }

            for (int i = 0; i < nodeList.getLength(); i++){
                productList.add(getProductFromElement((Element)nodeList.item(i)));
            }

            Log.d("p", "-------ProductLoader finished");
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
        if(adapter != null) adapter.notifyDataSetChanged();
    }

    private Product getProductFromElement(Element element) {
        Product product = new Product();

        product.setId(getTagValue("id", "product", element));
        product.setTitle(getTagValue("title", "product", element));
        product.setCategory(getTagValue("category", "product", element));
        product.setContent(getTagValue("content", "product", element));
        product.setImage(getTagValue("image", "product", element));
        product.setPeriod(Integer.parseInt(getTagValue("period", "product", element)));
        product.setPrice(Integer.parseInt(getTagValue("price", "product", element)));
        product.setHit(Integer.parseInt(getTagValue("hit", "product", element)));

        Member maker = new Member();
        maker.setId(getTagValue("id", "maker", element));
        maker.setEmail(getTagValue("email", "maker", element));
        maker.setAddress(getTagValue("address", "maker", element));
        maker.setName(getTagValue("name", "maker", element));
        maker.setIntroduce(getTagValue("introduce", "maker", element));
        maker.setImage(getTagValue("image", "maker", element));

        product.setMaker(maker);

        return product;
    }
}
