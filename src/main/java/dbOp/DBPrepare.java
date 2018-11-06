package dbOp;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DBPrepare {
    private String COMMA_DELIMITER =",";
    private String NEWLINE_SEPARATOR ="\n";

    public DBPrepare(){

    }


    public void writeCSVFile(ArrayList<ArrayList<String>> tupleList, String fileName){
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(fileName, false);
            for (ArrayList<String> tuple: tupleList){
                for (String field: tuple){
                    fileWriter.append(field);
                    fileWriter.append(COMMA_DELIMITER);
                }
                fileWriter.append(NEWLINE_SEPARATOR);
            }
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Creating .csv file failed");
        }
        finally {
            try{
                fileWriter.flush();
                fileWriter.close();
            }
            catch (IOException e){
                e.printStackTrace();
                System.out.println("Error while flushing/closing FileWriter");
            }
        }

    }

    public ArrayList<ArrayList<String>> readCSVFile(String fileName){
        ArrayList<ArrayList<String>> tupleList = new ArrayList<>();
        try{
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while((line= br.readLine()) != null){
                ArrayList<String> tuple = new ArrayList<> (Arrays.asList(line.split(",")));
                tupleList.add(tuple);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return tupleList;
    }


}
