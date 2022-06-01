package execute.implementation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import execute.Execute;
import gui.MainFrame;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrettyExecute implements Execute {
    public static List<String> keyWords;
    public static List<Character> characters;

    public PrettyExecute(){
        keyWords = new ArrayList<>();
        characters = new ArrayList<>();
        makeKeyWords();
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
    }

    @Override
    public void execute(Object data) {
        String text = (String) data;
        int tmpI = 0;
        StyledDocument doc = MainFrame.getInstance().getSqlEditor().getStyledDocument();
        Style style = MainFrame.getInstance().getSqlEditor().addStyle("", null);
        Style basicStyle = MainFrame.getInstance().getSqlEditor().addStyle("", null);
        StyleConstants.setForeground(style, Color.BLUE);
        StyleConstants.setForeground(basicStyle, Color.BLACK);
        MainFrame.getInstance().getSqlEditor().setText("");

        try{
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
