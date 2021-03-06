import java.io.*;
import java.util.ArrayList;
import java.lang.reflect.Array;
  
  
/**
 * Created by Gorman on 2015-11-23.
 */
  
  
public class Driver {
  
    private static Parser parser = null;
      
    public static void main (String [] args) throws Exception
    {
        //load the input data from the input file
        //String fileName = "C:\\Users\\Jason Schneider\\workspace\\433_Project\\src\\exampleLong2.txt";
        //String fileName = "example.txt";
        //args[1] = fileName;
  
        String fileName = null;
  
        if(args.length < 2)
        {
            System.out.println("Suggest Usage: -f <input.txt> -wmf <1.0> -wpr <1.0> -wpa <1.0> -wsd <1.0> -pcm <1.0> -plm <1.0> -ppa <1.0> -psd <1.0> ");
            System.exit(0);
        }
        //java Driver -f example.txt -wmf 1.0 -wpr 1.0 -wpa 1.0 -wsd 1.0 -pcm 1.0 -plm 1.0 -ppa 1.0 -psd 1.0
        else
        {
            //parser.parsePenalties(args);
            for(int i = 0; i < args.length; i+= 2)
            {
                if(args[i].equals("-f")) {
                    fileName = args[i + 1];
                    break;
                }
                else
                {
                    System.out.println("You didn't enter a file!");
                    System.out.println("Usage: file <filename> <penalty name> <penalty weight> <penalty name> <penalty weight> <penalty name> <penalty weight> <penalty name> <penalty weight> ");
                    System.exit(0);
                }
            }
        }
  
        loader(fileName);
  
        if(args.length > 2) {
            for(int i = 0; i < args.length; i++) {
                if (args[i].equals("-pcm"))
                    parser.courseMinPenalty = Double.parseDouble(args[i + 1]);
                else if (args[i].equals("-plm"))
                    parser.labMinPenalty = Double.parseDouble(args[i + 1]);
                else if (args[i].equals("-psd"))
                    parser.sectionDiffPenalty = Double.parseDouble(args[i + 1]);
                else if (args[i].equals("-ppa"))
                    parser.notPairPenalty = Double.parseDouble(args[i + 1]);
                else if(args[i].equals("-wmf"))
                    parser.w_minfilled = Double.parseDouble(args[i + 1]);
                else if (args[i].equals("-wpr"))
                    parser.w_pref = Double.parseDouble(args[i+1]);
                else if(args[i].equals("-wpa"))
                    parser.w_pair = Double.parseDouble(args[i+1]);
                else if(args[i].equals("-wsd"))
                    parser.w_secdif = Double.parseDouble(args[i+1]);
            }
        }
        else
        {
            System.out.println("Default penalties have been set");
        }
  
        System.out.println(parser.courseMinPenalty);
        System.out.println(parser.labMinPenalty);
        System.out.println(parser.sectionDiffPenalty);
        System.out.println(parser.notPairPenalty);
  
        SearchControl hq = new SearchControl(parser);
        Schedule result = hq.begin(); 
        //Schedule result = null;
          
        //creates a single valid schedule, use for testing output if the main loop doesn't work
       /* Schedule test = hq.test();
        System.out.println("Test Case: ");
        if (result != null) 
            System.out.println(test);
        else
            System.out.println("Error: No valid schedules found."); */
          
        //testing output
        //System.out.println("Search Eval: " + result.getValue() + ", Test Eval: " + test.getValue());
        if (result != null) 
            System.out.println(result);
        else
            System.out.println("Error: No valid schedules found.");
  
    }//end main
     
/** This Functions handles loading the input file's data
 * 
 * @param fileName name of the file to load data from
 * @throws Exception
 */
    private static void loader(String fileName) throws Exception{
        //run through document parser
        parser = new Parser();
        parser.parseDocument(fileName);
   
        for(int i = 0; i < parser.coursesVector.size(); i++)
        {
            Courses courses = parser.coursesVector.get(i);
   
            String dept = courses.getDepartment();
            String courseNum = courses.getCourseNumber();
            String lecNum = courses.getLectureNumber();
            ArrayList<Integer> nonCompCourse = courses.getNonCompatibleCourses();
            ArrayList<Integer> nonCompLab = courses.getNonCompatibleLabs();
            ArrayList<Integer> unwantedTimeSlot = courses.getUnwanted();
            ArrayList<Integer> prefSlot = courses.getPreference();
            ArrayList<Integer> prefEval = courses.getPrefEval();
            ArrayList<Integer> coursepair = courses.getPairCourse();
            ArrayList<Integer> courselab = courses.getPairLab();
            ArrayList<Integer> partAssign = courses.getPartAssign();
   
            System.out.println("Course: " + dept + " " + courseNum + " LEC " + lecNum + " NONCOMP COURSES " + nonCompCourse + " NONCOMP LABS " + nonCompLab
                    + " UNWANTED TIME SLOT " + unwantedTimeSlot + " PREFERENCE TIME " + prefSlot + prefEval + " PAIR " + coursepair + courselab + " PART ASSIGN " + partAssign);
   
            //System.out.println(courses.getLabIndex().get(0));
            ArrayList<Integer> indexNums = courses.getLabIndex();
            //System.out.println(parser.labsVector.size());
   
            for(int j = 0; j < courses.getLabIndex().size(); j++)
            {
                Integer index = courses.getLabIndex().get(j);
                Labs labs = parser.labsVector.get(index);
                String lDept = labs.getDepartment();
                String lCourseNum = labs.getCourseNumber();
                String lLecNum = labs.getLectureNumber();
                String lLabNum = labs.getLabNumber();
                ArrayList<Integer> nonCompCourses = labs.getNonCompatibleCourses();
                ArrayList<Integer> nonCompLabs = labs.getNonCompatibleLabs();
                ArrayList<Integer> lprefSlot = labs.getPreference();
                ArrayList<Integer> prefEvals = labs.getPrefEval();
                ArrayList<Integer> coursepairs = labs.getPairCourse();
                ArrayList<Integer> courselabs = labs.getPairLab();
                ArrayList<Integer> partAssigns = labs.getPartAssign();
   
                 System.out.println("\tLabs: " + lDept + " " + lCourseNum + " LEC " + lLecNum + " TUT " + lLabNum + " NONCOMP COURSES " + nonCompCourses
                         + " NONLAB COMPS " + nonCompLabs + " PREF " + lprefSlot + prefEvals + " PAIR " + coursepairs + courselabs + " PART ASSIGN " + partAssigns);
            }//end lab for-loop
   
        }//end course for-loop
   
//        for(int i = 0; i < parser.labsVector.size(); i ++)
//        {
//            System.out.println("Lab: " + parser.labsVector.get(i).getCourseNumber());
//        }
          
        /*for (int x = 0; x < parser.coursesVector.size(); x++)
        System.out.println(parser.coursesVector.get(x));
       
    for (int x = 0; x < parser.courseSlotsVector.size(); x++)
        System.out.println(parser.courseSlotsVector.get(x));*/
   
        for(int i =0; i < parser.labSlotsVector.size(); i++)
        {
            //System.out.println(parser.labSlotsVector.get(i).getCoursemax());
        }
   
    }//end loader
}