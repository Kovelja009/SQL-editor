package interfaces.functionalities;

import java.io.File;
import java.io.IOException;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonParser;

public class Pretty {
    public static List<String> keyWords = new ArrayList<>();
    public static List<String> specialKeyWrds = new ArrayList<>();
    public static List<Character> characters = new ArrayList<>();
    static boolean listmade = false;

    static void makeKeyWords(){
        JsonParser json;
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode node = (ObjectNode)mapper.readTree(new File("src\\main\\java\\jsonFiles\\keywords.json"));
            JsonNode arrayNode = node.get("data");
            keyWords = mapper.readValue(arrayNode.traverse(), new TypeReference<ArrayList<String>>(){});


        } catch (IOException e) {
            e.printStackTrace();
        }

        specialKeyWrds.add("SELECT");
        specialKeyWrds.add("WHERE");
        specialKeyWrds.add("FROM");
        specialKeyWrds.add("JOIN");
        specialKeyWrds.add("RIGHT JOIN");
        specialKeyWrds.add("LEFT JOIN");
        specialKeyWrds.add("GROUP BY");
        specialKeyWrds.add("FULL JOIN");
        specialKeyWrds.add("FULL OUTER JOIN");
        specialKeyWrds.add("ORDER BY");
        specialKeyWrds.add("HAVING");

        characters.add(' ');
        characters.add('(');
        characters.add(')');
        characters.add(',');
        characters.add(':');
        characters.add(';');
    }


    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_CYAN = "\u001B[36m";

    public static void makePretty(String text){
        if(!listmade){
            listmade = true;
            makeKeyWords();
        }

        String tmpString = "";
        int tmpI = 0;
        boolean first = true;
        for(int i = 0; i< text.length(); i++) {
            for (int j = text.length() - 1; j > i; j--) {
                if (specialKeyWrds.contains(text.substring(i, j).toUpperCase()) && (i == 0 || characters.contains(text.charAt(i-1))) && characters.contains(text.charAt(j))) {
                    if (first)
                        tmpString = tmpString + text.substring(tmpI, i) + TEXT_CYAN + text.substring(i, j).toUpperCase() + TEXT_RESET;
                    else
                        tmpString = tmpString + text.substring(tmpI, i) + "\n" + TEXT_CYAN + text.substring(i, j).toUpperCase() + TEXT_RESET;
                    tmpI = j;
                    i = j;
                } else if (keyWords.contains(text.substring(i, j).toUpperCase()) && (i == 0 || characters.contains(text.charAt(i-1))) && characters.contains(text.charAt(j))) {
                    tmpString = tmpString + text.substring(tmpI, i) + TEXT_CYAN + text.substring(i, j).toUpperCase() + TEXT_RESET;
                    tmpI = j;
                    i = j;
                }
            }
            first = false;
        }
        if(tmpI != text.length())
            tmpString = tmpString + text.substring(tmpI);
        System.out.println(tmpString);

//        String[] words = text.split("[ ();]");
//        boolean first = true;
//        for(String word:words){
//            if(keyWords.contains(word.toUpperCase())){
//                if(!first) System.out.print("\n");
//                System.out.print(TEXT_CYAN + word.toUpperCase() + TEXT_RESET);
//            }
//            else System.out.print(" " + word);
//            first = false;
//        }

    }
}
