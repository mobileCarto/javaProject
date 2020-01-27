package com.tudresden.mobilecartoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.tudresden.mobilecartoapp.AppDatabase.MIGRATION_1_2;

public class HistoryFragment extends Fragment {

    String db_name = "test.sqlite";
    LocationsDAO locationsdao;
    List<Locations> locations_list;
    RecyclerView recyclerView;
    ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        // Check if the Database exists
        final File dbFile = getActivity().getDatabasePath(db_name);
            if (!dbFile.exists()) {
                try {
                    copyDatabaseFile(dbFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

            // Connect to the Database
        AppDatabase database = Room.databaseBuilder(getActivity(), AppDatabase.class, db_name)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build();

            ////Get locations from the Database and display in recycler viewer////
            //Get locations from the Database
        locationsdao = database.getLocationsDAO();
        locations_list = locationsdao.getAllLocations();
        // Display them in recycler list
        //a Java object of the type RecyclerView and link it to the XML RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_list);
        // LinearLayoutManager arranges the items in a one-dimensional list
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new ListAdapter(locations_list);
        recyclerView.setAdapter(listAdapter);

        return rootView;

    }

    private void copyDatabaseFile(String destinationPath) throws IOException {
        InputStream assetsDB = getActivity().getAssets().open(db_name);
        OutputStream dbOut = new FileOutputStream(destinationPath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = assetsDB.read(buffer)) > 0) {
            dbOut.write(buffer, 0, length);
        }
        dbOut.flush();
        dbOut.close();
    }

}