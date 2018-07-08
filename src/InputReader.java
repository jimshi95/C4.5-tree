import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class InputReader {

    public String readString(String tips) {
        Scanner scan = new Scanner(System.in);
        System.out.print(tips);
        return scan.nextLine();
    }

    public Values readData() throws IOException {
        String fileName = readString("Please print file path:  ");
        Values values = new Values();
        ArrayList<RowValues> data = new ArrayList<>();
        File file = new File(fileName);

        Scanner scan = new Scanner(file);
        String headerLine = scan.nextLine();
        String headers[]  = headerLine.split(",");

        int numAttributes = headers.length;
        HashMap<String, Integer> attribute2Index = new HashMap<>();
        for (int i = 0; i < numAttributes; i++) {
            attribute2Index.put(headers[i], i);
        }
        values.setAttribute2Index(attribute2Index);

        while(scan.hasNextLine()) {
            RowValues rowValues = new RowValues();
            String[] lineData = scan.nextLine().split(",");
            ArrayList<Attribute> attributes = new ArrayList<>();
            for(int i = 0; i < numAttributes; i++) {
                Attribute attribute = new Attribute(headers[i]);
                attribute.setType("real");
                attribute.setValue(lineData[i]);
                attributes.add(attribute);
            }
            rowValues.setAttributeList(attributes);
            data.add(rowValues);
        }
        values.setData(data);
        return values;
    }
}
