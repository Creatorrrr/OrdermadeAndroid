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
import com.example.kosta.ordermadeandroid.activity.request.RequestMyListAdapter;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.PurchaseHistory;
import com.example.kosta.ordermadeandroid.dto.Request;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;

/**
 * Created by kosta on 2017-06-08.
 */

public class DealConsumerFragment extends Fragment{

    private List<PurchaseHistory> purchaseData;
    private PurchaseHistoryAdapter purchaseAdapter;

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_consumer, container, false);
        listView = (ListView)view.findViewById(R.id.dealConsumer_list);
        purchaseData = new ArrayList<>();

        // 구매 이력 출력
        PurchaseHistoriesLoadingTask(Constants.mBaseUrl+"/deal/xml/searchPurchaseConsumerList.do");
        Log.d("dealConsumer", "task done");

        return view;
    }

    private void PurchaseHistoriesLoadingTask (String...params) {

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(params[0])
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("dealConsumer", e.getMessage());
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
                            NodeList nodeList = doc.getElementsByTagName("purchaseHistory");
                            Log.d("a", "-------loadingTask start");
                            for (int i = 0; i < nodeList.getLength(); i++){
                                PurchaseHistory purchaseHistory = new PurchaseHistory();
                                Node node = nodeList.item(i);

                                Element element = (Element)node;

                                NodeList child = node.getChildNodes();

                                purchaseHistory.setId(getTagFindValue("id","purchaseHistory", element));
                                Log.d("dealConsumer", "----ID Value----"+(getTagFindValue("id","purchaseHistory", element)));
                                purchaseHistory.setCharge(Integer.parseInt(getTagValue("charge", element)));
                                Log.d("dealConsumer", "----Charge Value----"+(getTagValue("charge", element)));
                                purchaseHistory.setDeliveryStatus(getTagValue("deliveryStatus", element));
                                Log.d("dealConsumer", "----deliveryStatus Value----"+(getTagValue("deliveryStatus", element)));
                                if ( node.getNodeName().equals("invoiceNumber")){
                                    purchaseHistory.setInvoiceNumber(getTagValue("invoiceNumber", element));
                                    Log.d("dealConsumer", "----invoiceNumber Value----"+(getTagValue("invoiceNumber", element)));
                                }
                                //purchaseHistory.setPage(getTagValue("page", element));
                                //Log.d("a", "----deliveryStatus Value----"+(getTagValue("page", element)));
                                // page element가 없음
                                purchaseHistory.setPayment(getTagValue("payment", element));
                                Log.d("dealConsumer", "----Payment Value----"+(getTagValue("payment", element)));

                                Member consumer = new Member();
                                consumer.setId(getTagFindValue("id", "consumer", element));
                                Log.d("dealConsumer", "--consumer Id--"+(getTagFindValue("id", "consumer", element)));
                                consumer.setEmail(getTagFindValue("email", "consumer", element));
                                Log.d("dealConsumer", "--consumer Email--"+(getTagFindValue("email", "consumer", element)));
                                consumer.setAddress(getTagFindValue("address", "consumer", element));
                                consumer.setName(getTagFindValue("name", "consumer", element));
                                consumer.setIntroduce(getTagFindValue("introduce", "consumer", element));
                                consumer.setImage(getTagFindValue("image", "consumer", element));
                                purchaseHistory.setConsumer(consumer);


                                Member maker = new Member();
                                maker.setId(getTagFindValue("id", "maker", element));
                                Log.d("dealConsumer", "---maker Id---"+(getTagFindValue("id", "maker", element)));
                                maker.setEmail(getTagFindValue("email", "consumer", element));
                                Log.d("dealConsumer", "---maker Email---"+(getTagFindValue("id", "maker", element)));
                                maker.setAddress(getTagFindValue("address", "maker", element));
                                Log.d("dealConsumer", "---maker address---"+(getTagFindValue("address", "maker", element)));
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
                                Log.d("dealConsumer", "--Request Price--"+(getTagValue("price", element)));
                                request.setTitle(getTagValue("title", element));
                                purchaseHistory.setRequest(request);
                                /*request.setPage(node.getChildNodes().item(9)
                                .getChildNodes().item(7).getFirstChild().getNodeValue());*/
                                //purchaseHistory.setOrderDate((getTagValue("orderDate", element)));

                                purchaseData.add(purchaseHistory);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("dealConsumer", "purchase Task Done");
                                        purchaseAdapter = new PurchaseHistoryAdapter(getActivity(), purchaseData);
                                        listView.setAdapter(purchaseAdapter);
                                    }
                                });
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
