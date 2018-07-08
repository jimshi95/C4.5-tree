import java.util.ArrayList;
import java.util.Random;

public class TrainTestSplit {

    public static ArrayList<Values> trainTestSplit(Values allValues, Double testSize, int seed) {
        Random rand = new Random(seed);
        ArrayList<RowValues> allRowValues = new ArrayList<>(allValues.getData());
        ArrayList<Values> result = new ArrayList<>();
        Values trainValues = Values.copyAllInsteadOfData(allValues);
        Values testValues = Values.copyAllInsteadOfData(allValues);
        ArrayList<RowValues> trainRowValues = new ArrayList<>();
        ArrayList<RowValues> testRowValues = new ArrayList<>();
        int trainCount = (int) ((1 - testSize) * allRowValues.size());
        for (int j = 0; j < trainCount; j++) {
            int index = rand.nextInt(allRowValues.size());
            trainRowValues.add(allRowValues.get(index));
            allRowValues.remove(index);
        }
        trainValues.setData(trainRowValues);
        result.add(trainValues);

        while (allRowValues.size() > 0) {
            int index = rand.nextInt(allRowValues.size());
            testRowValues.add(allRowValues.get(index));
            allRowValues.remove(index);
        }
        testValues.setData(testRowValues);
        result.add(testValues);
        return result;
    }
}
