package com.example.kosta.ordermadeandroid.activity.product;

import android.os.Bundle;
import android.view.View;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;

public class ProductListActivity extends MainActivity {

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       getLayoutInflater().inflate(R.layout.activity_product_list, relativeLayout);

        setTitle("상품페이지");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout_for_frame, new ProductListFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.getMenu().getItem(0).setChecked(true);
    }
    /*public void clickP(View v){

        finish();
    }*/


    /*private List<Product> products;
    private ProductListAdapter adapter;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //  View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        //   return view;
        Log.d("C","productListActivity##########################");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) view.findViewById(R.id.product_for_main_listView);

        final AsyncTask<String, Void, Void> task = new ProductListActivity.ProductForMainLoadingTask();
        task.execute(Constants.mBaseUrl+"/product/ui/search.do?category=FUNITURE");*//*//*product/ajax/product/productId.do*//*

        products = new ArrayList<>();
        adapter = new ProductListAdapter(this, products);

        listView.setAdapter(adapter);
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
                Log.d("c", "-------loadingTask start > ProductListActivity");
              *//*  for (int i = 0; i < nodeList.getLength(); i++) {*//*
                Product product = new Product();
                Node node = nodeList.item(0);

                Element element = (Element) node;
                product.setCategory(getTagValue("category", element));
                product.setContent(getTagValue("content", element));
                product.setHit(Integer.parseInt(getTagValue("hit", element)));
                product.setId(getTagFindValue("id", "product", element));
                product.setImage(getTagValue("image", element));

                Member maker = new Member();
                maker.setId(getTagFindValue("id", "maker", element));
                maker.setEmail(getTagFindValue("email", "maker", element));
                maker.setAddress(getTagFindValue("address", "maker", element));
                maker.setName(getTagFindValue("name", "maker", element));
                maker.setIntroduce(getTagFindValue("introduce", "maker", element));
                maker.setImage(getTagFindValue("image", "maker", element));
                product.setMaker(maker);

                product.setPeriod(Integer.parseInt(getTagValue("period", element)));
                product.setPrice(Integer.parseInt(getTagValue("price", element)));
                product.setTitle(getTagValue("title", element));

                products.add(product);
              *//*  }*//*

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
            adapter.notifyDataSetChanged();
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
    }*/
}
