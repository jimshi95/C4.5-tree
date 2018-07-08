import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        InputReader inputReader = new InputReader();
        Values values = inputReader.readData();

        System.out.println("All attribute:");
        System.out.println(values.getAttribute2Index().keySet());
        String target = inputReader.readString("Please print target:  ");
        values.setTarget(target);

        int kfolds = 10;
        int seed = 123;
        CrossValidation cv = new CrossValidation(kfolds, values, seed);
        System.out.println("Before pruning:");
        cv.validate(false);
        System.out.println();
        System.out.println("After pruning:");
        cv.validate(true);

        System.out.println();

        List<Values> trainTestValues = TrainTestSplit.trainTestSplit(values, 0.3, seed);
        Values trainValues = trainTestValues.get(0);
        Values testValues = trainTestValues.get(1);

        DecisionTree decisionTree = new DecisionTree(true);

        decisionTree.train(trainValues);

        System.out.println("Train size : test size is 7:3");
        System.out.println("Accuracy in train data: " + Score.calcAccuracy(decisionTree, trainValues));
        System.out.println("Accuracy in test data:  " + Score.calcAccuracy(decisionTree, testValues));
    }
}
