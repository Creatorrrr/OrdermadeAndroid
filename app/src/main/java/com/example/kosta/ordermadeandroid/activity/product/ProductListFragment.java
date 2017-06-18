package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.example.kosta.ordermadeandroid.dto.loader.ProductLoader;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.example.kosta.ordermadeandroid.util.URI2RealPath;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by kosta on 2017-06-16.
 */

public class ProductListFragment extends Fragment {
    private static final int PICK_FROM_ALBUM = 1;

    private List<Product> productList;
    private ProductListAdapter adapter;

    //private List<Category> categoryData;
    private Spinner categorySpinner;
    //private List<String> categoryList;
   // private ArrayAdapter<String> adapter;

    private static ProductListFragment instance;

    synchronized public static ProductListFragment newInstance() {
        if (instance == null) instance = new ProductListFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        GridView listView = (GridView) view.findViewById(R.id.product_list_listView);
        categorySpinner = (Spinner)view.findViewById(R.id.product_list_category);

        // Dynamic Spinner item loading success, but setOnItemSelected fail 17/06/18 2pm
        /*categoryData = new ArrayList<>();
        categoryList = new ArrayList<String>();
        CategoryLoadingTask(Constants.mBaseUrl+"/main/xml/categoryList.do");*/

        productList = new ArrayList<>();
        adapter = new ProductListAdapter(getActivity(), productList);

        listView.setAdapter(adapter);

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(Constants.mBaseUrl + "/product/ajax/products/category.do")
                .addParams("category", "ACCESSORY")
                .addParams("page", "1")
                .build()
                .execute(new ProductLoader(productList, adapter));

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == Constants.SELECT_ACCESSORY) {
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .get()
                            .url(Constants.mBaseUrl + "/product/ajax/products/category.do")
                            .addParams("category", "ACCESSORY")
                            .addParams("page", "1")
                            .build()
                            .execute(new ProductLoader(productList, adapter));
                }else if (position == Constants.SELECT_CLOTHING){
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .get()
                            .url(Constants.mBaseUrl + "/product/ajax/products/category.do")
                            .addParams("category", "CLOTHING")
                            .addParams("page", "1")
                            .build()
                            .execute(new ProductLoader(productList, adapter));
                }else if (position == Constants.SELECT_DIGITAL ){
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .get()
                            .url(Constants.mBaseUrl + "/product/ajax/products/category.do")
                            .addParams("category", "DIGITAL")
                            .addParams("page", "1")
                            .build()
                            .execute(new ProductLoader(productList, adapter));
                }else if (position == Constants.SELECT_FUNITURE) {
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .get()
                            .url(Constants.mBaseUrl + "/product/ajax/products/category.do")
                            .addParams("category", "FUNITURE")
                            .addParams("page", "1")
                            .build()
                            .execute(new ProductLoader(productList, adapter));
                }else if (position == Constants.SELECT_KITCHEN) {
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .get()
                            .url(Constants.mBaseUrl + "/product/ajax/products/category.do")
                            .addParams("category", "KITCHEN")
                            .addParams("page", "1")
                            .build()
                            .execute(new ProductLoader(productList, adapter));
                }else if (position == Constants.SELECT_SPORT) {
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .get()
                            .url(Constants.mBaseUrl + "/product/ajax/products/category.do")
                            .addParams("category", "SPORT")
                            .addParams("page", "1")
                            .build()
                            .execute(new ProductLoader(productList, adapter));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = productList.get(position);
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

        view.findViewById(R.id.image_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//sd카드에서 불러오기
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
            case PICK_FROM_ALBUM: //로컬에 있는 이미지 경로
                Log.d("p","-----------"+data.getData());//content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F57/ACTUAL/1625851485
                imageUpload(new URI2RealPath().getRealPathFromURI(getActivity().getApplication(),data.getData()));
                break;
        }
    }

//    private void CategoryLoadingTask(String...params) {
//        OkHttpUtils.initClient(CustomApplication.getClient())
//                .get()
//                .url(params[0])
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Log.d("category", e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        try {
//                            //xml-------
//                            StringReader sr = new StringReader(response);
//                            InputSource is = new InputSource(sr);
//                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                            DocumentBuilder builder = factory.newDocumentBuilder();
//                            Document doc = builder.parse(is);
//
//                            NodeList nodeList = doc.getElementsByTagName("category");
//                            Log.d("category", "--####--- categoryLoadingTask START --###---");
//                            for (int i = 0; i < nodeList.getLength(); i++) {
//                                Category category = new Category();
//                                Node node = nodeList.item(i);
//
//                                Element element = (Element) node;
//                                category.setType(getTagValue("type", element));
//                                Log.d("category", "-- category --"+getTagValue("type", element));
//
//                                categoryData.add(category);
//
//                                categoryList.add(categoryData.get(i).getType());
//
//                            }
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        } catch (SAXException e) {
//                            e.printStackTrace();
//                        } catch (ParserConfigurationException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                });
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoryList);
//                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//                        categorySpinner.setAdapter(adapter);
//                    }
//                });
//    }
//
//    private static String getTagValue(String tag, Element element) {
//        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
//        Node value = (Node) nodeList.item(0);
//        return value.getNodeValue();
//    }
//
//    private static String getTagFindValue(String tag, String className, Element element) {
//        NodeList elementList = element.getElementsByTagName(tag);
//        for ( int i = 0 ; i < elementList.getLength() ; i++){
//            if ( className.equals(elementList.item(i).getParentNode().getNodeName())){
//                return elementList.item(i).getChildNodes().item(0).getNodeValue();
//            }
//        }
//        return null;
//    }

    //이미지 업로드 및 경로 받기
    public void imageUpload(String imageSrc){
        File image = new File(imageSrc);

        OkHttpUtils.initClient(CustomApplication.getClient())
                .post()
                .url(Constants.mBaseUrl + "/main/file/upload.do")
                .addFile("upload", "product" + image.getName().substring(image.getName().lastIndexOf(".")), image)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("p", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        //이미지 받은후 Form데이터에 넣고 보내기.
                        if(response == "fail"){
                            Toast.makeText(getActivity().getApplication(), "이미지 업로드 실패 하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                        sendFormData(response);
                    }
                });

    }

    //상품등록 데이터에 업로된 이미지 파일명을 넣어 보낸다.
    public void sendFormData(String uploadFileName) {
        if (uploadFileName == "fail") uploadFileName = "";
        Log.d("p", uploadFileName);
        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(Constants.mBaseUrl + "/product/ajax/products/image.do")
                .addParams("image", uploadFileName)
                .build()
                .execute(new ProductLoader(productList, adapter));
    }
}
