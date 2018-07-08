import java.io.Serializable;
import java.util.List;

public class RowValues implements Serializable {
    private List<Attribute> attributeList;

    public String getAttributeValue(int index) {
        return attributeList.get(index).getValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Attribute attribute : attributeList) {
            sb.append(attribute.toString());
            sb.append(",");
        }
        if (attributeList.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }
}
