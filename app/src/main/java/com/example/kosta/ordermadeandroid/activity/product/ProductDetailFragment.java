package com.example.kosta.ordermadeandroid.activity.product;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Review;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.example.kosta.ordermadeandroid.util.ImageLoadingTask;
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
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends Fragment {

    private List<Review> productReviewData;
    private ProductReviewListAdapter productReviewListAdapter;
    private ListView reviewListView;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        reviewListView = (ListView)view.findViewById(R.id.product_detail_review_listView);
        productReviewData = new ArrayList<>();

        reviewListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Intent intent = getActivity().getIntent();
        String productId = (String)intent.getExtras().get("productId");
        String productTitle = (String)intent.getExtras().get("productTitle");
        String productImage = (String)intent.getExtras().get("productImage");
        String productContent = (String)intent.getExtras().get("productContent");
        String makerImage = (String)intent.getExtras().get("makerImage");
        Log.d("productDetail", "maker Image : "+makerImage);
        String makerId = (String)intent.getExtras().get("makerId");
        Log.d("productDetail", "maker Image : "+makerId);
        String makerIntroduce = (String)intent.getExtras().get("makerIntroduce");
        Log.d("productDetail", "maker Image : "+makerIntroduce);

        ((TextView)view.findViewById(R.id.product_detail_productTitle))
                .setText(productTitle);
        ((TextView)view.findViewById(R.id.product_detail_productContent))
                .setText(productContent);
        ImageView image = (ImageView)view.findViewById(R.id.product_detail_productImage);
        ((TextView)view.findViewById(R.id.product_detail_makerId))
                .setText(makerId);
        ((TextView)view.findViewById(R.id.product_detail_makerIntroduce))
                .setText(makerIntroduce);
        ImageView profileImage = (ImageView)view.findViewById(R.id.product_detail_profileImage);

        // 상품이미지
        new ImageLoadingTask(image).execute(Constants.mBaseUrl
                +"/main/file/download.do?fileName="+productImage);

        // 제작자 이미지
        new ImageLoadingTask(profileImage).execute(Constants.mBaseUrl
                +"/main/file/download.do?fileName="+makerImage);

        // 후기 소비자 이미지
        ReviewLoadingTaks(Constants.mBaseUrl
                +"/product/ajax/reviews/productid.do?productId="+productId+"&page=1");

        return view;
    }

    private void ReviewLoadingTaks(String...params){

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(params[0])
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("productDetail", e.getMessage());
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

                            NodeList nodeList = doc.getElementsByTagName("review");
                            Log.d("productsDetail", "--####--- reviewLoadingTask START --###---");
                            for ( int i = 0 ; i < nodeList.getLength(); i++) {
                                Review review = new Review();
                                Node node = nodeList.item(i);

                                Element element = (Element) node;

                                review.setId(getTagFindValue("id","review",element));
                                review.setTitle(getTagFindValue("title","review",element));
                                review.setContent(getTagFindValue("content","review",element));
                                review.setGrade(Integer.parseInt(getTagFindValue("grade","review",element)));

                                Member consumer = new Member();
                                consumer.setId(getTagFindValue("id","consumer",element));
                                consumer.setEmail(getTagFindValue("email","consumer",element));
                                consumer.setAddress(getTagFindValue("address","consumer",element));
                                consumer.setName(getTagFindValue("name","consumer",element));
                                consumer.setIntroduce(getTagFindValue("introduce","consumer",element));
                                consumer.setImage(getTagFindValue("image","consumer",element));
                                review.setConsumer(consumer);

                                productReviewData.add(review);


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

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("productDetail", "product Review Task Done");
                                productReviewListAdapter = new ProductReviewListAdapter(getActivity(), productReviewData);
                                reviewListView.setAdapter(productReviewListAdapter);
                            }
                        });
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
