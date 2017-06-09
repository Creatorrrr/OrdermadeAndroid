package com.example.kosta.ordermadeandroid.activity.deal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
        task.execute("http://10.0.2.2:8080/ordermade/deal/xml/searchPurchaseConsumerList.do");
        Log.d("consumer", "task done");

        purchaseData = new ArrayList<>();
        purchaseAdapter = new PurchaseHistoryAdapter(getActivity(), purchaseData);

        listView.setAdapter(purchaseAdapter);
        Log.d("consumer", "listView Done");
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
                for (int i = 0; i < nodeList.getLength(); i++){
                    PurchaseHistory purchaseHistory = new PurchaseHistory();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;

                    Log.d("consumer", "---------"+String.valueOf(node.getChildNodes().item(1).getFirstChild().getNodeValue()));

                    purchaseHistory.setId((getTagValue("id", element)));
                    purchaseHistory.setCharge(Integer.parseInt(getTagValue("charge", element)));

                    Member consumer = new Member();
                    NodeList consumerNodeList =
                            ((Element) node).getElementsByTagName("consumer")
                                    .item(0).getChildNodes();
                    consumer.setId(consumerNodeList.item(0).getNodeValue());
                    consumer.setEmail(consumerNodeList.item(1).getNodeValue());
                    consumer.setAddress(consumerNodeList.item(2).getNodeValue());
                    consumer.setName(consumerNodeList.item(3).getNodeValue());
                    consumer.setIntroduce(consumerNodeList.item(4).getNodeValue());
                    consumer.setImage(consumerNodeList.item(5).getNodeValue());
                    purchaseHistory.setConsumer(consumer);


                    Member maker = new Member();
                    NodeList makerNodeList =
                            ((Element) node).getElementsByTagName("maker")
                                    .item(0).getChildNodes();
                    maker.setId(makerNodeList.item(0).getNodeValue());
                    maker.setEmail(makerNodeList.item(1).getNodeValue());
                    maker.setAddress(makerNodeList.item(2).getNodeValue());
                    maker.setName(makerNodeList.item(3).getNodeValue());
                    maker.setIntroduce(makerNodeList.item(4).getNodeValue());
                    maker.setImage(makerNodeList.item(5).getNodeValue());
                    purchaseHistory.setMaker(maker);

                    Request request = new Request();
                    NodeList requestNodeList =
                            ((Element) node).getElementsByTagName("request")
                                    .item(0).getChildNodes();
                    request.setBound(requestNodeList.item(0).getNodeValue());
                    request.setCategory(requestNodeList.item(1).getNodeValue());
                    request.setConsumer(consumer);
                    request.setContent(requestNodeList.item(3).getNodeValue());
                    request.setHopePrice(Integer.parseInt(requestNodeList.item(4).getNodeValue()));
                    request.setId(requestNodeList.item(5).getNodeValue());
                    request.setMaker(maker);
                    request.setPage(requestNodeList.item(7).getNodeValue());
                    request.setPrice(Integer.parseInt(requestNodeList.item(8).getNodeValue()));
                    request.setTitle(requestNodeList.item(9).getNodeValue());
                    purchaseHistory.setRequest(request);

                    purchaseHistory.setDeliveryStatus((getTagValue("deliveryStatus", element)));
                    purchaseHistory.setInvoiceNumber((getTagValue("invoiceNumber", element)));
                    purchaseHistory.setPage((getTagValue("page", element)));
                    purchaseHistory.setPayment((getTagValue("payment", element)));
                    //purchaseHistory.setOrderDate((getTagValue("orderDate", element)));

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
}
