package execute.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RunData {
    private String queryText;
    private List<Statement> statementList;

    public RunData(String queryText){
        this.queryText = queryText;
        statementList = new ArrayList<>();
        buildStatements();
    }

    private void buildStatements(){
        boolean isSubquery = false;
        int start = 0;
        for(int i = 0; i < queryText.length(); i++){
            if(queryText.charAt(i) == '(')
                isSubquery = true;
            if(queryText.charAt(i) == ')')
                isSubquery = false;
            String tmp = keyCheck(queryText, i);
            if(!tmp.equals("") && !isSubquery){
                if(!queryText.substring(start, i).equals("")){
                    String keyword = keyCheck(queryText, start);
                    String subtext = queryText.substring(start + keyword.length() + 1, i);
                    Statement statement = new Statement(keyword, subtext, Keywords.getInstance().getPriority(keyword));
//                    System.out.println(queryText.substring(start, i) + "->" + statement.toString());
                    statementList.add(statement);
                }
                start = i;
            }
        }
    }

    private String keyCheck(String text, int start){
        for(String word : Keywords.getInstance().getKeywords().values()){
            if(particular(text,start,word,word.length()))
                return word;
        }
        return "";
    }

    private boolean particular(String text, int start, String word, int size){
        if(start + size <= text.length()){
            String tmp = text.substring(start, start + size);
            return tmp.equalsIgnoreCase(word);
        }
        return false;
    }
}
