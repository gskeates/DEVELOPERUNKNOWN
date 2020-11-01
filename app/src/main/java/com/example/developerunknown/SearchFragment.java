package com.example.developerunknown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "com.example.developerunknown.MESSAGE";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView resultList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> dataList = new ArrayList<>();
    TextView searchBook;
    //String []books ={"To kill a mockingbird", "Indian Horse", "1984","1984", "Greenlight"};
    Button search_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        //final List<Book> books = new ArrayList<>();
    

        resultList = (ListView)view.findViewById(R.id.result_list);

        //dataList.addAll(books);
        Context context= container.getContext();;
        bookAdapter = new CustomList(context, dataList);

        //bookAdapter = new CustomList(getActivity(), dataList);

        resultList.setAdapter(bookAdapter);

        searchBook = (TextView)view.findViewById(R.id.editText_book);

        search_button = (Button)view.findViewById(R.id.search);
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String keyword = searchBook.getText().toString();
                CollectionReference BookRef = (CollectionReference) db.collectionGroup("Book");
                //Query myQuery = db.collectionGroup("Book").whereEqualTo("description", keyword);
                //Query query = db.collection("users").startAt(keyword).endAt(searchText+ "\uf8ff");
                Query myQuery = BookRef.startAt(keyword).endAt(keyword+ "\uf8ff");
                myQuery.get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    System.out.println("here");
                                    dataList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String Description = document.getString("description");
                                        String Author = document.getString("author");
                                        String Title = document.getString("title");
                                        String ISBN = document.getString("ISBN");
                                        String Status = document.getString("status");
                                        Book nowBook = new Book(Title, Author, Status, ISBN, Description);
                                        dataList.add(nowBook);
                                        bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetcheh
                                    }
                                    // [END_EXCLUDE]
                                }}
                        });}});

        /*search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                db.collectionGroup("Book").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String Description = document.getString("description");
                                String Author = document.getString("author");
                                String Title = document.getString("title");
                                String ISBN = document.getString("ISBN");
                                String Status = document.getString("status");
                                Book nowBook = new Book(Title, Author, Status, ISBN, Description);
                                dataList.add(nowBook);
                                bookAdapter.notifyDataSetChanged();;
                            }
                        }
                    }
                });
            }
        }); */

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Intent intent = new Intent(getActivity(),resultActivity.class);
                Book thisBook = bookAdapter.getItem(pos);
                intent.putExtra("SelectedBook", thisBook);
                startActivity(intent);
            }
        });

        return view;
    }

}
