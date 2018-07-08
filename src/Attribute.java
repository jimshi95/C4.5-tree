public class Attribute {
    public Attribute(String name) {
        this.name = name;
    }

    private String name;
    private String type;
    private String value;

    @Override
    public String toString() {
        return this.value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
