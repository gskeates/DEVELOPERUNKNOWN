package com.example.developerunknown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewRequestsFragment extends Fragment {
    ListView requestList;
    ArrayAdapter<Request> requestAdapter;
    ArrayList<Request> requestDataList = new ArrayList<>();
    Context context;
    User currentUser;
    Book clickedBook;

    public Button backButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public String bookid;
    public CollectionReference bookRequestCollectionReference;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        bookid = clickedBook.getID();
        // Look at this
        bookRequestCollectionReference = db.collection("user").
                document(uid).collection("Book").
                document(bookid).collection("Request");

        View view = inflater.inflate(R.layout.fragment_view_requests, container,false);
        final Context context= container.getContext();

        requestList = view.findViewById(R.id.request_list);

        // Grab requests
        CollectionReference bookRef = db.collection("user").document(currentUser.getUid()).collection("book").document(clickedBook.getID()).collection("Request");
        bookRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                requestDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String bookid = (String)doc.getData().get("Bookid");
                    String borrower = (String) doc.getData().get("Borrower");
                    String borrowerUname = (String) doc.getData().get("BorrowerUname");
                    //Timestamp time = (Timestamp) doc.getData().get("time");
                    String Rid = doc.getId();
                    requestDataList.add(new Request(borrower, borrowerUname, bookid,Rid));

                    //requestDataList.add(new UserNotification(sender, time, type, book,id) ); // Adding the cities and provinces from FireStore
                }
                //requestDataList.notifyDataSetChanged(); // Notifying the adapter to render any new data fetch
            }
        });


        // Display requests
        requestAdapter = new RequestList(context, requestDataList);
        requestList.setAdapter(requestAdapter);

        // Click oh item listener
        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Intent intent = new Intent(getActivity(),requestActicity.class);
                Request thisRequest = requestAdapter.getItem(pos);
                intent.putExtra("Request", (Serializable) thisRequest);
                startActivity(intent);
            }
        });

        // Get Back Button
        backButton = view.findViewById(R.id.back_button);

        return view;
    }

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        bookRequestCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                requestDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String borrowerID = (String) doc.getData().get("Borrower");
                    String borrowerUname = (String) doc.getData().get("BorrowerUname");

                    requestDataList.add(new Request(borrowerID, borrowerUname, clickedBook.getID())); // Adding the requests from FireStore
                }
                requestAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetcheh
            }
        });

    }

}