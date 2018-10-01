package com.example.sargiskh.guardian.presenter;

public interface PresenterContract {
    void getDataSearchedByPage(int pageNumber);
    void getDataSearchedByPhrase(String stringToSearch);
}
