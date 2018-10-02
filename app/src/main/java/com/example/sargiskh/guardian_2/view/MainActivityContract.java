package com.example.sargiskh.guardian_2.view;

import com.example.sargiskh.guardian_2.network.DataResponse;

public interface MainActivityContract {
    void displaySearchResults(DataResponse dataResponse);
    void displayError(String errorMessage);
    void showToast(String message);
}
