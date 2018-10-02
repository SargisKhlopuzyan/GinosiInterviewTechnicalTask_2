package com.example.sargiskh.guardian_2;

import com.example.sargiskh.guardian_2.model.Results;
import com.example.sargiskh.guardian_2.network.DataResponse;
import com.example.sargiskh.guardian_2.presenter.NetworkService;
import com.example.sargiskh.guardian_2.presenter.Presenter;
import com.example.sargiskh.guardian_2.view.MainActivityContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import retrofit2.Response;

public class PresenterTest {


    private Presenter presenter;


    @Mock
    private NetworkService networkService;
    @Mock
    private MainActivityContract viewContract;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this); // required for the "@Mock" annotations

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = Mockito.spy(new Presenter(viewContract, networkService));
    }



    @Test
    public void searchDataSearchedByPage_noQuery() {

        int pageNumber = -1;

        // Trigger
        presenter.getDataSearchedByPage(pageNumber);

        // Validation
        Mockito.verify(networkService, Mockito.never()).searchDataByPage(pageNumber, presenter);
    }

    @Test
    public void searchDataSearchedByPhrase_noQuery() {

        String phrase = null;

        // Trigger
        presenter.getDataSearchedByPhrase(phrase);

        // Validation
        Mockito.verify(networkService, Mockito.never()).searchDataByPhrase(phrase, presenter);
    }



    @SuppressWarnings("unchecked")
    @Test
    public void handleSearchByPageResponse_Success() {
        Response response = Mockito.mock(Response.class);
        DataResponse dataResponse = Mockito.mock(DataResponse.class);

        Mockito.doReturn(true).when(response).isSuccessful();
        Mockito.doReturn(dataResponse).when(response).body();

        com.example.sargiskh.guardian_2.model.Response response1 = new com.example.sargiskh.guardian_2.model.Response();
        response1.results = new ArrayList<>();
        response1.results.add(new Results());
        response1.results.add(new Results());
        response1.results.add(new Results());

        Mockito.doReturn(response1).when(dataResponse).getResponse();

        //trigger
        presenter.handleResponseForPageNumber(response);

        //validation
        Mockito.verify(viewContract, Mockito.times(1)).displaySearchResults(dataResponse);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleSearchByPhraseResponse_Success() {
        Response response = Mockito.mock(Response.class);
        DataResponse dataResponse = Mockito.mock(DataResponse.class);

        Mockito.doReturn(true).when(response).isSuccessful();
        Mockito.doReturn(dataResponse).when(response).body();

        com.example.sargiskh.guardian_2.model.Response response1 = new com.example.sargiskh.guardian_2.model.Response();
        response1.results = new ArrayList<>();
        response1.results.add(new Results());
        response1.results.add(new Results());
        response1.results.add(new Results());

        Mockito.doReturn(response1).when(dataResponse).getResponse();

        //trigger
        presenter.handleResponseForPhrase(response);

        //validation
        Mockito.verify(viewContract, Mockito.times(1)).displaySearchResults(dataResponse);
    }



    @Test
    public void handleSearchByPageResponse_Failure() {
        Response response = Mockito.mock(Response.class);

        // case No. 1
        Mockito.doReturn(false).when(response).isSuccessful();

        //trigger
        presenter.handleResponseForPageNumber(response);

        //validation
        Mockito.verify(viewContract, Mockito.times(1)).displayError(Mockito.anyString());

        /* if the following is not called, viewContract.displayError() will be mistakenly considered as invoked twice */
        Mockito.reset(viewContract);


        // case No. 2
        Mockito.doReturn(null).when(response).body();

        //trigger
        presenter.handleResponseForPageNumber(response);

        //validation
        Mockito.verify(viewContract, Mockito.times(1)).displayError(Mockito.anyString());

    }

    @Test
    public void handleSearchByPhraseResponse_Failure() {
        Response response = Mockito.mock(Response.class);

        // case No. 1
        Mockito.doReturn(false).when(response).isSuccessful();

        //trigger
        presenter.handleResponseForPhrase(response);

        //validation
        Mockito.verify(viewContract, Mockito.times(1)).displayError(Mockito.anyString());

        /* if the following is not called, viewContract.displayError() will be mistakenly considered as invoked twice */
        Mockito.reset(viewContract);


        // case No. 2
        Mockito.doReturn(null).when(response).body();

        //trigger
        presenter.handleResponseForPhrase(response);

        //validation
        Mockito.verify(viewContract, Mockito.times(1)).displayError(Mockito.anyString());

    }



    @Test
    public void handleError() {

        //trigger
        presenter.handleError();

        //validation
        Mockito.verify(viewContract, Mockito.times(1)).displayError(Mockito.anyString());
    }

}
