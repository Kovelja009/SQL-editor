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
    private List<String> aggregateFunctions = new ArrayList<>();

    private Keywords(){
        keywords = new TreeMap<>();
        keywords.put(1, "select");
        keywords.put(2, "from");
        keywords.put(3, "where");
        keywords.put(4, "group by");
        keywords.put(5, "having");
        keywords.put(6, "order by");

        keywords.put(-2, "update");
        keywords.put(-1, "set");
        keywords.put(-4, "insert");
        keywords.put(-3, "into");
        keywords.put(-5, "delete");
        keywords.put(-6, "exec");

        specialCharacters = new ArrayList<>();
        specialCharacters.add('=');
        specialCharacters.add('(');
        specialCharacters.add(')');
        specialCharacters.add('>');
        specialCharacters.add('<');
        specialCharacters.add('*');

        aggregateFunctions.add("min");
        aggregateFunctions.add("max");
        aggregateFunctions.add("avg");
        aggregateFunctions.add("count");
        aggregateFunctions.add("sum");


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

    public boolean isAggreateFunction(String s) {
        s = s.strip();

        int depth = 0;
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) =='(') depth++;
            if(s.charAt(i) == ')') depth--;
            for(String func: aggregateFunctions) {
                int len = func.length();
                if(depth == 0 && s.substring(i, Math.min(s.length(), i+len)).equalsIgnoreCase(func) && okPreviousChar(s, i) && okNextChar(s, i+len)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean okPreviousChar(String s, int pos) {
        return pos - 1 < 0 || s.charAt(pos-1) == ' ' || s.charAt(pos-1) == '\n' || !Character.isLetterOrDigit(s.charAt(pos-1));
    }

    private static boolean okNextChar(String s, int pos) {
        while(pos < s.length()) {
            if(s.charAt(pos) == '(')
                return true;
            if(s.charAt(pos) != ' ')
                return false;
            pos++;
        }
        return false;
    }
}
