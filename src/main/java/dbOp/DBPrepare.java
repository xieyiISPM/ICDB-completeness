package dbOp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DBPrepare {
    private String COMMA_DELIMITER =",";
    private String NEWLINE_SEPARATOR ="\n";

    public DBPrepare(){

    }


    public void writeCSVFile(ArrayList<ArrayList> tupleList, String fileName){
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


}
