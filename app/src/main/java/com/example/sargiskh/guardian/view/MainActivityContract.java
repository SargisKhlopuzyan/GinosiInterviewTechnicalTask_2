package com.example.sargiskh.guardian.view;

import com.example.sargiskh.guardian.network.DataResponse;

public interface MainActivityContract {
    void updateRecyclerView(DataResponse dataResponse);
    void displayError(String errorMessage);
    void showToast(String message);
}
