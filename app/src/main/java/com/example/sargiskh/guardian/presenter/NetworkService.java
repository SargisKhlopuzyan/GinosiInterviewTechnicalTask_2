package com.example.sargiskh.guardian.presenter;

import android.support.annotation.NonNull;

import com.example.sargiskh.guardian.network.APIService;
import com.example.sargiskh.guardian.network.DataResponse;
import com.example.sargiskh.guardian.network.RetrofitClientInstance;
import com.example.sargiskh.guardian.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkService {

    public interface NetworkServiceCallback {
        void handleResponseForPhrase(@NonNull Response<DataResponse> response);
        void handleResponseForPageNumber(@NonNull Response<DataResponse> response);
        void handleError(String errorMessage);
        void handleError();
    }


    public void searchDataByPage(int pageNumber, final NetworkServiceCallback networkServiceCallback) {

        /*Create handle for the RetrofitInstance interface*/
        APIService service = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        Call<DataResponse> call = service.getDataSearchedByPage(getUrlForSearchingByPage(pageNumber));
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                networkServiceCallback.handleResponseForPageNumber(response);
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                networkServiceCallback.handleError(t.getMessage());
            }
        });
    }

    public void searchDataByPhrase(String phrase, final NetworkServiceCallback networkServiceCallback) {
        /*Create handle for the RetrofitInstance interface*/
        APIService service = RetrofitClientInstance.getRetrofitInstance().create(APIService.class);
        Call<DataResponse> call = service.getDataSearchedByPhrase(getUrlForSearchingByPhrase(phrase));
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                networkServiceCallback.handleResponseForPhrase(response);
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                networkServiceCallback.handleError(t.getMessage());
            }
        });
    }


    private String getUrlForSearchingByPage(int pageNumber) {

        //NOTE - page-size=10 //this is just to show more articles
        //To search page by page we can use this: https://content.guardianapis.com/search?order-by=newest&page=" + pageNumber + "&api-key=" + Constants.API_KEY

        // In every try the application loads 10 pages
        pageNumber = pageNumber * 10 - 9;

        String url = "https://content.guardianapis.com/search?order-by=newest&page=" + pageNumber + "&page-size=10&api-key=" + Constants.API_KEY;
        return url;
    }

    private String getUrlForSearchingByPhrase(String phrase) {
        phrase = phrase.trim();
        String changedPhrase = phrase.replaceAll("\\s", "%20");
        String url = "https://content.guardianapis.com/search?q=" + changedPhrase + "&api-key=" + Constants.API_KEY;
        return url;
    }

}
