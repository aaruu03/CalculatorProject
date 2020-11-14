/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ParkerC
 */
    public class CalculatorProject
    {    
        /**
         * @param args the command line arguments
         */
        public static final String[] twoOperands = new String[] {"+", "-", "*", "/", "^", "%", "p", "c"};
        public static final String[] oneOperands = new String[] {"|", "v", "~", "sin", "cos", "tan"};
        public static void main(String[] args) throws FileNotFoundException
        {

            System.out.println("Welcome to calc!\nAt any time enter \"quit\" to exit");
            // add a welcome message here

                                    // comment out either startCalc() or testCalc() (just the method call below) 
                                    // based on how you want to run the project:
                                    // uncomment startCalc() to call your method for an interactive calculator
            // uncomment testCalc() to try out a bunch of 

            startCalc();   // you have to write this method below
                            // it should ask the user for input and print
                            // results until the user enters "quit" to stop


            // use this to validate your project (the calculator part, anyways)
            testCalc();     // testCalc will call a calculate(String s) method that you create
                            // as part of your overall project. This method will take the user's
                            // input, and return a String with the appropriate output.

            // add a goodbye message here
            System.out.println("Goodbye!");

        }

        public static void startCalc(){
            Scanner kb = new Scanner(System.in);
            while(true) {
                System.out.print("Enter your equation here:\n> ");
                String equation = kb.nextLine(); //grab user input
                if(equation.trim().equals("")){
                    System.out.println("ERROR\n");
                    continue;
                }
                String a = calculate(equation);
                
                if(a.equals("quit")) //if calculate returns quit then break the loop and finish code through main
                    break;
                else if(a.equals("help")){
                    help();
                    continue;
                }
                System.out.println(a + "\n");
            }
        }

        public static String calculate(String s){
            // you add code here to take a String as a parameter, and return a String with the result
            if(s.trim().toLowerCase().equals("quit")) //if quit is entered at any time it exits
                return "quit";
            if(s.trim().toLowerCase().equals("help"))
                return "help";

            ArrayList<String> equation = new ArrayList<>();
            equation = check(s);
            equation.add("Z");                              //filler to prevent overflow index crashing
            if(equation.get(0).equals("ERROR"))
                return "ERROR";
            String result = Double.toString(allTwoOp(removeOneOp(equation)));
                                                            //first removes one operands, then two operands
            return result;
        }
        
        public static ArrayList<String> check(String s) {
            Scanner equ = new Scanner(s);
            ArrayList<String> equation = new ArrayList<>(); //turn the string equation into an array list
            while(equ.hasNext())
                equation.add(equ.next());
            Scanner parse = new Scanner(s);
            
            int index = 0; //keeps track of indexes of token
            while(parse.hasNext()){
                String nextOperand = parse.next(); //nextOperand is used to check if it's allowed or not
                Scanner fill = new Scanner(nextOperand); //to check if double later
                boolean nextOneOp = false;                  
                for (String oneOperand : oneOperands){      //check if it's one operand operand
                    if(nextOperand.toLowerCase().equals(oneOperand)){     
                        nextOneOp = true;
                        break;
                    }
                }                                           
                boolean nextTwoOp = false;
                for(String twoOperand : twoOperands){       //check if it's two operand operand
                    if(nextOperand.toLowerCase().equals(twoOperand)){
                        nextTwoOp = true;
                        break;
                    }
                }
                if(index == 0)                              //if it's first index, cannot be a two operand operand
                    nextTwoOp = false;
                
                if(nextOneOp == true)
                    if(parse.hasNextDouble() != true)
                        equation.set(0, "ERROR");                     //Must be a number following a one operand
                if(nextTwoOp == true)
                    if(parse.hasNextDouble() != true)
                        equation.set(0, "ERROR");                    //Must be a number following a two operand
                
                if(nextOneOp == false && nextTwoOp == false && fill.hasNextDouble() == false){
                    equation.set(0, "ERROR");                         //if not double nor one op, should be error
                }
                index++;
            }
            if(index == 1)
                equation.set(0, "ERROR");
            
            return equation;
        }
        
        public static ArrayList<String> removeOneOp(ArrayList<String> equation)
        {
            for (int i = 0; i < equation.size() - 1; i++)
            {
                Scanner number = new Scanner(equation.get(i + 1)); //Take double from string following the one op
                if(number.hasNextDouble()){
                    switch (equation.get(i)){
                        case "|":
                            equation.set(i, Double.toString(Math.abs(number.nextDouble()))); //
                            equation.remove(i + 1);
                            break;
                        case "v":
                            equation.set(i, Double.toString(Math.sqrt(number.nextDouble())));
                            equation.remove(i + 1);
                            break;
                        case "~":
                            equation.set(i, Double.toString(Math.round(number.nextDouble())));
                            equation.remove(i + 1);
                            break;
                        case "sin":
                            equation.set(i, Double.toString(Math.sin(number.nextDouble())));
                            equation.remove(i + 1);
                            break;
                        case "cos":
                            equation.set(i, Double.toString(Math.cos(number.nextDouble())));
                            equation.remove(i + 1);
                            break;
                        case "tan":
                            equation.set(i, Double.toString(Math.tan(number.nextDouble())));
                            equation.remove(i + 1);
                            break;
                    }
                }
            }
            return equation;
        }
        
        public static double allTwoOp(ArrayList<String> equation)
        {
            ArrayList<Double> numbers = new ArrayList<>();
                       
            for (int i = 0; i < equation.size(); i++)
            {
                Scanner temp = new Scanner(equation.get(i));
                if(temp.hasNextDouble()){
                    numbers.add(temp.nextDouble());
                }
                else
                    temp.next();
            }
            
            for(int i = 0; i < equation.size(); i++){
                while(equation.get(i).equals("%")){
                    numbers.set(i/2, numbers.get(i/2) % numbers.get(i/2 + 1));
                    numbers.remove(i/2 + 1);
                    equation.remove(i);
                    equation.remove(i);
                }
            }
            for(int i = 0; i < equation.size(); i++){
                while(equation.get(i).equals("^")){
                    numbers.set(i/2, Math.pow(numbers.get(i/2), numbers.get(i/2 + 1)));
                    numbers.remove(i/2 + 1);
                    equation.remove(i);
                    equation.remove(i);
                }
            }
            
            for(int i = 0; i < equation.size(); i++){
                while(equation.get(i).toLowerCase().equals("p")){
                    double n = 1;
                    double k = 1;
                    for (int j = 1; j <= Math.round(numbers.get(i/2)); j++)
                        n *= j;
                    for (int j = 1; j <= (Math.round(numbers.get(i/2)) - Math.round(numbers.get((i/2+1)))); j++)
                        k *= j;
                    numbers.set(i/2, n/k);
                    numbers.remove(i/2 + 1);
                    equation.remove(i);
                    equation.remove(i);
                }
            }
            
            for(int i = 0; i < equation.size(); i++){
                while(equation.get(i).toLowerCase().equals("c")){
                    double n = 1;
                    double k = 1;
                    for (int j = 1; j <= Math.round(numbers.get(i/2)); j++)
                        n *= j;
                    for (int j = 1; j <= (Math.round(numbers.get(i/2)) - Math.round(numbers.get((i/2+1)))); j++)
                        k *= j;
                    for (int j = 1; j <= (Math.round(numbers.get(i/2+1))); j++) {
                        k *= j;
                    }
                    numbers.set(i/2, n/k);
                    numbers.remove(i/2 + 1);
                    equation.remove(i);
                    equation.remove(i);
                }
            }
            
            for(int i = 0; i < equation.size(); i++){
                while(equation.get(i).equals("/")){
                    numbers.set(i/2 + 1, 1 / numbers.get(i/2 + 1));
                    equation.set(i, "*");
                    equation.set(i + 1, Double.toString(numbers.get(i/2 + 1)));
                }
            }
            
            for(int i = 0; i < equation.size(); i++){
                while(equation.get(i).equals("*")){
                    numbers.set(i/2, numbers.get(i/2) * numbers.get(i/2 + 1));
                    numbers.remove(i/2 + 1);
                    equation.remove(i);
                    equation.remove(i);
                }
            }
            
            for(int i = 0; i < equation.size(); i++){
                while(equation.get(i).equals("-")){
                    numbers.set(i/2 + 1, -1 * numbers.get(i/2 + 1));
                    equation.set(i, "+");
                    equation.set(i + 1, Double.toString(numbers.get(i/2 + 1)));
                }
            }
            
            for(int i = 0; i < equation.size(); i++){
                while(equation.get(i).equals("+")){
                    numbers.set(i/2, numbers.get(i/2) + numbers.get(i/2 + 1));
                    numbers.remove(i/2 + 1);
                    equation.remove(i);
                    equation.remove(i);
                }
            }
            
            return numbers.get(0); 
        }
        
        public static void help() {
            System.out.println("\nPut a space between each operand/number, everything else will return an error\nParentheses don't work"
                    + "\nOperators:"
                    + "\n\"+\"-----Add"
                    + "\n\"-\"-----Subtract"
                    + "\n\"*\"-----Multiply"
                    + "\n\"/\"-----Divide"
                    + "\n\"^\"-----Exponent"
                    + "\n\"%\"-----Modulus"
                    + "\n\"P\"-----Permutation"
                    + "\n\"c\"-----Combination"
                    + "\n\"|\"-----Absolute value"
                    + "\n\"v\"-----Square root"
                    + "\n\"~\"-----Round"
                    + "\n\"sin\"---Sine"
                    + "\n\"cos\"---Cosine"
                    + "\n\"tan\"---Tangent"
                    + "\n");
        }

                    // add more methods here

                    //
                    // DO NOT MODIFY THE METHOD BELOW!!!
                    // YOU WILL CALL IT FROM MAIN TO TEST OUT YOUR OTHER CODE/METHODS
                    //
        public static void testCalc() throws FileNotFoundException{
            ArrayList<String> problems = new ArrayList<>();
            ArrayList<String> results = new ArrayList<>();
            // load problems from a file
            File fProblems = new File("problems.txt");
            Scanner sc = new Scanner(fProblems);        
            int count = 0;
            String line = "";
            int problemCount = 0;
            int resultCount = 0;
            while (sc.hasNextLine()){
                line = sc.nextLine();
                if (!line.startsWith("//") && !line.trim().equals("")){        // ignore comments at the beginning
                    problems.add(line.substring(3).trim());
                    problemCount++;
                    if (sc.hasNextLine()){
                        line = sc.nextLine();
                        if (!line.startsWith("//") && !line.trim().equals("")){
                            results.add(line.substring(3).trim());
                            resultCount++;
                        }
                    } 
                    count++;
                }
            }
            if (problemCount == resultCount){
                // now run the tests
                for (int i=0; i<problemCount; i++){
                    String prob = problems.get(i);
                    String result = calculate(prob);
                    if (result == null){
                        System.out.println("FAILED test " + i);
                        System.out.println("Expression: " + problems.get(i));
                        System.out.println("Expected result: " + results.get(i));
                        System.out.println("Actual: null String returned from calculate()");
                    } else {
                        if (result.equals(results.get(i))){
                            System.out.println("PASSED test " + i);
                        } else {
                            System.out.println("FAILED test " + i);
                            System.out.println("Expression: " + problems.get(i));
                            System.out.println("Expected result: " + results.get(i));
                            System.out.println("Actual: " + result);
                        }
                    }

                }
            } else {
                System.out.println("problem file error");
            }    

        }



}
