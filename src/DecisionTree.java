import java.util.*;

public class DecisionTree {
    private ArrayList<Integer> featureIndexList;
    private Integer targetIndex;

    private DecisionTreeNode root;

    private Double bestSplitPoint;
    private Integer bestSplitFeatureIndex;

    private boolean needPruning;

    DecisionTree(boolean needPruning) {
        this.needPruning = needPruning;
    }

    public void train(Values trainValues) {
        root = new DecisionTreeNode();
        featureIndexList = trainValues.getFeatureIndexList();
        targetIndex = trainValues.getTargetIndex();
        dfsTrain(trainValues, root);
        if (needPruning) {
            pruning();
        }
    }

    private void dfsTrain(Values trainValues, DecisionTreeNode node) {
        node.setTrainValues(trainValues);

        Collection<Integer> countList = calcCountList(trainValues.getData());
        if (countList.size() <= 1) {
            node.setLabel(trainValues.getData().get(0).getAttributeValue(targetIndex));
            node.setErrorCount(0);
            node.setInstanceCount(trainValues.getData().size());
            node.setLeafCount(1);
            return;
        }

        chooseBestFeatureToSplit(trainValues);
        if (bestSplitPoint == null) {
            setBestLabelInNode(node);
            return;
        }
        node.setSplitPoint(bestSplitPoint);
        node.setFeatureIndex(bestSplitFeatureIndex);

        List<Values> res = splitTrainValues(node);
        DecisionTreeNode leftNode = new DecisionTreeNode();
        DecisionTreeNode rightNode = new DecisionTreeNode();
        dfsTrain(res.get(0), leftNode);
        dfsTrain(res.get(1), rightNode);
        node.setLeft(leftNode);
        node.setRight(rightNode);
        Integer errorCount = leftNode.getErrorCount() + rightNode.getErrorCount();
        Integer instanceCount = leftNode.getInstanceCount() + rightNode.getInstanceCount();
        Integer leafCount = leftNode.getLeafCount() + rightNode.getLeafCount();
        node.setErrorCount(errorCount);
        node.setInstanceCount(instanceCount);
        node.setLeafCount(leafCount);
    }

    private void setBestLabelInNode(DecisionTreeNode node) {
        Values trainValues = node.getTrainValues();
        ArrayList<String> allLabels = trainValues.getAttributeValues(trainValues.getTarget());
        HashMap<String, Integer> labelCount = new HashMap<>();
        for (String label : allLabels) {
            if (labelCount.containsKey(label)) {
                labelCount.put(label, labelCount.get(label) + 1);
            } else {
                labelCount.put(label, 1);
            }
        }
        Integer maxCount = 0;
        String mostCommonLabel = null;
        for (HashMap.Entry<String, Integer> entry : labelCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonLabel = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        node.setLabel(mostCommonLabel);
        Integer errorCount = allLabels.size() - maxCount;
        node.setInstanceCount(allLabels.size());
        node.setErrorCount(errorCount);
        node.setLeafCount(1);
    }

    private List<Values> splitTrainValues(DecisionTreeNode node) {
        Values trainValues = node.getTrainValues();
        Values leftValues = Values.copyAllInsteadOfData(trainValues);
        Values rightValues = Values.copyAllInsteadOfData(trainValues);
        ArrayList<RowValues> data = trainValues.getData();
        ArrayList<RowValues> leftData = new ArrayList<>();
        ArrayList<RowValues> rightData = new ArrayList<>();
        for (RowValues rowValues : data) {
            Integer value = Integer.valueOf(rowValues.getAttributeValue(node.getFeatureIndex()));
            if (value < node.getSplitPoint()) {
                leftData.add(rowValues);
            } else {
                rightData.add(rowValues);
            }
        }
        leftValues.setData(leftData);
        rightValues.setData(rightData);
        List<Values> result = new ArrayList<>();
        result.add(leftValues);
        result.add(rightValues);
        return result;

    }

    private void dfsPruning(DecisionTreeNode node) {
        if (node.getLabel() != null) {
            return;
        }
        Integer instanceCount = node.getInstanceCount();
        Double errorRatio = node.getErrorRatio();
        Integer errorCount = node.getErrorCount();
        Double errorMean = errorRatio * instanceCount;
        Double errorStd = Math.sqrt(errorMean * (1 - errorRatio));
        Integer leafCount = node.getLeafCount();

        setBestLabelInNode(node);
        Double newErrorRatio = node.getErrorRatio();
        Double newErrorMean = newErrorRatio * node.getInstanceCount();


        if (errorRatio + errorStd >= newErrorMean) {
            node.setLeft(null);
            node.setRight(null);
            node.setLeafCount(1);
        } else {
            node.setLabel(null);
            node.setErrorCount(errorCount);
            node.setLeafCount(leafCount);
            dfsPruning(node.getLeft());
            dfsPruning(node.getRight());
        }
    }

    private void pruning() {
        dfsPruning(root);
    }

    private Double calcEntropy(Collection<Integer> countList) {
        Double result = 0d;
        Integer sum = 0;
        for (Integer aCount : countList) {
            sum += aCount;
        }
        for (Integer aCount : countList) {
            Double p = 1. * aCount / sum;
            result -= p * Math.log(p);
        }
        return result;
    }

    private Collection<Integer> calcCountList(List<RowValues> data) {
        HashMap<String, Integer> countMap = new HashMap<>();
        for (RowValues datum : data) {
            List<Attribute> attributeList = datum.getAttributeList();
            String label = attributeList.get(targetIndex).getValue();
            if (countMap.containsKey(label)) {
                countMap.put(label, countMap.get(label) + 1);
            } else {
                countMap.put(label, 1);
            }
        }
        return countMap.values();
    }

    private void chooseBestFeatureToSplit(Values trainValues) {
        Double initEntropy = calcEntropy(calcCountList(trainValues.getData()));

        bestSplitPoint = null;
        bestSplitFeatureIndex = null;
        Double maxGainRatio = null;
        for (Integer featureIndex : featureIndexList) {
            List<Double> temp = calcBestSplitPoint(initEntropy, featureIndex, trainValues);
            Double gainRatio = temp.get(0);
            Double splitPoint = temp.get(1);
            if (splitPoint != null && (maxGainRatio == null || maxGainRatio < gainRatio)) {
                maxGainRatio = gainRatio;
                bestSplitPoint = splitPoint;
                bestSplitFeatureIndex = featureIndex;
            }
        }
    }

    private ArrayList<Double> calcBestSplitPoint(Double initEntropy, Integer featureIndex, Values trainValues) {
        ArrayList<RowValues> data = (ArrayList<RowValues>) trainValues.getData().clone();
        data.sort((RowValues o1, RowValues o2) -> {
                    Integer i1 = Integer.valueOf(o1.getAttributeValue(featureIndex));
                    Integer i2 = Integer.valueOf(o2.getAttributeValue(featureIndex));
                    if (i1.equals(i2)) {
                        return 0;
                    }
                    if (i1 < i2) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
        );
        LinkedList<RowValues> lowList = new LinkedList<>();
        LinkedList<RowValues> highList = new LinkedList<>(data);
        Double maxGain = null;
        Double gainRatio = null;
        Double splitPoint = null;
        for (int i = 0; i < data.size() - 1; i++) {
            RowValues currentRowValues = data.get(i);
            RowValues nextRowValues = data.get(i + 1);
            lowList.add(currentRowValues);
            highList.removeFirst();
            if (currentRowValues.getAttributeValue(featureIndex).equals(nextRowValues.getAttributeValue(featureIndex))) {
                continue;
            }
            Double lowEntropy = calcEntropy(calcCountList(lowList));
            Double highEntropy = calcEntropy(calcCountList(highList));
            Double gain = initEntropy;
            gain -= 1. * lowList.size() / data.size() * lowEntropy;
            gain -= 1. * highList.size() / data.size() * highEntropy;
            List<Integer> dList = new ArrayList<>();
            dList.add(lowList.size());
            dList.add(highList.size());
            if (maxGain == null || maxGain < gain) {
                maxGain = gain;
                gainRatio = gain / calcEntropy(dList);
                Integer i1 = Integer.valueOf(currentRowValues.getAttributeValue(featureIndex));
                Integer i2 = Integer.valueOf(nextRowValues.getAttributeValue(featureIndex));
                splitPoint = 1. * (i1 + i2) / 2;
            }
        }
        ArrayList<Double> result = new ArrayList<>();
        result.add(gainRatio);
        result.add(splitPoint);
        return result;
    }

    public String predict(RowValues rowValues) {
        DecisionTreeNode node = root;
        while (node != null) {
            String label = node.getLabel();
            if (label != null) {
                return label;
            }
            Integer featureIndex = node.getFeatureIndex();
            Double splitPoint = node.getSplitPoint();
            Integer v = Integer.valueOf(rowValues.getAttributeValue(featureIndex));
            if (v < splitPoint) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        return null;
    }

}
