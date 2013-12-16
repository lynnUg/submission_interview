
import java.io.*;
import java.util.Scanner;
import java.util.regex.*;

public class Question2a
{
    public static void main(String[] args)
    {
   
        File f = new File(args[0]);
        String search_text = args[1];
        System.out.printf("Result of searching for %s in %s was %d\n", search_text, f.getName(), Question2a.find(f, search_text));
    }

    public static int find(File f, String searchString)
    {
        int result = 0;
        Scanner in = null;
        try 
        {
            in = new Scanner(new FileReader(f));
            Pattern p=Pattern.compile(searchString);
            while(in.hasNextLine() ) 
            {
            	 Matcher m=p.matcher(in.nextLine());
                 while(m.find())
                 {
                	 result++;
                 }
                //System.out.print (result);
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
        return result;
    }
  }