package com.example.ahp.metode;

import com.example.ahp.model.KombinasiGejala;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Data {

    // Set your alternatives here
//    static String[] alternatives = new String[]{"Mobile", "Edge", "Public"};
    public static String[] alternatives;

    // Set your criteria here
    public static String[] criteria;

    public static ArrayList<Map<String, String>> finalRanking=new ArrayList<>();

    // Set true for benefit criterion, false for cost criterion
    public static boolean[] costCriteria = new boolean[]{false, false, false, false, true,false, false, false, false,false, false, false, false,false, false, false, false,false,false}; // price is cost

    public static Double[] ahpWeights;

    public static TreeMap<String, ArrayList<Fuzzy>> availableSites = new TreeMap<>();
    public static TreeMap<String, KombinasiGejala> criteriaWeight = new TreeMap<>();

    // AHP criteria weights in respect to each other
    public static final Double BANDWIDTH_SPEED = 1.0;
    public static final Double BANDWIDTH_AVAILABILITY = 5.0;
    public static final Double BANDWIDTH_SECURITY = 7.0;
    public static final Double BANDWIDTH_PRICE = 9.0;
    public static final Double SPEED_AVAILABILITY = 5.0;
    public static final Double SPEED_SECURITY = 6.0;
    public static final Double SPEED_PRICE = 8.0;
    public static final Double AVAILABLITY_SECURITY = 3.0;
    public static final Double AVAIALABILITY_PRICE = 3.0;
    public static final Double SECURITY_PRICE = 2.0;

    // The following values are obtained in profiling stage prior to offloading
    // Here, we just use static fuzzy values for each alternative
    public static final Fuzzy MOBILE_BANDWIDTH = Fuzzy.VERY_HIGH;
    public static final Fuzzy MOBILE_SPEED = Fuzzy.GOOD;
    public static final Fuzzy MOBILE_AVAILABILITY = Fuzzy.HIGH;
    public static final Fuzzy MOBILE_SECURITY = Fuzzy.HIGH;
    public static final Fuzzy MOBILE_PRICE = Fuzzy.VERY_LOW;

    public static final Fuzzy EDGE_BANDWIDTH = Fuzzy.VERY_HIGH;
    public static final Fuzzy EDGE_SPEED = Fuzzy.HIGH;
    public static final Fuzzy EDGE_AVAILABILITY = Fuzzy.HIGH;
    public static final Fuzzy EDGE_SECURITY = Fuzzy.HIGH;
    public static final Fuzzy EDGE_PRICE = Fuzzy.LOW;

    public static final Fuzzy PUBLIC_BANDWIDTH = Fuzzy.LOW;
    public static final Fuzzy PUBLIC_SPEED = Fuzzy.VERY_HIGH;
    public static final Fuzzy PUBLIC_AVAILABILITY = Fuzzy.VERY_HIGH;
    public static final Fuzzy PUBLIC_SECURITY = Fuzzy.GOOD;
    public static final Fuzzy PUBLIC_PRICE = Fuzzy.VERY_HIGH;

    // These values can also be computed from max and min of weighted decision matrix
    public static double[] idealSolution = {0.75, 1.0, 1.0};
    public static double[] antiIdealSolution = {0.0, 0.0, 0.25};

    // Number of decimal points for float number formatting
    public static DecimalFormat df = new DecimalFormat("0.0000");

}
