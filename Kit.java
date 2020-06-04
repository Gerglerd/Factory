package storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Kit {
    private HashMap<String, Integer> status;
    public Kit(String fileName){
        status = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String current_line;
            while ((current_line = reader.readLine()) != null) {
                StringBuilder key = new StringBuilder();
                int position = 0;
                for (int i = 0; i < current_line.length(); i++) {
                    if (current_line.charAt(i) == '=') {
                        position = i;
                        break;
                    } else key.append(current_line.charAt(i));
                }
                StringBuilder value = new StringBuilder();
                for (int i = position + 1; i < current_line.length(); i++)
                    value.append(current_line.charAt(i));
                status.put(key.toString(), Integer.valueOf(value.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getStatus(String stat){
        return status.get(stat);
    }
}
