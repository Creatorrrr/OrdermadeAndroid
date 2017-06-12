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

                    purchaseHistory.setId(node.getChildNodes()
                            .item(3).getFirstChild().getNodeValue());
                    Log.d("a", "----------"+(
                            node.getChildNodes().item(3)
                                    .getFirstChild().getNodeValue()));
                    purchaseHistory.setCharge(Integer.parseInt(node.getChildNodes()
                            .item(0).getFirstChild().getNodeValue()));
                    Log.d("a", "----------"+(
                            node.getChildNodes().item(0)
                                    .getFirstChild().getNodeValue()));
                    purchaseHistory.setDeliveryStatus(node.getChildNodes()
                            .item(2).getFirstChild().getNodeValue());
                    Log.d("a", "----------"+(
                            node.getChildNodes().item(2)
                                    .getFirstChild().getNodeValue()));
                    purchaseHistory.setInvoiceNumber(node.getChildNodes()
                            .item(4).getFirstChild().getNodeValue());
                    purchaseHistory.setPage(node.getChildNodes()
                            .item(7).getFirstChild().getNodeValue());
                    purchaseHistory.setPayment(node.getChildNodes()
                            .item(8).getFirstChild().getNodeValue());
                    Log.d("a", "----------"+(
                            node.getChildNodes().item(8).getFirstChild().getNodeValue()));



                    Member consumer = new Member();
                    consumer.setId(node.getChildNodes().item(1)
                            .getChildNodes().item(0).getFirstChild().getNodeValue());
                    Log.d("a", "----------"+(node.getChildNodes().item(1)
                            .getChildNodes().item(0).getFirstChild().getNodeValue()));
                    consumer.setEmail(node.getChildNodes().item(1)
                            .getChildNodes().item(1).getFirstChild().getNodeValue());
                    Log.d("a", "----------"+(
                            element.getElementsByTagName("email")
                                    .item(0).getChildNodes().item(0).getNodeValue()));
                    consumer.setAddress(node.getChildNodes().item(1)
                            .getChildNodes().item(2).getFirstChild().getNodeValue());
                    consumer.setName(node.getChildNodes().item(1)
                            .getChildNodes().item(3).getFirstChild().getNodeValue());
                    consumer.setIntroduce(node.getChildNodes().item(1)
                            .getChildNodes().item(4).getFirstChild().getNodeValue());
                    consumer.setImage(node.getChildNodes().item(1)
                            .getChildNodes().item(5).getFirstChild().getNodeValue());
                    purchaseHistory.setConsumer(consumer);


                    Member maker = new Member();
                    maker.setId(element.getElementsByTagName("id")
                            .item(1).getChildNodes().item(0).getNodeValue());
                    maker.setEmail(element.getElementsByTagName("email")
                            .item(1).getChildNodes().item(0).getNodeValue());
                    Log.d("a", "----------"+(
                            element.getElementsByTagName("email")
                                    .item(1).getChildNodes().item(0).getNodeValue()));
                    maker.setAddress(element.getElementsByTagName("address")
                            .item(1).getChildNodes().item(0).getNodeValue());
                    Log.d("a", "----------"+(node.getChildNodes().item(5)
                            .getChildNodes().item(2).getFirstChild().getNodeValue()));
                    maker.setName(element.getElementsByTagName("name")
                            .item(1).getChildNodes().item(0).getNodeValue());
                    maker.setIntroduce(element.getElementsByTagName("introduce")
                            .item(1).getChildNodes().item(0).getNodeValue());
                    maker.setImage(element.getElementsByTagName("image")
                            .item(1).getChildNodes().item(0).getNodeValue());
                    purchaseHistory.setMaker(maker);

                    Request request = new Request();
                    request.setBound(node.getChildNodes().item(9)
                            .getChildNodes().item(0).getFirstChild().getNodeValue());
                    request.setCategory(node.getChildNodes().item(9)
                            .getChildNodes().item(1).getFirstChild().getNodeValue());
                    request.setConsumer(consumer);
                    request.setContent(node.getChildNodes().item(9)
                            .getChildNodes().item(3).getFirstChild().getNodeValue());
                    request.setHopePrice(Integer.parseInt(node.getChildNodes().item(9)
                            .getChildNodes().item(4).getFirstChild().getNodeValue()));
                    request.setId(node.getChildNodes().item(9)
                            .getChildNodes().item(5).getFirstChild().getNodeValue());
                    request.setMaker(maker);
                    request.setPrice(Integer.parseInt(node.getChildNodes().item(9)
                            .getChildNodes().item(7).getFirstChild().getNodeValue()));
                    Log.d("a", "----------"+(node.getChildNodes().item(9)
                            .getChildNodes().item(7).getFirstChild().getNodeValue()));
                    request.setTitle(node.getChildNodes().item(9)
                            .getChildNodes().item(8).getFirstChild().getNodeValue());
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
}
