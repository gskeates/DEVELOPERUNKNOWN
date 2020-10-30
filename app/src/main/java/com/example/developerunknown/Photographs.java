package com.example.developerunknown;
import android.media.Image;

import java.lang.String;
import java.util.AbstractMap;

public class Photographs {
    private Image defaultPhoto;
    private AbstractMap<String, Image> bookPhotoPair;
    private AbstractMap<String, Image> userPhotoPair;

    public Photographs(Image defaultPhoto, AbstractMap<String, Image> bookPhotoPair) {
        this.defaultPhoto = defaultPhoto;
        this.bookPhotoPair = bookPhotoPair;
    }

    public AbstractMap<String, Image> getBookPhotoPair() {
        return bookPhotoPair;
    }

    public AbstractMap<String, Image> getUserPhotoPair() {
        return userPhotoPair;
    }

    public void attachBookPhoto(String a_book, Image a_photo){
        bookPhotoPair.put(a_book, a_photo);

    }
    public void deleteBookPhoto(String a_book){
        bookPhotoPair.put(a_book, defaultPhoto);
    }

    public void attachUserPhoto(String a_book, Image a_photo){
        userPhotoPair.put(a_book, a_photo);

    }
    public void deleteUserPhoto(String a_book){
        userPhotoPair.put(a_book, defaultPhoto);
    }

    public void viewPhoto(){

    }
}
