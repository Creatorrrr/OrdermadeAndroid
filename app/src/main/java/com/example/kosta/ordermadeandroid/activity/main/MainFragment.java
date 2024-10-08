package com.example.kosta.ordermadeandroid.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.member.MemberMyPageFragment;
import com.example.kosta.ordermadeandroid.activity.member.MemberMyPageMakerFragment;
import com.example.kosta.ordermadeandroid.activity.product.ProductDetailActivity;
import com.example.kosta.ordermadeandroid.activity.product.ProductListFragment;
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
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private List<Product> productData;
    private MainProductAdapter mainProductAdapter;

    private SharedPreferences prefs;
    private String memberType;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView listView = (GridView) view.findViewById(R.id.hitProduct_for_main_listView);

        prefs = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        memberType = prefs.getString("memberType","");//SharedPreferences에서 꺼낸다

        // 인기 상품 리스트
        final AsyncTask<String, Void, Void> task = new ProductForMainLoadingTask();
        task.execute(Constants.mBaseUrl+"/product/xml/main/category/hit.do?category=FUNITURE&page=10");
        // Log.d("c", "--###-- MainProduct Task above me --##--");

        productData = new ArrayList<>();
        mainProductAdapter = new MainProductAdapter(getActivity(), productData);

        listView.setAdapter(mainProductAdapter);

        // 상품 상세 정보
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = productData.get(position);
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

        // main product button 구현
        view.findViewById(R.id.main_productBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setTitle("상품 검색");
                getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductListFragment.getInstance()).commit();
            }
        });

        view.findViewById(R.id.main_myPageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(memberType.equals("C")) {
                    getActivity().setTitle("나의 프로필");
                    getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, new MemberMyPageFragment()).commit();
                }else if(memberType.equals("M")){
                    getActivity().setTitle("나의 프로필");
                    getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, new MemberMyPageMakerFragment()).commit();
                }
            }
        });
        return view;
    }


    private class ProductForMainLoadingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL((String) params[0]);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));
                NodeList nodeList = doc.getElementsByTagName("product");
                Log.d("c", "-------loadingTask start > MainFragment");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Product product = new Product();
                    Node node = nodeList.item(i);

                    Element element = (Element) node;
                    product.setCategory(getTagValue("category", element));
                    product.setContent(getTagValue("content", element));
                    product.setHit(Integer.parseInt(getTagValue("hit", element)));
                    product.setId(getTagFindValue("id", "product", element));
                    product.setImage(getTagValue("image", element));

                    Member maker = new Member();
                    maker.setId(getTagFindValue("id", "maker", element));
                    maker.setEmail(getTagFindValue("email", "maker", element));
                    // Log.d("b", getTagFindValue("email", "maker", element));
                    maker.setAddress(getTagFindValue("address", "maker", element));
                    maker.setName(getTagFindValue("name", "maker", element));
                    maker.setIntroduce(getTagFindValue("introduce", "maker", element));
                    maker.setImage(getTagFindValue("image", "maker", element));
                    product.setMaker(maker);

                    product.setPeriod(Integer.parseInt(getTagValue("period", element)));
                    product.setPrice(Integer.parseInt(getTagValue("price", element)));
                    product.setTitle(getTagValue("title", element));

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


    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node value = (Node) nodeList.item(0);
        return value.getNodeValue();
    }

    private static String getTagFindValue(String tag, String className, Element element) {
        NodeList elementList = element.getElementsByTagName(tag);
        for (int i = 0; i < elementList.getLength(); i++) {
            if (className.equals(elementList.item(i).getParentNode().getNodeName())) {
                return elementList.item(i).getChildNodes().item(0).getNodeValue();
            }
        }
        return null;
    }
}
