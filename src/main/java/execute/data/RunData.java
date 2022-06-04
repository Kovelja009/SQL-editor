package execute.data;

import gui.MainFrame;
import lombok.Getter;
import resources.enums.AttributeType;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class RunData {
    private String queryText;
    private List<Statement> statementList;
    //          alias, table
    private Map<String, String> aliasMap;
    private Map<String, AttributeType> variables;
    boolean isProcedure = false;
    boolean isDeclare = false;


    public RunData(String queryText){
        queryText = trimming(queryText);

        this.queryText = queryText;
        statementList = new ArrayList<>();
        aliasMap = new HashMap<>();
        variables = new HashMap<>();
        buildStatements();
        checkProcedure(queryText);

        if(isProcedure)
            parseProcedure(queryText);

        if(isDeclare)
            parseDeclare(queryText);

        if(!variables.isEmpty()){
            for(Map.Entry<String, AttributeType> entry : variables.entrySet())
                System.out.println("key: " + entry.getKey() + " values: " + entry.getValue());
        }

    }

    private void buildStatements(){
        boolean isSubquery = false;
        int start = 0;
        String tmp = "";
        for(int i = 0; i < queryText.length(); i++){
            if(queryText.charAt(i) == '(')
                isSubquery = true;
            if(queryText.charAt(i) == ')')
                isSubquery = false;
            tmp = keyCheck(queryText, i);
            if(!tmp.equals("") && !isSubquery){
                if(!queryText.substring(start, i).equals("")){
                    String keyword = keyCheck(queryText, start);

                    String subtext = "";
                    if(!(start + keyword.length() + 1 > i-1))
                        subtext = queryText.substring(start + keyword.length() + 1, i-1);
                    Statement statement = new Statement(keyword, subtext, Keywords.getInstance().getPriority(keyword));
                    statementList.add(statement);
                }
                start = i;
            }
        }

        if(queryText.length() == 0)
            return;
        if(!queryText.substring(start, queryText.length()-1).equals("")){
            String keyword = keyCheck(queryText, start);
            String subtext = "";
            if((start + keyword.length() + 1 < queryText.length()))
                subtext = queryText.substring(start + keyword.length() + 1);
            Statement statement = new Statement(keyword, subtext, Keywords.getInstance().getPriority(keyword));
            statementList.add(statement);
        }

//        for(Statement statement : statementList)
//            System.out.println(statement);
    }

    private String trimming(String queryText){
        queryText = queryText.replaceAll("\r", " ");
        for(int i = queryText.length()-1; i >=0; i--){
            if(queryText.substring(i).equalsIgnoreCase(" ") || queryText.substring(i).equalsIgnoreCase("\n"))
                queryText = queryText.substring(0, i);
            else break;
        }
        queryText = queryText.replaceAll("\n", "\t");
        queryText = queryText.replaceAll("\t", " ");
        queryText = queryText.replaceAll("  ", " ");
        queryText = queryText.replaceAll("   ", " ");
        queryText = queryText.replaceAll("  ", " ");
//        queryText = queryText.replaceAll(";", " ");


        if(queryText.length() == 0){
            System.out.println("prazan");
            return "";
        }
        while(queryText.substring(0,1).equalsIgnoreCase(" "))
            queryText = queryText.substring(1);

        return queryText;
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

    private void checkProcedure(String s){
        List<String> procFun = new ArrayList<>();
        procFun.add("procedure");
        procFun.add("function");

        String[] tokens = s.split(" ");
        for(String token : tokens){
            if( procFun.contains(token.toLowerCase()))
                isProcedure = true;
            if(token.toLowerCase().equalsIgnoreCase("declare"))
                isDeclare = true;
        }
    }

    private void parseProcedure(String text){
        int zagrada = 0;

        String data = "";
        boolean first = true;
        int start = 0;
        int end = 0;
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == '('){
                if(first){
                    start = i + 1;
                    first = false;
                }
                zagrada++;
            }
            if(text.charAt(i) == ')'){
                zagrada--;
                if(zagrada == 0){
                    end = i;
                    break;
                }
            }
        }
        extrapolateData(text.substring(start, end), ",");

        int startAs = 0;
        int endAs = 0;
        for(int i = 0; i< text.length() - 7; i++){
            if(text.substring(i, i+2).equalsIgnoreCase("as") && (i==0 || Keywords.getInstance().getCharacters().contains(text.charAt(i-1))) && Keywords.getInstance().getCharacters().contains(text.charAt(i+2))){
                startAs = i+2;
            }
            if(text.substring(i,i+5).equalsIgnoreCase("begin") && (i==0 || Keywords.getInstance().getCharacters().contains(text.charAt(i-1))) && Keywords.getInstance().getCharacters().contains(text.charAt(i+5))){
                endAs = i;
            }
        }

        extrapolateData(text.substring(startAs, endAs), ";");

        System.out.println("////////////////////////////");
//        System.out.println(text.substring(startAs, endAs));
        for(Map.Entry<String, AttributeType> entry : variables.entrySet())
            System.out.println(entry.getKey() + " " + entry.getValue());

        System.out.println("////////////////////////////");

    }

    private void parseDeclare(String text){
        int start = 0;
        int end = 0;
        for(int i = 0; i< text.length() - 10; i++){
            if(text.substring(i, i+7).equalsIgnoreCase("declare") && (i==0 || Keywords.getInstance().getCharacters().contains(text.charAt(i-1))) && Keywords.getInstance().getCharacters().contains(text.charAt(i+7))){
                start = i+7;
            }
            if(text.substring(i,i+5).equalsIgnoreCase("begin") && (i==0 || Keywords.getInstance().getCharacters().contains(text.charAt(i-1))) && Keywords.getInstance().getCharacters().contains(text.charAt(i+5))){
                end = i;
            }
        }
        extrapolateData(text.substring(start, end), ";");

    }

    private String clear_data(String data){
        for(int i = data.length()-1; i >=0; i--){
            if(data.substring(i).equalsIgnoreCase(" ") || data.substring(i).equalsIgnoreCase("\n"))
                data = data.substring(0, i);
            else break;
        }

        while(data.length() > 0 && data.substring(0,1).equalsIgnoreCase(" "))
            data = data.substring(1);

        return data;
    }

    private AttributeType table_extra(String table){
        String[] tokens = table.split("\\.");
        String tbl = "";
        String att = "";

        if(tokens.length == 3){
            tbl = tokens[1];
            att = tokens[2];
        }
        if(tokens.length == 2){
            tbl = tokens[0];
            att = tokens[1];
        }
        return MainFrame.getInstance().getAppCore().getDatabase().getAttributeType(tbl, att);
    }

    private AttributeType getAttType(String type){
        if(type.contains("%type")){
            int end = type.indexOf("%type");
            String table = type.substring(0, end);
//            System.out.println(type.substring(0, end));
            return table_extra(table);
        }else{
            String tmp = "";
            for(int i = 0; i < type.length(); i++){
                if(type.charAt(i) == '(' || type.charAt(i) == ')' ||(type.charAt(i) >= '0' && type.charAt(i) <= '9'))
                    continue;
                tmp += String.valueOf(type.charAt(i));
            }
            type = tmp;
            return AttributeType.valueOf(
                    Arrays.stream(type.toUpperCase().split(" "))
                            .collect(Collectors.joining("_")));

        }
    }

    void extrapolateData(String data, String regex){
        String[] tokens = data.split(regex);

        for(String token : tokens){
            token = clear_data(token);
            System.out.println("[" + token + "]");
            String[] words = token.split(" ");
            String variable = words[0];
            if(words.length == 2)
                variables.put(variable, getAttType(words[1]));
            if(words.length == 3)
                variables.put(variable, getAttType(words[2]));
            if(words.length > 3)
                variables.put(variable, getAttType(words[1]));
        }
//        for(Map.Entry<String, AttributeType> entry : variables.entrySet()){
//            System.out.println("var: " + entry.getKey() + " type: " + entry.getValue());
//        }
    }


    //    private void buildAliasMapKeys(String text, boolean isSelect){
//        if(!isSelect){
//            System.out.println("''''''''''''''''''''''''''''''''''");
//            String[] data = text.split(" ");
//            int cnt = 0;
//            String value = "";
//            String key = "";
//            boolean as = false;
//            for(String str : data){
//                if(!checkingIfAgregate(str, 3) && !str.equalsIgnoreCase("on")){
//                    if(Keywords.getInstance().getSpecialCharacters().contains(str.charAt(0)) && str.length() == 1)
//                        continue;
//                    if(str.contains("."))
//                        continue;
//                    if(cnt == 0){
//                        value = str;
//                    }
//                    if(str.equalsIgnoreCase("as")){
//                        as = true;
//                        continue;
//                    }
//                    cnt++;
//                    System.out.println(str + "counter = " + cnt);
//                    if(cnt > 0){
//                        if(cnt > 1 && !as)
//                            key += " "+str;
//                        else
//                            key += str;
//                    }
//                }else{
//                    if(!key.equals("") && cnt > 1){
//                        aliasMap.put(key, value);
//                        System.out.println("key:" + key +" postao:"+value);
//
//                    }
//                    cnt = 0;
//                    value = "";
//                    key = "";
//                }
//            }
////            if(cnt > 0){
////                if(!key.equals(""))
////                    aliasMap.put(key,value);
////            }
//            System.out.println(queryText);
//            System.out.println("''''''''''''''''''''''''''''''''''");
///////////////////////////////////////////////////////////////////////////
////            for(int i = 0; i < text.length(); i++){
////                if(text.charAt(i) == '.'){
////                    boolean isMultpiple=false;
////                    int j;
////                    for(j = i - 1; j >= 0; j--){
////                        if(j == i - 1 && text.charAt(j) == '"')
////                            isMultpiple = true;
////                        if(text.charAt(j) == ' ' && !isMultpiple){
////                            j++;
////                            break;
////                        }
////                        if(text.charAt(j) == '"' && isMultpiple && j != i-1) {
////                            break;
////                        }
////                        if(!isMultpiple && Keywords.getInstance().getSpecialCharacters().contains(text.charAt(j))){
////                            j++;
////                            break;
////                        }
////                    }
////                    if(j < 0)
////                        j = 0;
////                    String alias1 = text.substring(j,i);
////                    aliasMap.put(alias1, "");
//////                    System.out.println("kluc: "+alias1);
////                }
////            }
//        }
////        if(isSelect){
////            String[] tokens = text.split(",");
////            for(String token : tokens){
//////                System.out.println(token);
////                int cnt = 0;
////                String alijas = "";
////                String[] words = token.split(" ");
////                for(String wrd : words){
////                    wrd = wrd.replaceAll(" ","");
////                    if(wrd.equals(""))
////                        continue;
////                    if(!checkingIfAgregate(wrd, 3)){
////                        if(wrd.equalsIgnoreCase("as")){
////                            cnt++;
////                            continue;
////                        }
////                        cnt++;
////                        if(cnt > 1){
////                            if(cnt > 2)
////                                alijas+=" "+wrd;
////                            else
////                                alijas+=wrd;
////                        }
////                    }
////
////                }
////                if(alijas.length() > 0){
////                    if(alijas.charAt(0) == ' ')
////                        alijas = alijas.substring(1);
////                    aliasMap.put(alijas, "select");
////                }
////            }
////
////
////        }
//    }

//    private boolean checkingIfAgregate(String txt, int num){
//        for(String str : Keywords.getInstance().getKeyWords())
//            if(str.toUpperCase().contains(txt.toUpperCase()) && str.length() >= num){
//                return true;
//            }
//        return false;
//    }

//    private String trim(String txt){
//        if(txt.length() == 1)
//            return txt;
//
//        int i;
//        for(i = 0; i < txt.length(); i++){
//            if(!Keywords.getInstance().getSpecialCharacters().contains(txt.charAt(i)))
//                break;
//        }
//        if(i >= txt.length() - 1){
//            System.out.println("eeeeeeeee pazi sta pises ako specijalne karaktere trim u runData");
//            return "";
//        }
//        int j = txt.length() - 1;
//        while(Keywords.getInstance().getSpecialCharacters().contains(txt.charAt(j)))
//            j--;
//        if(j <= i){
//            System.out.println("eeeeeeeee pazi sta pises ako specijalne karaktere trim j <= i u runData");
//            return "";
//        }
//        return txt.substring(i, j+1);
//    }
//
//    private void buildAliasMapValues(String text){
////        String[] tokens = text.split(" ");
////        boolean isCandidate = false;
////        String value = "";
////        boolean isMultiple = false;
////        String key = "";
////        for(String token : tokens){
////            token = trim(token);
//////            System.out.println(token);
////            if(token.contains(".")){
////                isCandidate = false;
////                continue;
////            }
////
////            if(token.contains("\"") && !isMultiple){
////                isMultiple = true;
////                isCandidate = true;
////                key =  token;
////                continue;
////            }
////
////            if(!token.contains("\"") && isMultiple){
////                key+=" " + token;
////                continue;
////            }
////
////            if(token.contains("\"") && isMultiple){
////                isMultiple = false;
////                key+=" " + token;
////                if(isCandidate){
////                    settingMap(key, value);
////                }
////                    isCandidate = false;
////                continue;
////            }
////
////            if(token.equalsIgnoreCase("as")){
////                isCandidate = true;
////                continue;
////            }
////            if(token.length() == 1){
////                char tmp = token.charAt(0);
////                if(Keywords.getInstance().getSpecialCharacters().contains(tmp) || Keywords.getInstance().getKeyWords().contains(String.valueOf(tmp))){
////                    isCandidate = false;
////                }
////                else{
////                    if(isCandidate){
////                        isCandidate = false;
////                        settingMap(String.valueOf(tmp), value);
////                    }else{
////                        isCandidate = true;
////                        value = String.valueOf(tmp);
////                    }
////
////                }
////            }else{
////                if(Keywords.getInstance().getKeyWords().contains(token.toUpperCase()))
////                    isCandidate = false;
////                else{
////                    if(isCandidate){
////                        isCandidate = false;
////                        settingMap(token, value);
////                        value = token;
////                    }else{
////                        isCandidate = true;
////                        value = token;
////                    }
////                }
////
////            }
////        }
//    }
//
//    private void settingMap(String key, String value){
////        System.out.println("Kljuc: " + key + "Vrednost: " + value);
//        aliasMap.put(key, value);
//    }
}
