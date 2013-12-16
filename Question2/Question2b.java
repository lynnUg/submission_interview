import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Question2b 
{
	public static void main(String[] args) throws IOException {

	    Map<String ,Integer> wordcount = new HashMap<String ,Integer>();

	    File file = new File(args[0]);   
	    Scanner in = null;
	    try 
        {
            in = new Scanner(new FileReader(file));
            while(in.hasNextLine() ) 
            {
            	String[] words = in.nextLine().replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            	
            	for (String s : words) {

        	       if(wordcount.containsKey(s))
        	       {
        	    	   wordcount.put(s,wordcount.get(s)+1);   
        	       }
        	       else
        	       {
        	          wordcount.put(s, 1);
        	       }

        	    }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();      
        }
        finally
        {
            try { in.close() ; } catch(Exception e) { /* ignore */ }  
        }
	    
	        
	    for (Map.Entry<String, Integer> entry:wordcount.entrySet()) {

	        System.out.println(entry.getKey() + ": " + entry.getValue());
	    }

	}
	

}

