package com.example.kosta.ordermadeandroid.activity.deal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.PurchaseHistory;
import com.example.kosta.ordermadeandroid.dto.Request;

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
 * Created by kosta on 2017-06-08.
 */

public class DealConsumerFragment extends Fragment{

    private List<PurchaseHistory> purchaseData;
    private PurchaseHistoryAdapter purchaseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_consumer, container, false);
        ListView listView = (ListView)view.findViewById(R.id.dealConsumer_list);

        final AsyncTask<String, Void, Void> task = new PurchaseHistoriesLoadingTask();
        // DealController - 145
        task.execute("http://10.0.2.2:8080/ordermade/deal/xml/searchPurchaseConsumerList.do");
        Log.d("a", "task done");

        purchaseData = new ArrayList<>();
        purchaseAdapter = new PurchaseHistoryAdapter(getActivity(), purchaseData);

        Log.d("a", "listView Done");

        listView.setAdapter(purchaseAdapter);

        return view;
    }

    private class PurchaseHistoriesLoadingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL((String)params[0]);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));
                NodeList nodeList = doc.getElementsByTagName("purchaseHistory");
                Log.d("a", "-------loadingTask start");
                for (int i = 0; i < nodeList.getLength(); i++){
                    PurchaseHistory purchaseHistory = new PurchaseHistory();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;

                    NodeList child = node.getChildNodes();

                    purchaseHistory.setId(getTagFindValue("id","purchaseHistory", element));
                    Log.d("a", "----ID Value----"+(getTagFindValue("id","purchaseHistory", element)));
                    purchaseHistory.setCharge(Integer.parseInt(getTagValue("charge", element)));
                    Log.d("a", "----Charge Value----"+(getTagValue("charge", element)));
                    purchaseHistory.setDeliveryStatus(getTagValue("deliveryStatus", element));
                    Log.d("a", "----deliveryStatus Value----"+(getTagValue("deliveryStatus", element)));
                    purchaseHistory.setInvoiceNumber(getTagValue("invoiceNumber", element));
                    Log.d("a", "----invoiceNumber Value----"+(getTagValue("invoiceNumber", element)));
                    //purchaseHistory.setPage(getTagValue("page", element));
                    //Log.d("a", "----deliveryStatus Value----"+(getTagValue("page", element)));
                    // page element가 없음
                    purchaseHistory.setPayment(getTagValue("payment", element));
                    Log.d("a", "----Payment Value----"+(getTagValue("payment", element)));

                    Member consumer = new Member();
                    consumer.setId(getTagFindValue("id", "consumer", element));
                    Log.d("a", "--consumer Id--"+(getTagFindValue("id", "consumer", element)));
                    consumer.setEmail(getTagFindValue("email", "consumer", element));
                    Log.d("a", "--consumer Email--"+(getTagFindValue("email", "consumer", element)));
                    consumer.setAddress(getTagFindValue("address", "consumer", element));
                    consumer.setName(getTagFindValue("name", "consumer", element));
                    consumer.setIntroduce(getTagFindValue("introduce", "consumer", element));
                    consumer.setImage(getTagFindValue("image", "consumer", element));
                    purchaseHistory.setConsumer(consumer);


                    Member maker = new Member();
                    maker.setId(getTagFindValue("id", "maker", element));
                    Log.d("a", "---maker Id---"+(getTagFindValue("id", "maker", element)));
                    maker.setEmail(getTagFindValue("email", "consumer", element));
                    Log.d("a", "---maker Email---"+(getTagFindValue("id", "maker", element)));
                    maker.setAddress(getTagFindValue("address", "maker", element));
                    Log.d("a", "---maker address---"+(getTagFindValue("address", "maker", element)));
                    maker.setName(getTagFindValue("name", "maker", element));
                    maker.setIntroduce(getTagFindValue("introduce", "maker", element));
                    maker.setImage(getTagFindValue("image", "maker", element));
                    purchaseHistory.setMaker(maker);

                    Request request = new Request();
                    request.setBound(getTagValue("bound", element));
                    request.setCategory(getTagValue("category", element));
                    request.setConsumer(consumer);
                    request.setContent(getTagValue("content", element));
                    request.setHopePrice(Integer.parseInt(getTagValue("hopePrice", element)));
                    request.setId(getTagFindValue("id", "request", element));
                    request.setMaker(maker);
                    request.setPrice(Integer.parseInt(getTagValue("price", element)));
                    Log.d("a", "--Request Price--"+(getTagValue("price", element)));
                    request.setTitle(getTagValue("title", element));
                    purchaseHistory.setRequest(request);
                    /*request.setPage(node.getChildNodes().item(9)
                            .getChildNodes().item(7).getFirstChild().getNodeValue());*/
                    //purchaseHistory.setOrderDate((getTagValue("orderDate", element)));

                    purchaseData.add(purchaseHistory);

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
            purchaseAdapter.notifyDataSetChanged();
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
