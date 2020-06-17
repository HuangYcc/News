package com.example.ts.news.Fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ts.news.Adapter.CategoryAdapter;
import com.example.ts.news.Adapter.NewsAdapter;
import com.example.ts.news.Bean.News;
import com.example.ts.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ts on 18-8-20.
 */

public class MainFragment_bak extends Fragment implements NewsAdapter.CallBack {
    private static final String TAG = "MainFragment_bak";

    private View view;

    private List<String> categoryList = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ListView listView;
    private CategoryAdapter categoryAdapter;

    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_bak, container, false);
        initCategory();
        initNews();
        initView();

        newsAdapter = new NewsAdapter(getContext(), R.layout.news_item, newsList, this);
        listView = view.findViewById(R.id.list_view);
        listView.setAdapter(newsAdapter);

        return view;
    }

    private void initNews() {
        for (int i = 0; i < 30; i++) {
//            News news = new News(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "这是新闻" + (i + 1) + "的标题");
//            newsList.add(news);
        }
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void initCategory() {
        String[] categories = {
                "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"
        };
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            categoryList.add(category);
        }
    }

    @Override
    public void click(View view) {
        Toast.makeText(getContext(), "该新闻已删除！", Toast.LENGTH_SHORT).show();
        newsList.remove(Integer.parseInt(view.getTag().toString()));
        newsAdapter.notifyDataSetChanged();
    }

}
