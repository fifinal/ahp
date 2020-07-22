package com.example.ahp.metode;

import android.util.Log;

public class Diagnosa {

    private  AHP ahp;
    public void calculateAHP(){

        System.out.println("criteria "+Data.criteria.toString());
        ahp = new AHP(Data.criteria);

        double[] compArray = ahp.getPairwiseComparisonArray();

        // Set the pairwise comparison values
//        compArray[0] = Data.BANDWIDTH_SPEED;
//        compArray[1] = Data.BANDWIDTH_AVAILABILITY;
//        compArray[2] = Data.BANDWIDTH_SECURITY;
//        compArray[3] = Data.BANDWIDTH_PRICE;
//        compArray[4] = Data.SPEED_AVAILABILITY;
//        compArray[5] = Data.SPEED_SECURITY;
//        compArray[6] = Data.SPEED_PRICE;
//        compArray[7] = Data.AVAILABLITY_SECURITY;
//        compArray[8] = Data.AVAIALABILITY_PRICE;
//        compArray[9] = Data.SECURITY_PRICE;

        double[] dm = {1.0, 5.0, 7.0, 9.0, 5.0, 6.0, 8.0, 3.0, 3.0, 2.0}; // DM1
//        double[] dm = {5.0, 7.0, 9.0, 9.0, 3.0, 7.0, 7.0, 5.0, 5.0, 1.0}; // DM2
//        double[] dm = {1.0/7.0, 1.0/5.0, 5.0, 5.0, 3.0, 9.0, 9.0, 9.0, 9.0, 1.0}; // DM3
//        double[] dm = {1.0, 1.0, 1.0/9.0, 3.0, 3.0, 1.0/9.0, 3.0, 1.0/9.0, 3.0, 9.0}; // DM4
//        double[] dm = {1.0, 3.0, 3.0, 1.0/9.0, 3.0, 3.0, 1.0/9.0, 3.0, 1.0/9.0, 1.0/9.0}; // DM5

//        double[] dm = Data.criteriaWeight; // DM1

//        System.arraycopy(dm, 0, compArray, 0, 10);

        setPairwiseComparisonArray();
        ahp.setEvd();

        for (int i = 0; i < ahp.getNrOfPairwiseComparisons(); i++) {
            System.out.print(ahp.getIndicesForPairwiseComparison(i)[1]+" Importance of " + Data.criteria[ahp.getIndicesForPairwiseComparison(i)[0]] + " compared to ");
            System.out.print(Data.criteria[ahp.getIndicesForPairwiseComparison(i)[1]] + "= ");
            System.out.println(ahp.getPairwiseComparisonArray()[i]);
        }

        System.out.println("A: " + ahp.toString());

        System.out.println("Geometric Consistency Index: " + Data.df.format(ahp.getGeometricConsistencyIndex()));
        System.out.println("Geometric Cardinal Consistency Index: " + Data.df.format(ahp.getGeometricCardinalConsistencyIndex()));
        System.out.println("Consistency Index: " + Data.df.format(ahp.getConsistencyIndex()));
        System.out.println("Consistency Ratio: " + Data.df.format(ahp.getConsistencyRatio()) + "%");
        System.out.println("Weights: ");
        for (int k=0; k<ahp.getWeights().length; k++) {
            Data.ahpWeights[k] = ahp.getWeights()[k];
            System.out.println(Data.criteria[k] + ": " + Data.df.format(ahp.getWeights()[k]));
        }
    }

    private void setPairwiseComparisonArray(){
        int i = 0;
        Log.d("crteria weight ",Data.criteria.length+"");

        for (int row = 0; row < Data.criteria.length; row++) {
            for (int col = row + 1; col < Data.criteria.length; col++) {
                String idKombinasi1=Data.criteria[row]+"-"+Data.criteria[col];
                String idKombinasi2=Data.criteria[col]+"-"+Data.criteria[row];
                Log.d("matrik ",idKombinasi1+" "+idKombinasi2);
                if(Data.criteriaWeight.get(idKombinasi1)!=null){
                    ahp.getMtx().setEntry(row, col, 1.0);
                    ahp.getMtx().setEntry(col, row, 1.0);
                }
                else if(Data.criteriaWeight.get(idKombinasi2)!=null){
                    ahp.getMtx().setEntry(row, col, 1.0);
                    ahp.getMtx().setEntry(col, row, 1.0 );
                }
                Log.d("matrik ",row+" "+col);
                ahp.comparisonIndices[i][0] = row;
                ahp.comparisonIndices[i][1] = col;
                Log.d("matrik arr",ahp.comparisonIndices.toString());

                i++;
            }
        }

    }

    public void topsisMethod(){
        Topsis topsis = new Topsis();
        topsis.start();
    }

    public void main() {
        Diagnosa test = new Diagnosa();

        System.out.println("Calculating AHP Criteria weighting: ");
        test.calculateAHP();

        System.out.println("End of AHP");
        System.out.println("********************************");
        System.out.println("Calculating Fuzzy TOPSIS: ");

        test.topsisMethod();
    }
}
