public class DecisionTreeNode {

    private Integer featureIndex;
    private Double splitPoint;
    private DecisionTreeNode left;
    private DecisionTreeNode right;
    private String label;
    private Integer leafCount;
    private Integer instanceCount;
    private Integer errorCount;
    private Values trainValues;


    public Double getErrorRatio() {
        return 1.*(errorCount + 0.5 * leafCount)/instanceCount;
    }

    public Integer getFeatureIndex() {
        return featureIndex;
    }

    public void setFeatureIndex(Integer featureIndex) {
        this.featureIndex = featureIndex;
    }

    public Double getSplitPoint() {
        return splitPoint;
    }

    public void setSplitPoint(Double splitPoint) {
        this.splitPoint = splitPoint;
    }

    public DecisionTreeNode getLeft() {
        return left;
    }

    public void setLeft(DecisionTreeNode left) {
        this.left = left;
    }

    public DecisionTreeNode getRight() {
        return right;
    }

    public void setRight(DecisionTreeNode right) {
        this.right = right;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getInstanceCount() {
        assert instanceCount != null;
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Values getTrainValues() {
        return trainValues;
    }

    public void setTrainValues(Values trainValues) {
        this.trainValues = trainValues;
    }

    public Integer getLeafCount() {
        return this.leafCount;
    }

    public void setLeafCount(Integer leafCount) {
        this.leafCount = leafCount;
    }
}
