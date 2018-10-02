package com.example.sargiskh.guardian_2.presenter;

public interface PresenterContract {
    void getDataSearchedByPage(int pageNumber);
    void getDataSearchedByPhrase(String stringToSearch);
}
