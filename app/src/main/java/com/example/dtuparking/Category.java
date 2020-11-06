package com.example.dtuparking;

import java.util.List;

public class Category {
    private String nameCatalogy;
    private List<LSGIAODICH> list;

    public Category(String nameCatalogy, List<LSGIAODICH> list) {
        this.nameCatalogy = nameCatalogy;
        this.list = list;
    }

    public String getNameCatalogy() {
        return nameCatalogy;
    }

    public void setNameCatalogy(String nameCatalogy) {
        this.nameCatalogy = nameCatalogy;
    }

    public List<LSGIAODICH> getList() {
        return list;
    }

    public void setList(List<LSGIAODICH> list) {
        this.list = list;
    }
}

