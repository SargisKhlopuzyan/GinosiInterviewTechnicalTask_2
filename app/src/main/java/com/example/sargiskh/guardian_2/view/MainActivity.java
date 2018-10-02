package com.example.sargiskh.guardian_2.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.sargiskh.guardian_2.R;
import com.example.sargiskh.guardian_2.network.DataResponse;
import com.example.sargiskh.guardian_2.presenter.DataController;
import com.example.sargiskh.guardian_2.presenter.NetworkService;
import com.example.sargiskh.guardian_2.presenter.Presenter;

public class MainActivity extends AppCompatActivity implements MainActivityContract, OnBottomReachedListener {

    private Presenter presenter;

    private SearchView searchView;
    private TextView textViewStatus;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("LOG_TAG", "_onCreate");
        presenter = new Presenter(this, new NetworkService());

        findViews();
        setupListPeopleView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if (DataController.getInstance().isInSearchByPhraseMode()) {
                    DataController.getInstance().setLoadedTries(1);
                    presenter.getDataSearchedByPhrase(searchView.getQuery().toString());
                } else {
                    getInitialDataByPage();
                }
            }
        });

        LiveData<DataResponse> liveData = DataController.getInstance().getData();
        liveData.observe(this, new Observer<DataResponse>() {
            @Override
            public void onChanged(@Nullable DataResponse value) {
                Log.e("LOG_TAG", "_onChanged");
                displaySearchResults(value);
            }
        });

        if (liveData.getValue() == null ||liveData.getValue().getResponse() == null || liveData.getValue().getResponse().results == null || liveData.getValue().getResponse().results.size() == 0) {
            getInitialDataByPage();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("LOG_TAG", "_onResume");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                DataController.getInstance().setLoadedTries(1);
                if (newText.isEmpty()) {
                    DataController.getInstance().setIsInSearchByPhraseMode(false);
                    getInitialDataByPage();
                } else {
                    DataController.getInstance().setIsInSearchByPhraseMode(true);
                    presenter.getDataSearchedByPhrase(newText);
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("LOG_TAG", "_onPause");
        searchView.setOnQueryTextListener(null);
    }

    @Override
    protected void onDestroy() {
        Log.e("LOG_TAG", "_onDestroy");
        LiveData<DataResponse> liveData = DataController.getInstance().getData();
        liveData.removeObservers(this);
        super.onDestroy();
    }

    private void findViews() {
        searchView = findViewById(R.id.searchView);
        textViewStatus = findViewById(R.id.textViewStatus);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
    }

    public void setupListPeopleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DataAdapter adapter = new DataAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void getInitialDataByPage() {
        DataController.getInstance().setLoadedTries(1);
        presenter.getDataSearchedByPage(DataController.getInstance().getLoadedTries());
    }

    private void getDataOfNextPage() {
        DataController.getInstance().increaseLoadedTriesBy(1);
        presenter.getDataSearchedByPage(DataController.getInstance().getLoadedTries());
    }

    @Override
    public void displaySearchResults(DataResponse dataResponse) {

        DataAdapter dataAdapter = (DataAdapter) recyclerView.getAdapter();
        dataAdapter.setResults(dataResponse.getResponse().results);

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
        textViewStatus.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayError(String errorMessage) {
        textViewStatus.setText("Something went wrong...Please try later! : " + errorMessage);
        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
        textViewStatus.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String toastMessage) { }

    @Override
    public void onBackPressed() {
        int firstVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == 0 || firstVisibleItemPosition == -1) {
            super.onBackPressed();
        } else {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onBottomReached(int position) {
        // Start refresh animation
        if (!swipeRefreshLayout.isRefreshing() && !DataController.getInstance().isInSearchByPhraseMode()) {
            swipeRefreshLayout.setRefreshing(true);
            getDataOfNextPage();
        }
    }

}
