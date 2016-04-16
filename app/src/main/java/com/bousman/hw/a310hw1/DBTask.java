package com.bousman.hw.a310hw1;


public class DBTask {
    private long id;
    private String searchString;


    public DBTask(String searchString){
        this.searchString = searchString;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getsearchString(){
        return searchString;
    }
    public void setsearchString(String searchString){
        this.searchString = searchString;
    }


    @Override
    public String toString() {
        return id + "," + searchString;
    }
}
