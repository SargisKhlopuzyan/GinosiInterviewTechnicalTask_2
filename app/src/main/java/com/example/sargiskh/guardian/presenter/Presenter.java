package com.example.sargiskh.guardian.presenter;

import android.support.annotation.NonNull;

import com.example.sargiskh.guardian.network.DataResponse;
import com.example.sargiskh.guardian.network.APIService;
import com.example.sargiskh.guardian.network.RetrofitClientInstance;
import com.example.sargiskh.guardian.utils.Constants;
import com.example.sargiskh.guardian.view.MainActivityContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Presenter implements PresenterContract, NetworkService.NetworkServiceCallback {

    private MainActivityContract mainActivityContract;
    private NetworkService networkService;

    public Presenter(MainActivityContract mainActivityContract, @NonNull final NetworkService networkService) {
        this.mainActivityContract = mainActivityContract;
        this.networkService = networkService;
    }

    @Override
    public void getDataSearchedByPage(int pageNumber) {
        networkService.searchDataByPage(pageNumber, this);
    }

    @Override
    public void getDataSearchedByPhrase(String phrase) {
        networkService.searchDataByPhrase(phrase, this);
    }

    @Override
    public void handleResponseForPageNumber(@NonNull Response<DataResponse> response) {
        if (DataController.getInstance().getLoadedTries() == 1) {
            DataController.getInstance().setData(response.body());
        } else {
            DataController.getInstance().addData(response.body());
        }
        //2nd approach //In this case we can directly update view. this time we need handle orientation changes
//      mainActivityContract.updateRecyclerView(response.body());
    }

    @Override
    public void handleResponseForPhrase(@NonNull Response<DataResponse> response) {
        DataController.getInstance().setData(response.body());

        //2nd approach //In this case we can directly update view. this time we need handle orientation changes
//      mainActivityContract.updateRecyclerView(response.body());
    }

    @Override
    public void handleError(String errorMessage) {
        mainActivityContract.displayError(errorMessage);
    }

    @Override
    public void handleError() {

    }
}
