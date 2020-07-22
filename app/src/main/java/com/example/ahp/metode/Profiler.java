package com.example.ahp.metode;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.TreeMap;

class Profiler {

    private FirebaseFirestore firestore;

    public Profiler(){
        firestore=FirebaseFirestore.getInstance();
    }

    private TreeMap<String, ArrayList<Fuzzy>> availableSites = new TreeMap<>();

    public TreeMap<String, ArrayList<Fuzzy>> start(){
        for (String alternative : Data.alternatives) {
            ArrayList<Fuzzy> criteriaImportance = profileNode(alternative);
            availableSites.put(alternative, criteriaImportance);
        }

        return availableSites;
    }

        private ArrayList<Fuzzy> profileNode(String node) {

        ArrayList<Fuzzy> siteCriteria = new ArrayList<>();

//        // Mobile node
//        if (node.equalsIgnoreCase(Data.alternatives[0])) {
//            siteCriteria.add(Data.MOBILE_BANDWIDTH);
//            siteCriteria.add(Data.MOBILE_SPEED);
//            siteCriteria.add(Data.MOBILE_AVAILABILITY);
//            siteCriteria.add(Data.MOBILE_SECURITY);
//            siteCriteria.add(Data.MOBILE_PRICE);
//        } else if (node.equalsIgnoreCase(Data.alternatives[1])) { // Edge
//            siteCriteria.add(Data.EDGE_BANDWIDTH);
//            siteCriteria.add(Data.EDGE_SPEED);
//            siteCriteria.add(Data.EDGE_AVAILABILITY);
//            siteCriteria.add(Data.EDGE_SECURITY);
//            siteCriteria.add(Data.EDGE_PRICE);
//        }
//        // Public cloud instance
//        else {
//            siteCriteria.add(Data.PUBLIC_BANDWIDTH);
//            siteCriteria.add(Data.PUBLIC_SPEED);
//            siteCriteria.add(Data.PUBLIC_AVAILABILITY);
//            siteCriteria.add(Data.PUBLIC_SECURITY);
//            siteCriteria.add(Data.PUBLIC_PRICE);
//        }

        //Mobile-1
        if (node.equalsIgnoreCase(Data.alternatives[0])) {
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.GOOD);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_LOW);
        }

        //Mobile-2
        else if (node.equalsIgnoreCase(Data.alternatives[1])) {
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_LOW);
        }

        //Mobile-3
        else if (node.equalsIgnoreCase(Data.alternatives[2])) {
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_LOW);
        }

        //Edge-1
        else if (node.equalsIgnoreCase(Data.alternatives[3])) {
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.GOOD);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.LOW);
        }

        //Edge-2
        else if (node.equalsIgnoreCase(Data.alternatives[4])) {
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.GOOD);
        }

        //Edge-3
        else if (node.equalsIgnoreCase(Data.alternatives[5])) {
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.HIGH);
        }

        //Public-1
        else if (node.equalsIgnoreCase(Data.alternatives[6])) {
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.GOOD);
        }

        //Public-2
        else if (node.equalsIgnoreCase(Data.alternatives[7])) {
            siteCriteria.add(Fuzzy.GOOD);
            siteCriteria.add(Fuzzy.HIGH);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.HIGH);
        }

        //Public-3
        else if (node.equalsIgnoreCase(Data.alternatives[8])) {
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.VERY_HIGH);
            siteCriteria.add(Fuzzy.LOW);
            siteCriteria.add(Fuzzy.VERY_HIGH);
        }

        return siteCriteria;
    }
}
