import java.util.ArrayList;
import java.util.Random;

public class CrossValidation {


    private int kfolds;
    private Values allValues;
    private Random rand;

    private ArrayList<Values> kfoldBundles;

    CrossValidation(int kfolds, Values allValues, int seed) {
        this.kfolds = kfolds;
        this.allValues = allValues;
        rand = new Random(seed);
    }

    public void validate(boolean needPruning) {
        shuffle();

        ArrayList<Double> scores = new ArrayList<>();
        for (int i = 0; i < kfolds; i++) {
            Values trainValues = new Values();
            trainValues.setTarget(allValues.getTarget());
            trainValues.setAttribute2Index(allValues.getAttribute2Index());
            trainValues.setData(new ArrayList<>());
            Values testValues = null;
            for (int j = 0; j < kfolds; j++) {
                if (i == j) {
                    testValues = kfoldBundles.get(i);
                } else {
                    trainValues.getData().addAll(kfoldBundles.get(j).getData());
                }
            }

            assert testValues != null;

            DecisionTree decisionTree = new DecisionTree(needPruning);

            decisionTree.train(trainValues);

            scores.add(Score.calcAccuracy(decisionTree, testValues));
        }

        System.out.println(scores);
        Double sum = 0d;
        for (Double score : scores) {
            sum += score;
        }
        System.out.println("Mean accuracy: " + sum / scores.size());
    }

    private void shuffle() {
        ArrayList<RowValues> allRowValues = new ArrayList<>(allValues.getData());
        int avgSize = allRowValues.size() / kfolds;
        kfoldBundles = new ArrayList<>();
        for (int i = 0; i < kfolds; i++) {
            Values v = Values.copyAllInsteadOfData(allValues);
            ArrayList<RowValues> rowValues = new ArrayList<>();
            for (int j = 0; j < avgSize; j++) {
                int index = rand.nextInt(allRowValues.size());
                rowValues.add(allRowValues.get(index));
                allRowValues.remove(index);
            }
            if (i == kfolds - 1) {
                while (allRowValues.size() > 0) {
                    int index = rand.nextInt(allRowValues.size());
                    rowValues.add(allRowValues.get(index));
                    allRowValues.remove(index);
                }
            }
            v.setData(rowValues);
            kfoldBundles.add(v);
        }
    }

    public int getKfolds() {
        return kfolds;
    }

    public void setKfolds(int kfolds) {
        this.kfolds = kfolds;
    }


    public Values getAllValues() {
        return allValues;
    }

    public void setAllValues(Values allValues) {
        this.allValues = allValues;
    }
}
