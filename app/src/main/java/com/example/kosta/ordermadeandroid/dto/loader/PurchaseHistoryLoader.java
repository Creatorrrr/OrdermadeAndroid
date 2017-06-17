package com.example.kosta.ordermadeandroid.dto.loader;

import android.util.Log;
import android.widget.BaseAdapter;

import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.PurchaseHistory;
import com.example.kosta.ordermadeandroid.dto.Request;
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
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;

/**
 * Created by Creator on 2017-06-17.
 */

public class PurchaseHistoryLoader extends DTOLoader {
    private List<PurchaseHistory> purchaseHistoryList;
    private BaseAdapter adapter;

    public PurchaseHistoryLoader(List<PurchaseHistory> purchaseHistoryList, BaseAdapter adapter) {
        this.purchaseHistoryList = purchaseHistoryList;
        this.adapter = adapter;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d("rs", e.getMessage());
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));

            Log.d("rs", "-------PurchaseHistoryLoader start");

            NodeList nodeList = doc.getElementsByTagName("purchaseHistory");

            for (int i = 0; i < nodeList.getLength(); i++){
                purchaseHistoryList.add(getPurchaseHistoryFromElement((Element)nodeList.item(i)));
            }

            Log.d("rs", "-------PurchaseHistoryLoader finished");
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

    private PurchaseHistory getPurchaseHistoryFromElement(Element element) {
        PurchaseHistory purchaseHistory = new PurchaseHistory();

        purchaseHistory.setId(getTagValue("id","purchaseHistory", element));
        purchaseHistory.setOrderDate(getTagValue("orderDate","purchaseHistory", element));
        purchaseHistory.setCharge(Integer.parseInt(getTagValue("charge", "purchaseHistory", element)));
        purchaseHistory.setDeliveryStatus(getTagValue("deliveryStatus", "purchaseHistory", element));
        purchaseHistory.setInvoiceNumber(getTagValue("invoiceNumber", "purchaseHistory", element));
        purchaseHistory.setPayment(getTagValue("payment", "purchaseHistory", element));

        Member consumer = new Member();
        consumer.setId(getTagValue("id", "consumer", "purchaseHistory", element));
        consumer.setEmail(getTagValue("email", "consumer", "purchaseHistory", element));
        consumer.setAddress(getTagValue("address", "consumer", "purchaseHistory", element));
        consumer.setName(getTagValue("name", "consumer", "purchaseHistory", element));
        consumer.setIntroduce(getTagValue("introduce", "consumer", "purchaseHistory", element));
        consumer.setImage(getTagValue("image", "consumer", "purchaseHistory", element));

        purchaseHistory.setConsumer(consumer);

        Member maker = new Member();
        maker.setId(getTagValue("id", "maker", "purchaseHistory", element));
        maker.setEmail(getTagValue("email", "consumer", "purchaseHistory", element));
        maker.setAddress(getTagValue("address", "maker", "purchaseHistory", element));
        maker.setName(getTagValue("name", "maker", "purchaseHistory", element));
        maker.setIntroduce(getTagValue("introduce", "maker", "purchaseHistory", element));
        maker.setImage(getTagValue("image", "maker", "purchaseHistory", element));

        purchaseHistory.setMaker(maker);

        Request request = new Request();

        request.setId(getTagValue("id", "request", "purchaseHistory", element));
        request.setTitle(getTagValue("title", "request", "purchaseHistory", element));

        Member requestMaker = new Member();
        requestMaker.setId(getTagValue("id", "maker", "request", element));
        requestMaker.setEmail(getTagValue("email", "maker", "request", element));
        requestMaker.setAddress(getTagValue("address", "maker", "request", element));
        requestMaker.setName(getTagValue("name", "maker", "request", element));
        requestMaker.setImage(getTagValue("image", "maker", "request", element));

        request.setMaker(maker);

        Member requestConsumer = new Member();
        requestConsumer.setId(getTagValue("id", "consumer", "request", element));
        requestConsumer.setEmail(getTagValue("email", "consumer", "request", element));
        requestConsumer.setAddress(getTagValue("address", "consumer", "request", element));
        requestConsumer.setName(getTagValue("name", "consumer", "request", element));
        requestConsumer.setImage(getTagValue("image", "consumer", "request", element));

        request.setConsumer(requestConsumer);
        request.setCategory(getTagValue("category", "request", "purchaseHistory", element));
        request.setContent(getTagValue("content", "request", "purchaseHistory", element));
        request.setHopePrice(Integer.parseInt(getTagValue("hopePrice", "request", "purchaseHistory", element)));
        request.setPrice(Integer.parseInt(getTagValue("price", "request", "purchaseHistory", element)));
        request.setBound(getTagValue("bound", "request", "purchaseHistory", element));
        request.setPayment(getTagValue("payment", "request", "purchaseHistory", element));

        purchaseHistory.setRequest(request);

        return purchaseHistory;
    }
}
