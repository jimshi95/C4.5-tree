import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Values {

    private HashMap<String, Integer> attribute2Index;

    private ArrayList<Integer> featureIndexList;

    private String target;

    private ArrayList<RowValues> data;

    @Override
    public String toString() {
        return "{Values:" + data.size() + "}\n";
    }

    public Integer getTargetIndex() {
        return attribute2Index.get(target);
    }

    public ArrayList<String> getAttributeValues(String attribute) {
        ArrayList<String> result = new ArrayList<>();
        for(RowValues rowValues: data) {
            result.add(rowValues.getAttributeValue(attribute2Index.get(attribute)));
        }
        return result;
    }

    public static Values copyAllInsteadOfData(Values values) {
        Values newValues = new Values();
        newValues.setAttribute2Index(values.attribute2Index);
        newValues.setTarget(values.target);
        newValues.getFeatureIndexList();
        return newValues;
    }

    public ArrayList<Integer> getFeatureIndexList() {
        if (featureIndexList != null) {
            return featureIndexList;
        }
        featureIndexList = new ArrayList<>();
        Set<String> keySet = attribute2Index.keySet();
        for(String key: keySet) {
            if (!key.equals(target)) {
                featureIndexList.add(attribute2Index.get(key));
            }
        }
        return featureIndexList;
    }

    public ArrayList<RowValues> getData() {
        return data;
    }

    public void setData(ArrayList<RowValues> data) {
        this.data = data;
    }

    public HashMap<String, Integer> getAttribute2Index() {
        return attribute2Index;
    }

    public void setAttribute2Index(HashMap<String, Integer> attribute2Index) {
        this.attribute2Index = attribute2Index;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
