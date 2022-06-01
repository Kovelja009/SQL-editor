package interfaces.functionalities;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonParser;
import gui.MainFrame;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Pretty {
    public static List<String> keyWords = new ArrayList<>();
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

        characters.add(' ');
        characters.add('(');
        characters.add(')');
        characters.add(',');
        characters.add(':');
        characters.add(';');
        characters.add('\n');
    }

    public static void makePretty(String text) throws BadLocationException {
        if(!listmade){
            listmade = true;
            makeKeyWords();
        }

        int tmpI = 0;
        StyledDocument doc = MainFrame.getInstance().getSqlEditor().getStyledDocument();
        Style style = MainFrame.getInstance().getSqlEditor().addStyle("", null);
        Style basicStyle = MainFrame.getInstance().getSqlEditor().addStyle("", null);
        StyleConstants.setForeground(style, Color.BLUE);
        StyleConstants.setForeground(basicStyle, Color.BLACK);
        MainFrame.getInstance().getSqlEditor().setText("");


        for(int i = 0; i< text.length(); i++) {
            for (int j = text.length(); j > i; j--) {
                if (keyWords.contains(text.substring(i, j).toUpperCase()) && (i == 0 || characters.contains(text.charAt(i-1))) &&
                        (j == text.length() || !(text.charAt(j)>64 && text.charAt(j)<91) && !(text.charAt(j)>96 && text.charAt(j)<123))) {
                    doc.insertString(doc.getLength(), text.substring(tmpI, i), basicStyle);
                    if(i > 0 && text.charAt(i-1) != '\n')
                        doc.insertString(doc.getLength(), "\n", basicStyle);
                    doc.insertString(doc.getLength(), text.substring(i, j).toUpperCase(), style);
                    tmpI = j;
                    i = j;
                }
            }
        }
        if(tmpI != text.length())
            doc.insertString(doc.getLength(), text.substring(tmpI), basicStyle);

//        String[] words = text.split("[ ();]");
//        boolean first = true;
//        for(String word:words){
//            if(keyWords.contains(word.toUpperCase())){
//                if(!first) System.out.print("\n");
//                System.out.print(TEXT_CYAN + word.toUpperCase() + TEXT_RESET);
//            }
//            else System.out.print(" " + word);
//            first = false;
    }


    public static List<String> getKeyWords() {
        return keyWords;
    }

    public static void setKeyWords(List<String> keyWords) {
        Pretty.keyWords = keyWords;
    }

    public static List<Character> getCharacters() {
        return characters;
    }

    public static void setCharacters(List<Character> characters) {
        Pretty.characters = characters;
    }
}
