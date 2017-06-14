package com.example.kosta.ordermadeandroid.activity.portfolio;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Portfolio;

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
 * Created by kosta on 2017-06-14.
 */

public class PortfolioMyListActivity extends AppCompatActivity {

    private List<Portfolio> portfolios;
    private PortfolioAdapter adapter;
    private Spinner searchBy;

 /*   private static final int CREATE = 0;
    private static final int EDIT = 1;
    private NoteDB db;*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_list);

        searchBy = (Spinner)findViewById(R.id.searchBy);
/*      ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(this,R.array.spinner_item,R.layout.support_simple_spinner_dropdown_item);
        adap.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        searchBy.setAdapter(adap);
        db = new NoteDB(this);
        db.open();
        fillData();
        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBy.getSelectedItem().toString().equals("제목")){
                    fillSearchedData("title",((EditText)findViewById(R.id.keywordForSearch)).getText().toString());
                }else if(searchBy.getSelectedItem().toString().equals("내용")){
                    fillSearchedData("content",((EditText)findViewById(R.id.keywordForSearch)).getText().toString());
                }
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PortfolioMyListActivity.this,PortfolioDetailActivity.class);
                startActivityForResult(intent,CREATE);
            }
        });
        registerForContextMenu(getListView());*/

        ListView list = (ListView)findViewById(R.id.list);
        Button button = (Button)findViewById(R.id.searchBtn);

        final PortfolioLoadingTask task = new PortfolioLoadingTask();
        task.execute("http://10.0.2.2:80" +
                "80/ordermade/portfolio/ui/mylist.do");

        portfolios = new ArrayList<>();
        adapter = new PortfolioAdapter(this, portfolios);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Portfolio portfolio = portfolios.get(position);
                Intent intent = new Intent(PortfolioMyListActivity.this, PortfolioDetailActivity.class);
                intent.putExtra("portfolio", portfolio);
                startActivity(intent);
            }
        });
    }

    private class PortfolioLoadingTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {

            try {
                URL url = new URL((String) params[0]);

                DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(url.openStream()));

                NodeList nodeList = document.getElementsByTagName("portfolio");

                for(int i = 0; i<nodeList.getLength(); i++){
                    Portfolio portfolio = new Portfolio();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;
     /*
	private List<Tag> tags;
	private Member maker;
	*/
                    Member maker = new Member();

                    portfolio.setId(getTagValue("id",element));
                    portfolio.setTitle(getTagValue("title",element));
                    portfolio.setCategory(getTagValue("category",element));
                    portfolio.setContent(getTagValue("content",element));
                    portfolio.setImage("http://10.0.2.2:8080/ordermade/resources/image/"+getTagValue("image",element));
                   // portfolio.setMaker(getTagValue("maker",element));

                    portfolios.add(portfolio);

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }   catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged(); //adapter갱신
        }
    }
    private static String getTagValue(String tag, Element element){
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node value = (Node) nodeList.item(0);
        return value.getNodeValue();
    }


/*    private void fillSearchedData(String searchBy, String keyword) {
        Cursor cursor = null;

        if(searchBy.equals("title")) {
            cursor = db.fetchNotesByTitle(keyword);
        } else if (searchBy.equals("content")) {
            cursor = db.fetchNotesByBody(keyword);
        }

        SimpleCursorAdapter adapter = (SimpleCursorAdapter)getListAdapter();

        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    private void fillData() {
        Cursor cursor = db.fetchAllNotes();

        String[] from = {NoteDB.KEY_TITLE};
        int[] to = {R.id.titleText};

        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(
                        this,
                        R.layout.activity_portfolio_listitem,
                        cursor,
                        from,
                        to,
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        this.setListAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();
    }*/
}
