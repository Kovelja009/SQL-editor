package execute.data;

import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

@Getter
public class Keywords {
    private static Keywords instance = null;
    private Map<Integer, String> keywords;

    private Keywords(){
        keywords = new TreeMap<>();
        keywords.put(1, "select");
        keywords.put(2, "from");
        keywords.put(3, "where");
        keywords.put(4, "group by");
        keywords.put(5, "having");
        keywords.put(6, "order by");
    }

    public static Keywords getInstance(){
        if(instance == null){
            instance = new Keywords();
        }
        return instance;
    }

    public int getPriority(String keyword){
        for(Integer prior : keywords.keySet()){
            if(keywords.get(prior).equalsIgnoreCase(keyword))
                return prior;
        }
        System.out.println("getPriority in Kwywords error!");
        return -1;
    }
}
