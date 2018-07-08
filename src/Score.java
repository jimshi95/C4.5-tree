public class Score {


    public static Double calcAccuracy(DecisionTree decisionTree, Values testValues) {
        Integer targetIndex = testValues.getTargetIndex();
        int correctCount = 0;
        for (int j = 0; j < testValues.getData().size(); j++) {
            RowValues rowValues = testValues.getData().get(j);
            String predictLabel = decisionTree.predict(rowValues);
            String trueLabel = rowValues.getAttributeList().get(targetIndex).getValue();
            if (predictLabel.equals(trueLabel)) {
                correctCount += 1;
            }
        }
        return 1. * correctCount / testValues.getData().size();
    }
}
