package execute.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
public class Keywords {
    private static Keywords instance = null;
    private Map<Integer, String> keywords;
    private List<String> keyWords;
    private List<Character> characters;
    private List<Character> specialCharacters;

    private Keywords(){
        keywords = new TreeMap<>();
        keywords.put(1, "select");
        keywords.put(2, "from");
        keywords.put(3, "where");
        keywords.put(4, "group by");
        keywords.put(5, "having");
        keywords.put(6, "order by");

        specialCharacters = new ArrayList<>();
        specialCharacters.add('=');
        specialCharacters.add('(');
        specialCharacters.add(')');
        specialCharacters.add('>');
        specialCharacters.add('<');
        specialCharacters.add('*');


        keyWords = new ArrayList<>();
        characters = new ArrayList<>();
        makeKeyWords();
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

    void makeKeyWords(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode node = (ObjectNode)mapper.readTree(new File("src\\main\\java\\jsonFiles\\keywords.json"));
            JsonNode arrayNode = node.get("data");
            keyWords = mapper.readValue(arrayNode.traverse(), new TypeReference<ArrayList<String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        characters.add(' ');
        characters.add('(');
        characters.add(')');
        characters.add(',');
        characters.add(':');
        characters.add(';');
        characters.add('\n');
        characters.add('\r');
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public Map<Integer, String> getKeywords() {
        return keywords;
    }
}
