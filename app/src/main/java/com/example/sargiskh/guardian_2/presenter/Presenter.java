package com.example.sargiskh.guardian_2.presenter;

import android.support.annotation.NonNull;

import com.example.sargiskh.guardian_2.network.DataResponse;
import com.example.sargiskh.guardian_2.view.MainActivityContract;

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
        if (pageNumber > 0) {
            networkService.searchDataByPage(pageNumber, this);
        }
    }

    @Override
    public void getDataSearchedByPhrase(String phrase) {
        if (phrase != null && !phrase.isEmpty()) {
            networkService.searchDataByPhrase(phrase, this);
        }
    }


    @Override
    public void handleResponseForPageNumber(@NonNull Response<DataResponse> response) {
        if (response.isSuccessful()) {
            DataResponse dataResponse = response.body();
            if (dataResponse != null && dataResponse.getResponse() != null) {
                if (DataController.getInstance().getLoadedTries() == 1) {
                    DataController.getInstance().setData(response.body());
                } else {
                    DataController.getInstance().addData(response.body());
                }
                //2nd approach //In this case we can directly update view. this time we need handle orientation changes
//              mainActivityContract.displaySearchResults(response.body());
            } else {
                mainActivityContract.displayError("empty response");
            }
        } else {
            mainActivityContract.displayError("failed to get response");
        }
    }

    @Override
    public void handleResponseForPhrase(@NonNull Response<DataResponse> response) {
        if (response.isSuccessful()) {
            DataResponse dataResponse = response.body();
            if (dataResponse != null && dataResponse.getResponse() != null) {
                DataController.getInstance().setData(response.body());
                //2nd approach //In this case we can directly update view. this time we need handle orientation changes
//              mainActivityContract.displaySearchResults(response.body());
            } else {
                mainActivityContract.displayError("empty response");
            }
        } else {
            mainActivityContract.displayError("failed to get response");
        }
    }

    @Override
    public void handleError(String errorMessage) {
        mainActivityContract.displayError(errorMessage);
    }

    @Override
    public void handleError() {

    }
}
