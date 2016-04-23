package com.bousman.hw.a310hw1;


public class DBTask {
    private long id;
    private String state;


    public DBTask(String searchString){
        this.state = searchString;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state = state;
    }


    @Override
    public String toString() {
        return id + "," + state;
    }
}
