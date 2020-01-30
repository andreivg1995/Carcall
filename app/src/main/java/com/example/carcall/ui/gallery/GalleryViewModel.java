package com.example.carcall.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<Query> mQuery;

    public GalleryViewModel() {
        mQuery = new MutableLiveData<>();
        //mText.setValue("This is gallery fragment");
    }

    public LiveData<Query> getViajes() {
        return mQuery;
    }

    public void firebaseQuery(FirebaseUser user) {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("viajes")
                .orderByChild("uid")
                .equalTo(user.getUid());

        mQuery.postValue(query);
    }
}