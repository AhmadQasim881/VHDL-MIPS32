/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package com.mycompany.complierdesign;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class Main_form extends javax.swing.JFrame {

    ArrayList<String> tokens;
    ArrayList<Integer> key;
    
    ArrayList<String> sentence;
    ArrayList<Integer> sen_keys;
    String file_data="";
         
    public Main_form() {
        initComponents();
        jTextArea_code.setText("#include<std.uma>\n\nfun main()\n{\n\n}");
        sentence = new ArrayList<>();
        sen_keys = new ArrayList<>();
    }

/******************************************************************
     ******************************************************************
     ***********************Remove comments****************************
     ******************************************************************
     ******************************************************************/
    /**********************************************************************
    *   It's parameter is the code which the coder write, and it returns the
    *   code without comments, we have two type of comments, single line comment
    *   like // and multi line comment like /* */
    /*  For example : 
    *     The parameter is   : fun main() //it's the our main  /* my main */
    /*    The return will be : fun main()  
    **********************************************************************/
    String remove_comments(String C_code)
    {
        int index; /*contains the index of // */
        C_code = C_code + "\n";  /*To avoid exeptions*/
        
        /***** removing //comments *****/
        while(C_code.contains("//"))
        {
            index = C_code.indexOf("//");
            C_code = C_code.substring(0,index) + C_code.substring(C_code.indexOf("\n", index)); /*Will remove // part till endl from code*/           
        }
        
        /********** removing /* comments ***********/
        int sec_index; // index of */
        while( C_code.contains("/*") && C_code.contains("*/") )
        {
            index     = C_code.indexOf("/*");
            sec_index = C_code.indexOf("*/");
            C_code = C_code.substring(0,index) + C_code.substring(sec_index+2); /**/
        }
        return C_code;
    }
    /******************************************************************
     ******************************************************************
     ***********************pre_processor *****************************
     ******************************************************************
     ******************************************************************/
    
    /*******************************************************************
     * This function is the pre_processor function which changes the code 
     * to .pre pure code without any #.
     * It's parameter is the code after removing comments from it and it
     * It's return is the code after handeling all # keywords
     * For example : 
     *   The parameter is  :  #replace SIZE 5 
     *                        #if(SIZE=5)
     *                        int x; x = SIZE;
     *                        #endif
     *  The return will be :  int x; x = 5;
     *******************************************************************/
    String pre_processor(String C_code) /*if there is an error it will return error, else will return the code.pre*/
    {
      try{
        int index,temp; /*contains the index of #*/
        C_code = "\n" + C_code + "\n"; /*To avoid Exceptions*/ 
        while( C_code.contains("#") ) /*Loop to take all # */
        {
            index = C_code.indexOf('#');/*The index of #*/
            String hash_line; /*will contains the line of pre_prossesor*/
            /******************** #replace ****************************/
            if( C_code.substring(index).startsWith("#replace") )
            {
                /*Take the line which starts from # till the endLine */
                try{
                hash_line =  C_code.substring(index, C_code.indexOf("\n", index));
                C_code = C_code.substring(0,index-1) +C_code.substring(C_code.indexOf("\n", index)); /*Will remove #replace part from code*/
                }catch(Exception e) /*no \n in code*/
                {
                    hash_line = C_code.substring(index);
                    C_code = C_code.substring(0,index-1);
                }
                /*Text replacment part */
                /*For example :  #replace SIZE 5 */
                StringTokenizer s1 = new StringTokenizer(hash_line.trim()," ");
                String replace,replaced; /*I will remove all (replace) and put (replaced)*/
                if(s1.countTokens()!=3) /* #replace SIZE 10 ,so it should be 3 tokens*/
                    return "error in #replace number, there is "+s1.countTokens()+" numbers";
                s1.nextToken(); /*first token is #replaced, I don't want it*/
                replace = s1.nextToken().trim(); /*SIZE*/
                replaced = s1.nextToken().trim(); /*5*/
                
                
                C_code=C_code.replaceAll(replace, replaced); /*The final step is the replacment*/
            }
            /************************ #if(...) ****************************/
            else if( C_code.substring(index).startsWith("#if") || C_code.substring(index).startsWith("#IF")  )
             {
                  int bool;/*True or False is #if*/
                  int endif_index = C_code.indexOf("#endif");/*index of #endif*/
                  
                  if(endif_index==-1) /*No #endif*/
                      return "error_no_#endif in code";
                try{ /*Take the codition of #if only without #if*/
                hash_line =  C_code.substring(index+3, C_code.indexOf("\n", index));
                }catch(Exception e) /*no \n in code*/
                {
                    return "error";
                }
                
                hash_line = hash_line.trim();/*To remove spaces*/
                /*It may be #if(SIZE = 5) or #if SIZE=5 */
                if(hash_line.startsWith("(")) /*if it contains (, so we will remove it*/
                    hash_line = hash_line.substring(1,hash_line.length()-1);
                
                 try { /*If it's #if(0) or #if(1) */
                     bool = Integer.parseInt(hash_line); /*if it's a number only*/
                 } catch (Exception e) {
                     /*if it contains > or < or = only,  like #if(SIZE = 5) or #if(SIZE > 9) */
                     int x,y;
               
                    try{
                    if(hash_line.contains("<"))
                    {
                        x = Integer.parseInt( hash_line.substring(0,hash_line.indexOf("<")).trim() );/*change first number to int*/
                        y = Integer.parseInt( hash_line.substring(hash_line.indexOf("<")+1).trim() );/*change second number to int*/
                        if(x<y)
                            bool = 1;
                        else
                            bool = 0; 
                    }else if(hash_line.contains(">"))
                    {
                        x = Integer.parseInt( hash_line.substring(0,hash_line.indexOf(">")).trim() );/*change first number to int*/
                        y = Integer.parseInt( hash_line.substring(hash_line.indexOf(">")+1).trim() );/*change second number to int*/
                        if(x>y)
                            bool = 1;
                        else
                            bool = 0; 
                    }else if(hash_line.contains("="))
                    {
                        x = Integer.parseInt( hash_line.substring(0,hash_line.indexOf("=")).trim() );/*change first number to int*/
                        y = Integer.parseInt( hash_line.substring(hash_line.indexOf("=")+1).trim() );/*change second number to int*/
                        if(x==y)
                            bool = 1;
                        else
                            bool = 0; 
                    }else
                        return "error in #if parameters";
                    }catch(Exception ee)
                    {
                        return "error in #if parameters";
                    }
                        
                 }/*catch bracet*/
                 
                 if(bool!=0)/*so I will not remove the code*/
                 {
                    
                     C_code = C_code.substring(0,index-1)+C_code.substring(C_code.indexOf("\n", index)); /*Will remove #if(...) part from code*/
                     endif_index = C_code.indexOf("#endif");  /*because the code is changed so index changed*/
                     C_code = C_code.substring(0,endif_index-1)+C_code.substring(C_code.indexOf("\n", endif_index)); /*Will remove #endif part from code*/                
                 }else/*I will remove the code between #if and #endif*/
                 {
                     C_code = C_code.substring(0,index-1)+C_code.substring(endif_index+6); /*Will remove #if(...) part from code*/
                 }
                 
   
             }else if(C_code.substring(index).startsWith("#include"))
             {
                 String file_name=C_code;
                 file_name = file_name.substring( file_name.indexOf("<")+1,file_name.indexOf(">") );
                
                 try{
                    File my_lib = new File("my libraries\\"+file_name);
                    Scanner reader = new Scanner(my_lib);
                    while(reader.hasNext())
                    {
                        file_data = file_data + reader.nextLine() + "\n";
                    }
                    System.out.println( file_data);
                    try{
                 
                   hash_line =  C_code.substring(index, C_code.indexOf("\n", index));
                   C_code = C_code.substring(0,index-1) +C_code.substring(C_code.indexOf("\n", index)); /*Will remove # part from code*/
                   }catch(Exception e) /*no \n in code*/
                   {
                       hash_line = C_code.substring(index);
                       C_code = C_code.substring(0,index-1);
                   }
                    
                    
                 }catch(Exception e)
                 {
                  JOptionPane.showMessageDialog(this,"error, can't find the pass of libary file");
                 }
            
             }
        }/*while bracet*/   
          System.out.println(C_code);
        return C_code;
       }catch(Exception e){return "error";}

    }
    
    /***************************************************************
     ***************************************************************
     ***************************************************************
     ***************************************************************
     ***********************Lexical Analysis************************
     ***************************************************************
     ***************************************************************
     ***************************************************************/
    
    /***************************************************************
     * This function makes Lexical Analysis to my code, it subdivide the code into
     * tokens and each token has it's own id or key, and save them in two arrayLists
     * called tokens and key.
     * For example  :  
     *    parameter : fun main(){int x;}
     *    tokens    : token array list , key array list
     *                fun              ,   key=50 
     *                main             ,   key=27  for variable
     *                (                ,   key=6
     *                )                ,   key=7
     *                {                ,   key=4
     *                int              ,   key=1
     *                x                ,   key=27  
     *                ;                ,   key=10
     *                }                ,   key=5
     *  
     ***************************************************************/
    void Lexical_Analysis(String s)
    {
        tokens = new ArrayList<>();
        key    = new ArrayList<>();
        s = s + "                  ";  /*To avoid Exceptions error*/
        int i=0;
        while(i<s.length())
        {
            try{
            while(s.charAt(i)==' ' || s.charAt(i)=='\n') /*To ignor space and newline*/
                i++;
            }catch(Exception e)
            {  /*if the string is end*/
                continue;
            }
            
//            System.out.print("i="+i+"  "+s.charAt(i)+s.charAt(i+1)+s.charAt(i+2)+s.charAt(i+3)+s.charAt(i+4) + "  ");
            
            /*************************************************
             *                  Data types                   *
             *************************************************/
            
             /********int************/
             if(s.charAt(i)=='i')             
              if(s.charAt(i+1)=='n')
               if(s.charAt(i+2)=='t')
                 if(s.charAt(i+3)==' ')
                 {  /*it's int word*/
                     tokens.add("int"); /*it's key is 1*/
                     key.add(1);
                     i = i + 4;  /*increment i*/
                     System.out.print("int ");
                     continue;  /*because i find the token so loop again*/
                 }
             /***********float****************/ 
             if(s.charAt(i)=='f')
               if(s.charAt(i+1)=='l')
                 if(s.charAt(i+2)=='o')
                   if(s.charAt(i+3)=='a')
                     if(s.charAt(i+4)=='t')
                      if(s.charAt(i+5)==' ')
                      { /*it's float word*/
                         tokens.add("float");
                         key.add(2); /*it's key is 2*/
                         i = i + 6;
                         System.out.print("float ");
                         continue;
                      }
             
             /************char***********/
             if(s.charAt(i)=='c')
               if(s.charAt(i+1)=='h')
                 if(s.charAt(i+2)=='a')
                   if(s.charAt(i+3)=='r')
                     if(s.charAt(i+4)==' ')
                     {  /*it's char word*/
                         i = i + 5;
                         tokens.add("char");
                         key.add(3); /*it's key is 3*/
                         System.out.println("char ");
                         continue;
                     }
             
             /************ 'X' *************/
             /*I will change it to a number*/
             if(s.charAt(i)=='\'')
               if(s.charAt(i+2)=='\'')
               { /*so there is an word between 'X' I will change X to assci*/
                  int assci = (int)s.charAt(i+1);
                  tokens.add(assci+"");
                  key.add(25); /*numbers key is 25*/
                  System.out.println(assci);
                  i = i + 3;
                  continue;
               }
           
             
             /*** fun () ***/
             if(s.charAt(i)=='f')
               if(s.charAt(i+1)=='u')
                 if(s.charAt(i+2)=='n')
                   if(s.charAt(i+3)=='(' || s.charAt(i+3)==' ')
                   {     /*it's fun word*/
                         i = i + 3;
                         tokens.add("fun");
                         key.add(50); /*it's key is 50*/
                         System.out.println("fun ");
                         continue;                      
                   }
             
             /*** return nothing ***/
             if(s.charAt(i)=='r')
               if(s.charAt(i+1)=='e')
                 if(s.charAt(i+2)=='t')
                   if(s.charAt(i+3)=='u')
                     if(s.charAt(i+4)=='r')
                       if(s.charAt(i+5)=='n')
                         if(s.charAt(i+6)==';' || s.charAt(i+6)==' ')/*can be ; or space after return key word*/
                         { /*it's return word*/
                            i = i + 6;
                            tokens.add("return");
                            key.add(51); /*it's key is 51*/
                            System.out.println("return ");
                            continue;   
                         }
                    
             
             /*************************************
              *            coditions              *
              *************************************/
             
             /*** if ***/
             if(s.charAt(i)=='i')
               if(s.charAt(i+1)=='f')
                  if(s.charAt(i+2)=='(' || s.charAt(i+2)==' ')/* ( or space */
                  { /* if(codition) or if  (codition)*/
                      i = i + 2; /*I don't want to take the bracet*/
                      tokens.add("if");
                      key.add(28); /*if key is 28*/
                      System.out.println("if");
                      continue;
                  }
             
             /*** while ***/
             if(s.charAt(i)=='w')
               if(s.charAt(i+1)=='h')
                 if(s.charAt(i+2)=='i')
                   if(s.charAt(i+3)=='l')
                     if(s.charAt(i+4)=='e')
//                       if(s.charAt(i+5)=='(' || s.charAt(i+5)==' ')
                       {
                          i = i + 5;
                          tokens.add("while");
                          key.add(29); /*while key is 29*/
                          System.out.println("while");
                          continue;
                       }
             
             /*** for ***/
             if(s.charAt(i)=='f')
               if(s.charAt(i+1)=='o')
                 if(s.charAt(i+2)=='r')
                    if(s.charAt(i+3)=='(' || s.charAt(i)==' ')
                    {
                        i = i + 3;
                        tokens.add("for");
                        key.add(30); /*for key is 30*/
                        System.out.println("for");
                        continue;
                    }
             
 
             /*************************************
             *              Symbols               *
             **************************************/
             
             /*** { ***/
             if(s.charAt(i)=='{')
             {
                 i++;
                 tokens.add("{");
                 key.add(4); /*it's key is 4*/
                 System.out.println("{ ");
                 continue;
             }
             
             /*** } ***/
             if(s.charAt(i)=='}')
             {
                 i++;
                 tokens.add("}");
                 key.add(5); /*it's key is 5*/
                 System.out.println("} ");
                 continue;
             }
             
             /*** ( ***/
             if(s.charAt(i)=='(')
             {
                 i++;
                 tokens.add("(");
                 key.add(6); /*it's key is 6*/
                 System.out.println("( ");
                 continue;
             }
             
             /*** ) ***/
             if(s.charAt(i)==')')
             {
                 i++;
                 tokens.add(")");
                 key.add(7); /*it's key is 7*/
                 System.out.println(") ");
                 continue;
             }
             
             /*** [ ***/
             if(s.charAt(i)=='[')
             {
                 i++;
                 tokens.add("[");
                 key.add(8); /*it's key is 8*/
                 System.out.println("[ ");
                 continue;
             }
             
             /*** ] ***/
             if(s.charAt(i)==']')
             {
                 i++;
                 tokens.add("]");
                 key.add(9); /*it's key is 9*/
                 System.out.println("] ");
                 continue;
             }
             
             /*** ; ***/
             if(s.charAt(i)==';')
             {
                 i++;
                 tokens.add(";");
                 key.add(10); /*it's key is 10*/
                 System.out.println("; ");
                 continue;
             }
             
             /*** , ***/
             if(s.charAt(i)==',')
             {
                 i++;
                 tokens.add(",");
                 key.add(11); /*it's key is 11*/
                 System.out.println(", ");
                 continue;
             }
             
             /********************************************
              *           Arithmetic operators           *
              ********************************************/
             
             /*** + ***/
             if(s.charAt(i)=='+')
             {
                 i++;
                 tokens.add("+");
                 key.add(12); /*it's key is 12*/
                 System.out.println("+ ");
                 continue;
             }
             
             
             /*** - ***/
             if(s.charAt(i)=='-')
             {
                 i++;
                 tokens.add("-");
                 key.add(13); /*it's key is 13*/
                 System.out.println("- ");
                 continue;
             }
             
             /*** / ***/
             if(s.charAt(i)=='/')
             {
                 i++;
                 tokens.add("/");
                 key.add(14); /*it's key is 14*/
                 System.out.println("/ ");
                 continue;
             }
             
             /*** * ***/
             if(s.charAt(i)=='*')
             {
                 i++;
                 tokens.add("*");
                 key.add(15); /*it's key is 15*/
                 System.out.println("* ");
                 continue;
             }
             
             /**********************************************
              *              operators                     *
              **********************************************/            
             
             /*** == ***/
             if(s.charAt(i)=='=')
               if(s.charAt(i+1)=='=')
               {
                 i = i + 2;
                 tokens.add("==");
                 key.add(20); /*it's key is 20*/
                 System.out.println("== ");
                 continue;
               }
             
             
             /*** >= ***/
             if(s.charAt(i)=='>')
               if(s.charAt(i+1)=='=')
               {
                 i = i + 2;
                 tokens.add(">=");
                 key.add(21); /*it's key is 21*/
                 System.out.println(">= ");
                 continue;
               }
             
              /*** <= ****/
             if(s.charAt(i)=='<')
               if(s.charAt(i+1)=='=')
               {
                 i = i + 2;
                 tokens.add("<=");
                 key.add(22); /*it's key is 22*/
                 System.out.println("<= ");
                 continue;
               }
             
             /*** != ***/
             if(s.charAt(i)=='!')
               if(s.charAt(i+1)=='=')
               {
                 i = i + 2;
                 tokens.add("!=");
                 key.add(26); /*it's key is 26*/
                 System.out.println("!= ");
                 continue;
               }
             
             
             /*** > ***/
             if(s.charAt(i)=='>')
             {
                 i++;
                 tokens.add(">");
                 key.add(16); /*it's key is 16*/
                 System.out.println("> ");
                 continue;
             }
             
             /*** < ***/
             if(s.charAt(i)=='<')
             {
                 i++;
                 tokens.add("<");
                 key.add(17); /*it's key is 17*/
                 System.out.println("< ");
                 continue;
             }
             
             
             /*** = ***/
             if(s.charAt(i)=='=')
             {
                 i++;
                 tokens.add("=");
                 key.add(18); /*it's key is 18*/
                 System.out.println("= ");
                 continue;
             }
             
              /*** ! ***/
             if(s.charAt(i)=='!')
             {
                 i++;
                 tokens.add("!");
                 key.add(19); /*it's key is 19*/
                 System.out.println("! ");
                 continue;
             }
             
              /*** && ***/
             if(s.charAt(i)=='&')
               if(s.charAt(i+1)=='&')
               {
                 i = i + 2;
                 tokens.add("&&");
                 key.add(23); /*it's key is 23*/
                 System.out.println("&& ");
                 continue;
               }
             
              /*** || ***/
             if(s.charAt(i)=='|')
               if(s.charAt(i+1)=='|')
               {
                 i = i + 2;
                 tokens.add("||");
                 key.add(24); /*it's key is 24*/
                 System.out.println("|| ");
                 continue;
               }
             
             /****** numbers ******/
             String numb = "";  /*to store my number in it*/
             boolean is_numb=false; /*to know if he enters the loop or not*/
             while(Character.isDigit(s.charAt(i)))
             {
                 numb = numb + s.charAt(i);
                 i++;
                 is_numb = true;
             }
             if(is_numb) /*we enter the loop*/
             {
                 tokens.add(numb);
                 key.add(25); /*it's key is 25*/
                 System.out.println(numb);
                 continue;
             }
             
             /******* variables *******/
             String variable = "";  /*to store my variable in it*/
             if(Character.isLetter(s.charAt(i))) /*the first character must be letter*/
             {
                variable = variable + s.charAt(i);
                i++;
                while(Character.isLetter( s.charAt(i) ) || Character.isDigit( s.charAt(i) ) || s.charAt(i)=='_')
                {  /*if it's a letter or digit or _ */
                    variable = variable + s.charAt(i);
                    i++;
                }
                 System.out.println(variable);
                 tokens.add(variable);
                 key.add(27); /*it's key is 27*/
                 continue;
             }
             
             /*if it doesn't match any one*/
             tokens.add("error");
             key.add(0); /*error key is 0*/
             
             
        }/*while bracet*/
        
    }
    
    /*This function is used in postfix function to find the parority of oprators*/
    private int what_priority(char c)
    {
        if(c == '+' || c=='-' )
            return 1;
        else
            return 2;
    }
    
    
    
    /*This function is used in Postfix function to replace digits and variables with 
      symbols to be able to make postfix to the equation.
      For example : 
          parameter : 5*var+99-100/var2
          return    : A*B+C-D/E
    */
    String replace_digit_to_letters(String s)
    {
        Hashtable<Character,String> numbers =new Hashtable<>();
        char x = 'A';
        int plus;
        String temp;
        boolean bool;
        
        for(int i=0;i<s.length();i++)
        {
          temp="";
          bool=false;
          plus=0;
          
          /*take all digits beside each other*/
          while( (i+plus)< s.length() && ( Character.isDigit( s.charAt(i+plus)) || s.charAt(i+plus)=='.' ) )
          {
             bool=true;              
             temp = temp + s.charAt(i+plus);
             plus++;
          }
          
          /*replace the variable by a symbol*/
           plus = 0;
           if((i+plus)< s.length() && Character.isLetter(s.charAt(i+plus))) /*the first character must be letter*/
            {
                bool = true;
                temp = temp + s.charAt(i+plus);
                plus++;
                /*to take all the variable*/
                
                while((i+plus)< s.length() && (Character.isLetter( s.charAt(i+plus) ) || Character.isDigit( s.charAt(i+plus) ) || s.charAt(i+plus)=='_' ) )
                {  /*if it's a letter or digit or _ */
                    temp = temp + s.charAt(i+plus);
                    plus++;
                }
                
    
             }
          
          
          if(bool)
          {
            s = s.replaceFirst(temp,x+"");
            numbers.put(x,temp);
            x++;             
          }         
        }
       
       return s ;
    }
    
    /*this function is to change infix to prefix*/
    String from_infix_to_postfix(String s)
    {
    /*First we replace digits and variables with 
      symbols to be able to make postfix to the equation, and all replaced elements 
      are stored in hashtable to return back to it's original form after postfix .
      For example : 
          parameter : 5*var+99-100/var2
          return    : A*B+C-D/E
    */
        Hashtable<Character,String> numbers =new Hashtable<>();
        char x1 = 'A';
        int plus;
        String temp1;
        boolean bool;
        
        for(int i=0;i<s.length();i++)
        {
          temp1="";
          bool=false;
          plus=0;
          /*replace the number int or float with a symbole*/
          while( (i+plus)< s.length() && ( Character.isDigit( s.charAt(i+plus)) || s.charAt(i+plus)=='.' ) )
          {
             bool=true;              
             temp1 = temp1 + s.charAt(i+plus);
             plus++;
          }
          
          /*replace the variable by a symbol*/
           plus = 0;
           if((i+plus)< s.length() && Character.isLetter(s.charAt(i+plus))) /*the first character must be letter*/
            {
                bool = true;
                temp1 = temp1 + s.charAt(i+plus);
                plus++;
                /*to take all the variable*/
                
                while((i+plus)< s.length() && (Character.isLetter( s.charAt(i+plus) ) || Character.isDigit( s.charAt(i+plus) ) || s.charAt(i+plus)=='_' ) )
                {  /*if it's a letter or digit or _ */
                    temp1 = temp1 + s.charAt(i+plus);
                    plus++;
                }
                
    
             }
          
          if(bool)/*if it's a varaible or number*/
          {
            s = s.replaceFirst(temp1,x1+"");
            numbers.put(x1,temp1);
            x1++;             
          }         
        }
        
        
        /*Then make Postfix*/
        String postfix="";
        Stack<Character> my_stack = new Stack<>(); /*creat a stack*/
        char temp;
        
        for(int i=0;i<s.length();i++)
        {
            temp = s.charAt(i);
              
           if( temp!='+' && temp!='-' && temp!='/' && temp!='*' && temp!='(' && temp!=')' ) 
           {
              postfix = postfix + " " +temp;           
           }
           
           if( temp=='(' ) 
             my_stack.push(temp);
           else if( temp==')' )
           {
               char x;
               while(!my_stack.isEmpty())
               {
                   x = my_stack.pop();
                   if(x=='(')
                       break;
                   else
                   {
                       postfix = postfix + " " + x;
                   }
               }/*while bracet*/
               
           }else if( temp=='+' || temp=='-' || temp=='/' || temp=='*')
           {
               if(my_stack.isEmpty())
                   my_stack.push(temp);
               else
               {
                   while(!my_stack.isEmpty())
                   {
                       char x = my_stack.pop();
                       if(x=='(')
                       {
                          my_stack.push(x);
                          break;
                       }else if(temp=='+' || temp=='-' || temp=='/' || temp=='*' )
                       {
                           if(what_priority(x) < what_priority(temp))
                           {
                               my_stack.push(x);
                               break;
                           }else
                           {
                             postfix = postfix + " " + x;
                           }
                       }
                   }
                   my_stack.push(temp);
               }
           }
           
           
        }/*for bracet*/
        
        while(!my_stack.isEmpty())
            postfix = postfix + " " + my_stack.pop();
        
        int k=0;
        char key;
        String nu;
        while( k<numbers.size() )
        {
            key = (char) ('A'+k) ;
            nu = numbers.get(key);
            System.out.println(nu);
            postfix = postfix.replace( key+"" , " "+nu+" ");
            k++;
        }
        
        return postfix;
    }
    
    
    /***********************************************************
     ***********************************************************
     ***********************************************************
     ************************ Parsing **************************
     ***********************************************************
     ***********************************************************
     ***********************************************************/
    
    /**************************************************************
     * This function is to do parsing to the tokens which comes from 
     * Lexical Analysis, in which we link all tokens with each other 
     * to come up with understandable sentences, and each sentence has
     * it's key and stored in sentence arrayList and sen_keys arrayList.
     * 
     *     parameter : int start is the start of my tokens and it is
     *     used in recursion.
     * For example :
     *     tokens and keys are             : int(key=1), x(key=27), ;(key=10) 
     *     sentence and sen_keys arrayList : x (key=1)
     * another Ex : 
     *     tokens and keys are             : if(key=28), ( (key=6), x(key=27), >(key=16) , 10(key=25) , ) (key=7) 
     *     sentence and sen_keys arrayList : x;>;10 (key=5)
     * 
     *     return : ok if all sentences matched or error if any error occur 
     * 
     ************************************************************/
    String parsing(int start)
    {

        while(start<key.size())
        {
            
            if(key.get(start)==5)/* }, to end recurtion */
              return start+"";

            
            
            
        /*************************************
         *        declaration of variable    *
         *************************************/
        
        /*** int x ; ***/
        /***************************************************
         * Stored in sentence array list in this form variable
         * It's key is 1
         **************************************************/
        try{
        if(key.get(start)==1)/*int*/
          if(key.get(start+1)==27)/*variable*/
            if(key.get(start+2)==10)/*;*/
            {
                sentence.add(tokens.get(start+1));
                sen_keys.add(1); /*int sentence is 1*/
                start = start + 3;
                continue;
            }else if(key.get(start+2)==11) /*it's comma for int x,y,z,.. */
            {
                sentence.add(tokens.get(start+1)); /*add first variable*/
                sen_keys.add(1);
                start = start + 2;
                
                do{
                   if(key.get(start)==11 && key.get(start+1)==27)/* , variable */
                   {
                     sentence.add(tokens.get(start+1));
                     sen_keys.add(1);
                     start = start + 2;
                   }else
                       return "error in variable decleration";
                }while( key.get(start) != 10 );
                start++;
                continue;
            }else if(key.get(start+2)==6)/*(*/
            {
                return "error The function must start with fun keyword";
            }
        }catch(Exception e){return "error, the last line is not correct";}
        
        /*floatinf point Not supported in CPU*/
        /*** float ***/
        /***************************************************
         * Stored in sentence array list in this form variable
         * It's key is 2
         **************************************************/
//        if(key.get(start)==2)/*float*/
//          if(key.get(start+1)==27)/*variable*/
//            if(key.get(start+2)==10)/*;*/
//            {
//                sentence.add(tokens.get(start+1));
//                sen_keys.add(2); /*flaot sentence is 2*/
//                start = start + 3;
//                continue;
//            }else if(key.get(start+2)==11) /*it's comma for float x,y,z,.. */
//            {
//                sentence.add(tokens.get(start+1)); /*add first variable*/
//                sen_keys.add(2);
//                start = start + 2;
//                
//                do{
//                   if(key.get(start)==11 && key.get(start+1)==27)/* , variable */
//                   {
//                     sentence.add(tokens.get(start+1));
//                     sen_keys.add(2);
//                     start = start + 2;
//                   }else
//                       return "error in variable decleration";
//                }while( key.get(start) != 10 );
//                start++;
//                continue;
//            }else if(key.get(start+2)==6)/*(*/
//            {
//                return "error The function must start with fun keyword";
//            }
        
        /*** char ***/
        /***************************************************
         * Stored in sentence array list in this form variable
         * It's key is 3
         **************************************************/
       try{
        if(key.get(start)==3)/*char*/
          if(key.get(start+1)==27)/*variable*/
            if(key.get(start+2)==10)/*;*/
            {
                sentence.add(tokens.get(start+1));
                sen_keys.add(3); /*char sentence is 3*/
                start = start + 3;
                continue;
            }else if(key.get(start+2)==11) /*it's comma for char x,y,z,.. */
            {
                sentence.add(tokens.get(start+1)); /*add first variable*/
                sen_keys.add(3);
                start = start + 2;
                
                do{
                   if(key.get(start)==11 && key.get(start+1)==27)/* , variable */
                   {
                     sentence.add(tokens.get(start+1));
                     sen_keys.add(3);
                     start = start + 2;
                   }else
                       return "error in variable decleration";
                }while( key.get(start) != 10 );
                start++;
                continue;
            }else if(key.get(start+2)==6)/*(*/
            {
                return "error The function must start with fun keyword";
            }
          }catch(Exception e){return "error, the last line is not correct";}
        
        /*******************************************
         *         return without value            *
         *******************************************/
        /***************************************************
         * Stored in sentence array list in this form return
         * It's key is 12
         **************************************************/
        try{
         if(key.get(start)==51)/*** return ***/
          if(key.get(start+1)==10)/*;*/
          {
            sentence.add("return");
            sen_keys.add(12); /*return sentence is 12*/
            start = start + 2;
            continue;
          }
        }catch(Exception e){return "error, no ; after return";}
         /********************************************
          *            return with value             *
          ********************************************/
         /***************************************************
         * Stored in sentence array list in this form variable or number 
         * It's key is 13
         **************************************************/
        try{
         if(key.get(start)==51)/*return variable or positive number*/
           if(key.get(start+1)==25 || key.get(start+1)==27)/*variable  or number*/
             if(key.get(start+2)==10)/*;*/
             {
                sentence.add(tokens.get(start+1));/*contains the variable or number*/
                sen_keys.add(13);/*it's key is 13*/
                start = start +3;
                continue;
             }
        }catch(Exception e){return "error, in return";}
        
        try{
          if(key.get(start)==51)/*return negative number number*/
           if(key.get(start+1)==13) /*-ve*/
            if(key.get(start+2)==25)/*number*/
              if(key.get(start+3)==10)/*;*/
              {
                sentence.add("-"+tokens.get(start+2));/*contains the variable or number*/
                sen_keys.add(13);/*it's key is 13*/
                start = start +4;
                continue;
              }
           }catch(Exception e){return "error, in return";}
        
        /*********************************************
         *                   assining                *
         *********************************************/
        /***************************************************
         * Stored in sentence array list in this form variable;postfix for equations
         *                                            variable;fun_name();arg1-value;arg2-value;... for functions
         * It's key is 4
         **************************************************/
        
        /* x = 2*3+9;  I will make a postfix then to be  x;923*+ */
        try{
        if(key.get(start)==27)/*variable*/
         if(key.get(start+1) == 18)/* = */
         {
             /* x = 2*3+9 */
             String variable = tokens.get(start);
             start = start + 2; /*starts points to first token after =*/
             int copy = start; /*copy the value of start*/
             String str="";
             boolean bool=true; /*true if it's assining of a equation and false if it's assining of a function*/
           
             while(key.get(start)!=10)/*till ; */
             { 
                 if(key.get(start)==11 /*comma*/ || ( key.get(start)==6 && key.get(start+1)==7)/*()*/ || (key.get(start)==6 && key.get(start+2)==7)/*(a)*/ )
                 {
                   bool=false;
                   start = copy;
                   break;  
                 }
                 if(start>=key.size())
                     return "error no ; in last line";
                 str = str + tokens.get(start);
                 start++;
             }
             
             if(bool)/*equation like x=9*y+1; */
             {
               str = from_infix_to_postfix(str);
               sentence.add(variable+";"+str);
               sen_keys.add(4);
               start++;
               continue;
             }
             else /*function like x=add(1,2); */
             {
               str = "";
              if(key.get(start)==27)/*variable*/
                if(key.get(start+1)==6)/* ( */
                {
                    str=tokens.get(start);/*get the name of function*/
                    try{
                    start = start + 2;
                    while(key.get(start)!=7)/*loop till ) */
                    {
                        if(key.get(start)==11)/*comma*/
                           start++;
                        str = str +";"+tokens.get(start);
                        start++;

                    }
                    }catch(Exception e){return "error, no ) for the calling function";}

                    if(key.get(start+1)!=10)/*;*/
                        return "error, not found ; in the calling of function";
                    start = start + 2;
                    sentence.add(variable+";"+str);
                    sen_keys.add(11);/*The key of calling function*/               
                    continue;              
                }
                
             }
                
         } 
         }catch(Exception e){return "error in assining";}

        
        /*********************************************
         *             coditions                     *
         *********************************************/
        
        
        /* if(codition) */
        /***************************************************
         * Stored in sentence array list in this form (variable or number);condition;(variable or number)
         * For example : if(x>1) ->  x;>;1
         * It's key is 5
         **************************************************/
       try{
        if(key.get(start)==28) /* if */
          if(key.get(start+1)==6)/* ( */
          {
              start = start + 2 ;
              String str;
              
              if(key.get(start)==27 || key.get(start)==25)/*The first token after ( should be variable or number*/
                 str = tokens.get(start) + ";";
              else
                  return "error, not found first variable or number in if";
              if(key.get(start+1)==20 || key.get(start+1)==21 || key.get(start+1)==22 || key.get(start+1)==16 || key.get(start+1)==17 || key.get(start+1)==26)/*The second token after ( should be >,<,==,<=,>=,!=*/
                 str = str + tokens.get(start+1)+";";
              else 
                  return "error, not found the compare operator in if";
              if(key.get(start+2)==27 || key.get(start+2)==25)/*The third token after ( should be variable or number*/
                  str = str + tokens.get(start+2);
              else
                  return "error, not found the second variable or number in if";
              
              if(key.get(start+3) != 7)/* it should be ) */
                 return "error many coditions in if or no codition, should be one codition only";
              
              
              start = start + 5;
              sentence.add(str); /* var or num ; codition ; var or num */
              sen_keys.add(5);
              
              if(key.get(start-1)==4) /* { */
              {
              /*opening of block*/
              sentence.add("{");
              sen_keys.add(6);
              
              System.out.println("start 1 = "+start);
              String s = parsing(start); /* recurtion till the } */
              if(s.contains("error"))
                  return s;
              else if(s.contains("ok"))
                  return "error, no } in if";
              else
                  start = Integer.parseInt(s);
              
              System.out.println("start 2 = "+start);
              /*ending of block*/
              start++;
              sentence.add("}");
              sen_keys.add(7);

              continue;
              }else
                  return "error, not found bracet { in if condtion";
              
            }else
                return "error, not found bracet ( in if condition";
          }catch(Exception e){return "error, in if";}
        
        /*********************************************
         *            while(codition){}              *
         *********************************************/
        /***************************************************
         * Stored in sentence array list in this form (variable or number);condition;(variable or number)
         * For example : while(x==2) -> x;==;2
         * It's key is 8
         **************************************************/
        try{
        if(key.get(start)==29) /* while */
          if(key.get(start+1)==6)/* ( */
          {
              start = start + 2 ;
              String str;
              
              if(key.get(start)==27 || key.get(start)==25)/*The first token after ( should be variable or number*/
                 str = tokens.get(start) + ";";
              else
                  return "error, not found first variable or number in while";
              if(key.get(start+1)==20 || key.get(start+1)==21 || key.get(start+1)==22 || key.get(start+1)==16 || key.get(start+1)==17 || key.get(start+1)==26)/*The second token after ( should be >,<,==,<=,>=,!=*/
                 str = str + tokens.get(start+1)+";";
              else 
                  return "error, not found the compare operator in while";
              if(key.get(start+2)==27 || key.get(start+2)==25)/*The third token after ( should be variable or number*/
                  str = str + tokens.get(start+2);
              else
                  return "error, not found the second variable or number in while";
              
              if(key.get(start+3) != 7)/* it should be ) */
                 return "error many coditions in while or no codition, should be one codition only";
              
              
              start = start + 5;
              sentence.add(str); /* var or num ; codition ; var or num */
              sen_keys.add(8);
              
              if(key.get(start-1)==4) /* { */
              {
              /*opening of block*/
              sentence.add("{");
              sen_keys.add(6);
              
              System.out.println("start 1 = "+start);
              String s = parsing(start); /* recurtion till the } */
              if(s.contains("error"))
                  return s;
              else if(s.contains("ok"))
                  return "error, no } in while";
              else
                  start = Integer.parseInt(s);
              
              System.out.println("start 2 = "+start);
              /*ending of block*/
              start++;
              sentence.add("}");
              sen_keys.add(7);

              continue;
              }else
                  return "error, not found bracet { in while condtion";
              
            }else
                return "error, not found bracet ( in while condition";
          }catch(Exception e){return "error, in while";}
       
        /******************************************
         *    for(assining;codition;assining){}   *
         ******************************************/
       try{
        if(key.get(start)==30)/*for*/
          if(key.get(start+1)==6)/* ( */
          {
            start = start + 2;
            
           /* 
            for(first_assining;condition;second_assining)
            {
              //code
            }
            we will change for loop to while loop
            first assining;
            while(condtion)
            {
              //code
              second_assining;
            }
            */
                
            /*first_assining*/
            if(key.get(start)==10)/* ; */
            { /*if no assining and in for loop*/
                start++;
                /*Do nothing*/
            }else
            { /*first assining part in for loop*/
                /*x = 2*3+9; */
                if(key.get(start)==27)/*variable*/
                 if(key.get(start+1) == 18)/* = */
                 {
                     String variable = tokens.get(start);/*get the variable x */
                     start = start + 2;
                     String str="";
                     while(key.get(start)!=10)/*till ; */
                     { 
                         if(start>=key.size())
                             return "error no ; in for";
                         str = str + tokens.get(start);
                         start++;
                     }

                     str = from_infix_to_postfix(str);
                     sentence.add(variable+";"+str);/*add the first assining*/
                     sen_keys.add(4);
                     start++;
                 }else
                 {
                     return "error, not found = in for loop in first assining";
                 }
            }
            
            /*codition in for loop */
            String str;
            String str2="";

            if(key.get(start)==10)/*second ;*/
            { /*no codition so it's infinity loop*/
                /*put true codition*/
                sentence.add("1;==;1");
                sen_keys.add(8);
                start++;
               /*Do nothing*/
            }else/*there is a condition*/
            {
            if(key.get(start)==27 || key.get(start)==25)/*The first token after should be variable or number*/
               str = tokens.get(start) + ";";
            else
                return "error, not found first variable or number in for loop codition";
            if(key.get(start+1)==20 || key.get(start+1)==21 || key.get(start+1)==22 || key.get(start+1)==16 || key.get(start+1)==17 || key.get(start+1)==26)/*The second token after should be >,<,==,<=,>=,!=*/
               str = str + tokens.get(start+1)+";";
            else 
                return "error, not found the compare operator in for loop codition";
            if(key.get(start+2)==27 || key.get(start+2)==25)/*The third token after should be variable or number*/
                str = str + tokens.get(start+2);
            else
                return "error, not found the second variable or number in for loop codition";

              System.out.println("***********"+tokens.get(start+3));
            if(key.get(start+3)!=10)/*no second ; */
               return "second ; in for condition not found";
            
             start = start + 4;
             sentence.add(str); /*if statment, var or num ; codition ; var or num */
             sen_keys.add(8); /*While conditoin*/
            }
            
            
             /*** second assining ***/
            if(key.get(start)==7)/* ), so no second assining */
            {
                start++;
                /*Do nothing*/
            }else
            { /*second assining part in for loop*/
                /*x = 2*3+9; */
                if(key.get(start)==27)/*variable*/
                 if(key.get(start+1) == 18)/* = */
                 {
                     String variable = tokens.get(start);
                     start = start + 2;
                     while(key.get(start)!=7)/*till ) */
                     { 
                         if(start>=key.size())
                             return "error no ; in for";
                         str2 = str2 + tokens.get(start);
                         start++;
                     }

                     str2 = from_infix_to_postfix(str2);
                     str2 = variable+";"+str2;
                     start = start +2;
                 }else
                 {
                     return "error, not found = in for loop in first assining";
                 }
            }
             
            /*** inside for loop ***/
            if(key.get(start-1)==4) /* { */
            {
              /*opening of block*/
              sentence.add("{");
              sen_keys.add(6);
              
              String s = parsing(start); /* recurtion till the } */
              if(s.contains("error"))
                  return s;
              else if(s.contains("ok"))
                  return "error, no } in for loop";
              else
                  start = Integer.parseInt(s);
              
              /*ending of block*/
              /*Before ending the bloc, we should add the second assining*/
              
              sentence.add(str2);/*second assining*/
              sen_keys.add(4);
              
              start++;
              sentence.add("}"); /*ending of the block*/
              sen_keys.add(7);

              continue;
              }else
                  return "error, not found bracet { in for condtion";
            
             
          }else
                return "error, not found bracet ( in for";
       }catch(Exception e){return "error, in for";}
        
        /*******************************************************
         *                 functions                           *
         *******************************************************/
        /***************************************************
         * Stored in sentence array list in this form function-name;arg-type;arg-name;...
         * For example : fun add(int x,int y) -> add;int;x;int;y
         * It's key is 9
         **************************************************/
       try{
        if(key.get(start)==50)/*fun*/
          if(key.get(start+1)==27)/*variable name*/
            if(key.get(start+2)==6)/*must be ( */
            {
               String str;
               String fun_name = tokens.get(start+1); /*the name of function*/
               str  = fun_name;
               start = start + 3;
               while(key.get(start)!=7)/*loop till ) */
               {
                   /*** arguments ***/
                   if(key.get(start)==1 || key.get(start)==2 || key.get(start)==3)/*int or float or char*/
                   {
                     if(key.get(start+1)==27)/*variable*/
                     {
                       str = str +";"+key.get(start)+";"+tokens.get(start+1);
                       start = start + 2;
                       if(key.get(start)==11)/*it should be a comma*/
                         start ++; /*to ignore the comma*/  
                     }                     
                     else
                     {
                         return "error in arrguments name of function "+fun_name;
                     }
                   }else{
                         return "error in arrguments decleration of  function "+fun_name;
                   }
               }
                sentence.add(str);
                sen_keys.add(9);
                start++;
                /*function block*/
                 if(key.get(start)==4) /* { */
                {
                /*opening of block*/
                sentence.add("{");
                sen_keys.add(6);

                String s = parsing(start+1);


                if(s.contains("error"))
                    return s;
                else if(s.contains("ok"))
                    return "error, no } in if";
                else
                    start = Integer.parseInt(s);
                
                
                /*ending of block*/
                start++;
                sentence.add("}");
                sen_keys.add(7);
                
                continue;

                }else
                   return "error no oppening bracet in function"+fun_name;

             
                
            }else/*No bracek and it can't be a variable declertion because we check it at first so it's an error*/
            {
                return "error, the function "+tokens.get(start+1)+" is not correct no ( in  it";
            }
          }catch(Exception e){return "error, in functoin";} 
        
        
        /*************************************************
         *      calling function like add(1,3);          *
         *************************************************/
        /***************************************************
         * Stored in sentence array list in this form function-name;arg1-value;arg2-value;...
         * For example : add(x,9) -> add;x;9
         * It's key is 10
         **************************************************/
        try{
         if(key.get(start)==27)/*variable*/
           if(key.get(start+1)==6)/* ( */
           {
               String str=tokens.get(start);/*get the name of function*/
               try{
               start = start + 2;
               while(key.get(start)!=7)/*loop till ) */
               {
                   if(key.get(start)==11)/*comma*/
                      start++;
                   str = str +";"+tokens.get(start);
                   start++;
                   
               }
               }catch(Exception e){return "error, no ) for the calling function";}
               
               if(key.get(start+1)!=10)/*;*/
                   return "error, not found ; in the calling of function";
               start = start + 2;
               sentence.add(str);
               sen_keys.add(10);/*The key of calling function*/               
               continue;              
           }
        }catch(Exception e){return "error, in calling the function";}
        
      //  System.out.println("tokrin nub="+tokens.size()+tokens.get(start));
        return "error";
           
        }/*while bracet*/
        
        return "ok";
     
    }
    
    
    /**********************************************************************************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************
     *******************semantic analysis and change to assemply***********************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************
     **********************************************************************************/
     
     /**********************************************************************************
      * This function takes the sentences and there key from there arrayList and 
      * translate it to assembly.
      **********************************************************************************/
    
    ArrayList<String>function_names = new ArrayList<>(); /*This arrayList will contain our functions name*/
    ArrayList<Integer>arg_num  = new ArrayList<>(); /*This arrayList will contains the number of arguments in the function*/
    Stack<String>memory_variables  = new Stack<>(); /*This arrayList will contain our variables which are in memory*/
    Stack<String>memory_variables_type = new Stack<>(); /*This arrayList will contain our variables name ; and it's function*/
    ArrayList<String>reg_variables =  new ArrayList<>();/*contains the variables that is stored in registers*/
    String tail_of_assemply=""; /*contains the assemply that should be added at last*/
    String function_in_name="";/*contains the name of the function that I am in*/
    int lable_number=0; /*contains the number of used lables*/
    int sp_inti=0; /*contains the start of my stack*/
    int available_s_reg=2;  /*contains the available s reg number*/ 
    int count_push_for_return=0; /*This is a global counter for the function, to counts
                                 the number of pushes, and it's is cleared at rhe begaing of the function*/
    
    String to_assempply(int my_sentence_index)
    {
        boolean end=true;/*is false if we enter the while loop and is true if we didn't enter so it's the end of code */
        int count_pushs=0;/*This function count the of variables that pushed*/
        /* int : 1 , float : 2 , char : 3 */
        String my_assembly="";
        StringTokenizer sk1,sk2;
        int available_t_reg;/*contains the index of first @t rigester which is not busy*/
        int sentence_key;
      
        while(my_sentence_index < sen_keys.size())
        {
            
     try{
        end=false;
        sentence_key = sen_keys.get(my_sentence_index);
        String str1="",str2="",str3="";
        switch (sentence_key) {
            
            /*****************closing of the bracket**************************
             * At the closing of the bracket, I will pop all variable which is 
             * pushed in this block and remove them from memory_variable arrayList.
             *******************************************************************/
            case 7: 
//                int x = memory_variables.size(); /*The number of variables*/
                for(int i=0;i<count_pushs;i++)
                {
                  my_assembly = my_assembly + "Pop .\n";
                  memory_variables.pop(); /*remove form array*/
                  memory_variables_type.pop(); 
                }
                System.out.println("cound pushs}="+count_pushs);
                return my_sentence_index+";"+my_assembly;/*concatinate the index with assemply to return them*/
           
            /***********************return variable;**************************
             * At return I will put the return value in @v0 register
             *****************************************************************/
            case 13:
                 str1 = sentence.get(my_sentence_index);/*contains the return value */
                 if(var_reg_num(str1)==2)/*if the return is a number*/
                 {
                     my_assembly = my_assembly + "LoadI @v0,@zero,"+str1+".\n";
                 }else /*so it's a variable*/
                 {
                     str2 = variable_in_memory_or_register(str1+";"+function_in_name);
                     if(str2.startsWith("@s"))/*so it is stored in memory*/
                     {
                         int my_variable_index = ( -memory_variables.size()+memory_variables.indexOf(str1) );
                         if(memory_variables.indexOf(str1)==-1)
                            return "error, not found the variable "+str1;
                         my_assembly = my_assembly +"Load @v0,@sp,"+my_variable_index+".\n";/*put the variable value in @v0 register*/
                     }else /*so it's in a register*/
                     {
                       my_assembly = my_assembly + "LoadI @v0,"+str2+",0.\n";
                     }
                 }
                 /*will not break but will go to return void case*/
            case 12: /*return void, I will put Jr @ra instruction only toput the value of @ra in PC*/
//                 if(sen_keys.get(my_sentence_index+1)!=7)/*if there is a } after it so we will ignoe the return*/

                for(int i=0;i<count_push_for_return;i++)
                {
                  my_assembly = my_assembly + "Pop .\n";
                }
                  my_assembly = my_assembly + "Jr @ra.\n\n";/*Put the value of @ra register in PC */
                System.out.println("!!!!!!!!"+function_in_name+" "+count_push_for_return);
                  break;
            case 6:/*The opening of a block, if any block is in the code without if or which or for or function like {int x;x=9;}*/
                /*All opening blocks are handeled the it's own like for,if,while,function*/
               break;
            /*******************integer declaration**********************
             *  I declare an integer so I will push in memory to increment 
             *  the stack pointer and store the the variable name in memory 
             *  variables arrayList.
             ************************************************************/    
            case 1: 
                memory_variables.push(sentence.get(my_sentence_index));/*will add my variable name in arrayList*/
                memory_variables_type.push("@s0"+"int");
                my_assembly = my_assembly + "Push .\n";
                count_pushs++; /*I push a new variable*/
                count_push_for_return++; 
                break;
//            case 2: /*float decleration*/
//                memory_variables.push(sentence.get(my_sentence_index));/*will add my variable name in arrayList*/
//                memory_variables_type.push("@s0"+"float");                
//                my_assembly = my_assembly + "Push .\n";  
//                count_pushs++; /*I push a new variable*/
//                break;
            /*******************char declaration**********************
             *  I declare an integer so I will push in memory to increment 
             *  the stack pointer and store the the variable name in memeory 
             *  variables arrayList.
             ************************************************************/    
             case 3: 
                memory_variables.push(sentence.get(my_sentence_index));/*will add my variable name in arrayList*/
                memory_variables_type.push("@s0"+"char");              
                my_assembly = my_assembly + "Push .\n"; 
                count_pushs++; /*I push a new variable*/
                count_push_for_return++;
                break;
                
            case 4: 
                /*************************************************
                 ***************Variable assining*****************
                 *************************************************/
                Stack<String>my_stack = new Stack<>(); /*This arrayList will contain our variables*/
                str1 = sentence.get(my_sentence_index);/* contains vaiable;postfix */
                sk1 = new StringTokenizer(str1,";");/*to separate the variable from postfix*/
                str1 = sk1.nextToken(); /*contains my variable*/
                str2 = sk1.nextToken(); /*contais the postfix*/
                available_t_reg=0;
//                int available_s_reg=0;
                /***************solving the postfix****************/
                sk2 = new StringTokenizer(str2," ");
                int i=0;
                String s1;
                
                if(sk2.countTokens()==1)/*assining a number or variable in variable directly Ex : x=y; or x=9;*/
                {
                    available_t_reg++; /*to avoid error below*/
                    String right_variable = sk2.nextToken();/*my right variable or number*/
                    if(var_reg_num(right_variable)==2)/*so it's a number*/
                    {
                    int g = Integer.parseInt(right_variable);
                    my_assembly = my_assembly + "LoadI @t0,@zero,"+g+".\n";
                    }else/*so it's a variable*/
                    {
                       String sr = variable_in_memory_or_register(right_variable+";"+function_in_name);
                       if(sr.startsWith("@s"))/*so right variable is in memory*/
                       {
                           /*x=y; and y is in memory*/
                           int idex00 =  ( -memory_variables.size()+memory_variables.indexOf(right_variable) );
                           if(memory_variables.indexOf(right_variable)==-1)
                              return "error, not found the variable "+right_variable;
                           my_assembly = my_assembly + "Load @t0,@sp,"+idex00+".\n";
                               
                       }else/*right variable is in a register*/
                       {
                            my_assembly = my_assembly + "LoadI @t0,"+sr+",0.\n";
                       }
                       
                    }
                }
                
                while(i<sk2.countTokens())
                {
                  s1 = sk2.nextToken().trim();
                  if(s1.equals("+") || s1.equals("-") || s1.equals("*") || s1.equals("/"))
                  {
                      String x1,x2;
                      String function = operator_to_op_code(s1);/* will return Add,Sub,Mul or Div*/
                      x2=my_stack.pop();
                      x1=my_stack.pop();
                      if( var_reg_num(x1)==2 && var_reg_num(x2)==2 ) /*if two are numbers*/
                      {
                          int g;/*contains the result of two numbers*/
                          
                          if(s1.equals("+")) /*to know what is the operation */
                            g = Integer.parseInt(x1) + Integer.parseInt(x2);
                          else if(s1.equals("-"))
                              g = Integer.parseInt(x1) - Integer.parseInt(x2);
                          else if(s1.equals("*"))
                              g = Integer.parseInt(x1) * Integer.parseInt(x2);
                          else 
                              g = Integer.parseInt(x1) / Integer.parseInt(x2);
                             
                          
                          my_assembly = my_assembly + "LoadI @t"+available_t_reg+",@zero,"+g+".\n";
                          my_stack.push("@t"+available_t_reg);  /*push the result reg in stack*/
                          available_t_reg = (available_t_reg + 1)%8; /* 0->7 */

                      }else if( var_reg_num(x1)==0 && var_reg_num(x2)==2 )/*x1 is variable and x2 is a number*/
                      {
                         boolean is_reg;
                         int index_var=-2;
                         String str;/*contains the type of my variable*/
//                        
                         String mor = variable_in_memory_or_register(x1+";"+function_in_name);
                           if(mor.startsWith("@s"))/*so the variable in stack memory*/
                           {
                             index_var =  ( -memory_variables.size()+memory_variables.indexOf(x1) )+sp_inti;/*contains the index of my variable*/
                             if(memory_variables.indexOf(x1)==-1)
                               return "error, not found the variable "+x1;
                             str="@s0";/*contains the type of my variable*/
                             is_reg = false;
                           }else/*so it's in reigter*/
                           {
                              is_reg = true;/*so the variable in a register*/
                              str = mor;   
                           }
                             
                           
                         int g = Integer.parseInt(x2); /*change String to int value*/
                         if(!is_reg)/*if it's in memory so I should get load it*/
                           my_assembly = my_assembly + "Load @s0,@sp,"+index_var+".\n"; /*I will get the value of x1 variable from memory*/
                        
                         my_assembly = my_assembly + function+"I @t" + available_t_reg+ "," +str+ ","+g+".\n";  /*imm function*/ 
                         my_stack.push("@t"+available_t_reg); /*push the result reg in stack*/
                         available_t_reg = (available_t_reg + 1)%8; /* 0->7 */
                      }else if( var_reg_num(x1)==2 && var_reg_num(x2)==0 ) /*x1 is a number but x2 is a variable*/
                      {
                         boolean is_reg;
                         int index_var=-2;
                         String str;/*contains the type of my variable*/
                         String mor = variable_in_memory_or_register(x2+";"+function_in_name);
                         if(mor .startsWith("@s"))/*so the variable in stack memory*/
                         {
                          index_var = ( -memory_variables.size()+memory_variables.indexOf(x2) )+sp_inti;/*contains the index of my variable*/
                          if(memory_variables.indexOf(x2)==-1)
                             return "error, not found the variable "+x2;
                          str="@s0";/*contains the type of my variable*/
                          is_reg = false;
                         }else/*so it's in reigter*/
                         {
                           is_reg = true;/*so the variable in a register*/
                           str = mor;   
                         }
                         
                         
                         
                         int g = Integer.parseInt(x1); /*change String to int value*/
                         if(!is_reg)/*if it's in memory so I should get load it*/
                           my_assembly = my_assembly + "Load @s0,@sp,"+index_var+".\n"; /*I will get the value of x2 variable from memory*/
                         
                         my_assembly = my_assembly + function+"I @t" +available_t_reg+ ","+str+","+g+".\n"; /*imm function*/           
                         my_stack.push("@t"+available_t_reg);  /*push the result reg in stack*/
                         available_t_reg = (available_t_reg + 1)%8; /* 0->7 */
                      }else if(var_reg_num(x1)==0 && var_reg_num(x2)==0 ) /*x1 and x2 are both variables*/
                      {
                         /*For first variable*/
                         boolean is_reg1;
                         int index_var1=-1;
                         String str01;/*contains the type of my variable*/
                         String mor = variable_in_memory_or_register(x1+";"+function_in_name);
                         if(mor.startsWith("@s"))/*so the variable in stack memory*/
                         {
                          index_var1 =  ( -memory_variables.size()+memory_variables.indexOf(x1) )+sp_inti;/*contains the index of my variable*/
                          if(memory_variables.indexOf(x1)==-1)
                            return "error, not found the variable "+x1;
                          str01="@s0";/*contains the type of my variable*/
                          is_reg1 = false;
                         }else/*so it's in reigter*/
                         {
                           is_reg1 = true;/*so the variable in a register*/
                           str01 = mor;   
                         }
                         
                         /*For second variable*/
                         boolean is_reg2;
                         int index_var2=-1;
                         String str02;/*contains the type of my variable*/
                         mor = variable_in_memory_or_register(x2+";"+function_in_name);
                         if(mor.startsWith("@s"))/*so the variable in stack memory*/
                         {
                          index_var2 =  ( -memory_variables.size()+memory_variables.indexOf(x2) )+sp_inti;/*contains the index of my variable*/
                          if(memory_variables.indexOf(x2)==-1)
                            return "error, not found the variable "+str2;
                          str02="@s1";/*contains the type of my variable*/
                          is_reg2 = false;
                         }else/*so it's in reigter*/
                         {
                           is_reg2 = true;/*so the variable in a register*/
                           str02 = mor;   
                         }
                         
                         if(!is_reg1)
                           my_assembly = my_assembly + "Load @s0,@sp,"+index_var1+".\n"; /*I will get the value of x1 variable from memory*/  
                         if(!is_reg2)
                           my_assembly = my_assembly + "Load @s1,@sp,"+index_var2+".\n"; /*I will get the value of x2 variable from memory*/  
                     
                         my_assembly = my_assembly + function+" @t"+available_t_reg+","+str01+","+str02+".\n"; /* R_function */
                         my_stack.push("@t"+available_t_reg);  /*push the result reg in stack*/
                         available_t_reg = (available_t_reg + 1)%8; /* 0->7 */
                         
                      }else if( var_reg_num(x1)==1 && var_reg_num(x2)==1 )/*x1 is reg and x2 is reg*/
                      {
                          available_t_reg = (available_t_reg-2)%8;   /*we pop two registers*/
                          my_assembly = my_assembly +function+" @t"+available_t_reg+","+x1+","+x2+".\n";
                          my_stack.push("@t"+available_t_reg); /*push the result reg in stack*/
                          available_t_reg = (available_t_reg+1)%8;   /*then increment the available register*/
                          
                      }else if( var_reg_num(x1)==1 && var_reg_num(x2)==0 )/*x1 is reg and x2 is variable*/
                      {
                         /*For the variable*/
                          boolean is_reg;
                          int index_var=-1;
                          String str;/*contains the type of my variable*/
                          String mor = variable_in_memory_or_register(x2+";"+function_in_name);
                           if(mor.startsWith("@s"))/*so the variable in stack memory*/
                           {
                             index_var =  ( -memory_variables.size()+memory_variables.indexOf(x2) )+sp_inti;/*contains the index of my variable*/
                            if(memory_variables.indexOf(x2)==-1)
                              return "error, not found the variable "+str1;
                             str="@s0";/*contains the type of my variable*/
                             is_reg = false;
                           }else/*so it's in reigter*/
                           {
                              is_reg = true;/*so the variable in a register*/
                              str = mor;   
                           }
                         
                          available_t_reg = (available_t_reg-1)%8;   /*we pop one registers*/
                          if(!is_reg)
                            my_assembly = my_assembly + "Load @s0,@sp,"+index_var+".\n"; /*I will get the value of x2 variable from memory*/
                          
                          my_assembly = my_assembly + function+" @t"+available_t_reg+","+str+","+x1+".\n"; 
                          my_stack.push("@t"+available_t_reg); /*push the result reg in stack*/
                          available_t_reg = (available_t_reg+1)%8;   /*then increment the available register*/
                      }else if(var_reg_num(x1)==0 && var_reg_num(x2)==1) /*x1 is variable and x2 is reg*/
                      {
                         /*For the variable*/
                         boolean is_reg;
                         int index_var=-1;
                         String str;/*contains the type of my variable*/
                         String mor = variable_in_memory_or_register(x1+";"+function_in_name);
                           if(mor.startsWith("@s"))/*so the variable in stack memory*/
                           {
                             index_var =  ( -memory_variables.size()+memory_variables.indexOf(x1) )+sp_inti;/*contains the index of my variable*/
                            if(memory_variables.indexOf(x1)==-1)
                               return "error, not found the variable "+str1;
                             str="@s0";/*contains the type of my variable*/
                             is_reg = false;
                           }else/*so it's in reigter*/
                           {
                              is_reg = true;/*so the variable in a register*/
                              str = mor;   
                           }
                         
                          available_t_reg = (available_t_reg-1)%8;   /*we pop one registers*/
                          if(!is_reg)
                            my_assembly = my_assembly + "Load @s0,@sp,"+index_var+".\n"; /*I will get the value of x2 variable from memory*/
                          
                          my_assembly = my_assembly + function+" @t"+available_t_reg+","+str+","+x2+".\n"; 
                          my_stack.push("@t"+available_t_reg); /*push the result reg in stack*/
                          available_t_reg = (available_t_reg+1)%8;   /*then ncrement the available register*/ 
                      }else if(var_reg_num(x1)==1 && var_reg_num(x2)==2 ) /*x1 is reg and x2 is number*/
                      {
                         int g = Integer.parseInt(x2); /*change String to int value*/                   
                         available_t_reg = (available_t_reg-1)%8;   /*we pop one registers*/
                         my_assembly = my_assembly +function+"I @t"+available_t_reg+","+x1+","+x2+".\n";
                         my_stack.push("@t"+available_t_reg); /*push the result reg in stack*/
                         available_t_reg = (available_t_reg+1)%8;   /*then increment the available register*/
                      }else if(var_reg_num(x1)==2 && var_reg_num(x2)==1) /*x1 is number and x2 is reg*/
                      {
                         int g = Integer.parseInt(x1); /*change String to int value*/                   
                         available_t_reg = (available_t_reg-1)%8;   /*we pop one registers*/
                         my_assembly = my_assembly +function+"I @t"+available_t_reg+","+x2+","+x1+".\n";
                         my_stack.push("@t"+available_t_reg); /*push the result reg in stack*/
                         available_t_reg = (available_t_reg+1)%8;   /*then increment the available register*/  
                      }
                          
                  }else /**/
                  {
                      my_stack.push(s1);
                  }
                }
                /*Handling the left variable which will be stored in it*/
                
                String str = variable_in_memory_or_register(str1+";"+function_in_name);/*contains the type of my variable*/
                if(str.startsWith("@s"))/*so it's in memory*/
                {
                  int index_var1 =  ( -memory_variables.size()+memory_variables.indexOf(str1) )+sp_inti;/*contains the index of my variable*/
                  if(memory_variables.indexOf(str1)==-1)
                     return "error, not found the variable "+str1;  
                  my_assembly = my_assembly + "Store @t0,@sp,"+index_var1+".\n";
                }else/*so it's in a register*/
                {
                  my_assembly = my_assembly + "LoadI "+str+",@t0,0"+".\n";
                }
  
                break;
               
            case 5:
                /****************************************
                 ***************   if  ******************
                 ****************************************/
                str1 = sentence.get(my_sentence_index);/* contains (vaiable or number);condition;(variable or number) */
                sk1 = new StringTokenizer(str1,";");
                if(sk1.countTokens()!=3)
                    return "error in if condition";
                str1 = sk1.nextToken(); /*vaiable or number*/
                str2 = sk1.nextToken(); /*condition*/
                str3 = sk1.nextToken(); /*vaiable or number*/
                
                available_t_reg = 0;
                int local_lable1 = lable_number; /*we copy it in a local variable becuase if an recusionn occuer will not affect on it*/
                lable_number = lable_number +2; /*we will use 2 lables in if codition*/
                
                if(var_reg_num(str1)==2 && var_reg_num(str3)==2 )/*str1 is number and str2 is number*/
                {
                    int n1,n2;
                    n1 = Integer.parseInt(str1);
                    n2 = Integer.parseInt(str3);
                    boolean bool; /*contains the result of if codition*/
              
                      if(str2.equals("=="))/*if the condition is == */
                      {
                          
                          if(n1==n2)
                            bool=true;/*if codition is true*/
                          else
                            bool=false;/*if condtion is false*/
                          
                      }else if(str2.equals(">"))/*if the condition is > */
                      {
                          if(n1>n2)
                            bool=true;
                          else
                            bool =false;
                      }else if(str2.equals("<"))/*if the condition is < */
                      {
                          if(n1<n2)
                            bool=true;
                          else
                            bool=false;
                      }else if(str2.equals(">="))/*if the condition is >= */
                      {
                          if(n1>=n2)
                            bool=true;
                          else
                            bool=false;
                      }else if(str2.equals("<=")) /*if the condition is <= */
                      {
                          if(n1<=n2)
                            bool=true;
                          else
                            bool=false;
                      }else/*if the condition is != */
                      {
                          if(n1!=n2)
                            bool=true;
                          else
                            bool=false;
                      }
                        
                        if(bool)/*The condition is true*/
                        {
                            /*** Make a recursion ***/
                            String s = to_assempply(my_sentence_index+2);/*ignor the if and { sentence, and send index after them*/
                            StringTokenizer sk0 = new StringTokenizer(s,";");/*to separate the index from assemply*/
                            my_sentence_index = Integer.parseInt(sk0.nextToken());/*contains the index*/
                            s = sk0.nextToken(); /*contains the assemply*/
                            if(s.startsWith("error"))/*if any error occurs in recurtion*/
                                return s;
                            my_assembly = my_assembly +s;
                        }else/*The condition is false*/
                        { /*the if result is false so i will ignor all code in if*/
                            while(sen_keys.get(my_sentence_index)!=7)/*ignore any thing before }*/
                                my_sentence_index++;
                        }
                }else 
                {
                   String instruction="";/*contains the instruction that should be used*/
                   boolean bool;/*it's false if the condition can be inverted and we have it's instruction to optomaitze the assembly*/
                   int n2;

                   if(var_reg_num(str1)==0 && var_reg_num(str3)==2)/*str1 is a variable and str3 is a number*/
                   {

                    n2 = Integer.parseInt(str3); /*contains my number*/
                    String mor = variable_in_memory_or_register(str1+";"+function_in_name);
                    if(mor.startsWith("@s"))/*so the variable in stack memory*/
                    {
                     /*to arrenge which will put first in register file the variable or number*/
                     int my_index01 =  ( -memory_variables.size()+memory_variables.indexOf(str1) )+sp_inti;
                     if(memory_variables.indexOf(str1)==-1)
                        return "error, not found the variable "+str1;
                     my_assembly = my_assembly + "Load @t"+available_t_reg+",@sp,"+my_index01+".\n";/*put my variable in a register*/
                     available_t_reg++;
                    }else /*so my variable is in a register*/
                    {
                       my_assembly = my_assembly + "LoadI @t"+available_t_reg+","+mor+",0.\n";
                       available_t_reg++;
                    }
                    my_assembly = my_assembly + "LoadI @t"+available_t_reg+",@zero,"+n2+".\n";/*load the number in a register*/
                    available_t_reg++;                       
                     
                   }
                   else if(var_reg_num(str1)==2 && var_reg_num(str3)==0)/*str1 is a number and str3 is a variable*/
                   {

                     n2 = Integer.parseInt(str1);
                    String mor = variable_in_memory_or_register(str3+";"+function_in_name);
                    if(mor.startsWith("@s"))/*so the variable in stack memory*/
                    {
                     /*to arrange which will put first in register file the variable or number*/
                     int my_index01 =  ( -memory_variables.size()+memory_variables.indexOf(str3) )+sp_inti ;
                     if(memory_variables.indexOf(str3)==-1)
                            return "error, not found the variable "+str3;
                     my_assembly = my_assembly + "Load @t"+available_t_reg+",@sp,"+my_index01+".\n";/*put my variable in a register*/
                     available_t_reg++;
                    }else /*so it's in a register*/
                    {
                        my_assembly = my_assembly + "LoadI @t"+available_t_reg+","+mor+",0.\n";
                        available_t_reg++; 
                    }
                     my_assembly = my_assembly + "LoadI @t"+available_t_reg+",@zero,"+n2+".\n";/*laod the number in a register*/
                     available_t_reg++;
                   }else /*Both str1 and str3 are variables*/
                   {
                       
                    /*First variable*/
                    String mor = variable_in_memory_or_register(str1+";"+function_in_name);
                    if(mor.startsWith("@s"))/*so the variable in stack memory*/
                    {
                     /*to arrange which will put first in register file the variable or number*/
                     int my_index01 =  ( -memory_variables.size()+memory_variables.indexOf(str1) )+sp_inti ;
                     if(memory_variables.indexOf(str1)==-1)
                            return "error, not found the variable "+str1;
                     my_assembly = my_assembly + "Load @t"+available_t_reg+",@sp,"+my_index01+".\n";/*put my variable in a register*/
                     available_t_reg++;
                    }else /*so my variable is in a register*/
                    {
                        my_assembly = my_assembly + "LoadI @t"+available_t_reg+","+mor+",0.\n";
                        available_t_reg++; 
                    }
                    
                    /*second variable*/
                    mor = variable_in_memory_or_register(str3+";"+function_in_name);
                    if(mor.startsWith("@s"))/*so the variable in stack memory*/
                    {
                     /*to arrange which will put first in register file the variable or number*/
                     int my_index01 =  ( -memory_variables.size()+memory_variables.indexOf(str3) )+sp_inti ;
                     if(memory_variables.indexOf(str3)==-1)
                         return "error not fount the variable " + str3;
                     my_assembly = my_assembly + "Load @t"+available_t_reg+",@sp,"+my_index01+".\n";/*put my variable in a register*/
                     available_t_reg++;
                    }else /*so my variable is in a register*/
                    {
                        my_assembly = my_assembly + "LoadI @t"+available_t_reg+","+mor+",0.\n";
                        available_t_reg++; 
                    }  
                     
                   }
                   
                   if(str2.equals("=="))/*if the condition is == */
                   { 
                       instruction = "beq";
                       bool=true;
                   }
                   else if(str2.equals(">"))/*if the condition is > */
                   {
                       instruction = "blt";
                       bool = true;
                   }
                   else if(str2.equals("<"))/*if the condition is < */
                   {
                       instruction = "bge";
                       bool = false;
                   }
                   else if(str2.equals(">="))/*if the condition is >= */
                   {
                       instruction = "bge";
                       bool = true;
                   }
                   else if(str2.equals("<=")) /*if the condition is <= */
                   {
                       instruction = "blt";
                       bool = true;
                   }
                   else/*if the condition is != */
                   {
                        instruction = "beq";
                        bool = false;
                   } 
                   
                   if(bool)
                   {
                       
                   if(str2.equals(">")) /*it's if, so I will put the lable after the codition*/                 
                       my_assembly = my_assembly + "blt @t"+(int)(available_t_reg-1)+",@t"+(int)(available_t_reg-2)+",Lable"+(int)(local_lable1)+".\nLable"+(int)(local_lable1+1)+" : ";
                   else if(str2.equals("<="))
                   {
                    /*it's if, so I will put the lable after the codition*/  
                     my_assembly = my_assembly + "blt @t"+(int)(available_t_reg-2)+",@t"+(int)(available_t_reg-1)+",Lable"+(local_lable1)+".\n";
                     my_assembly = my_assembly + "beq @t"+(int)(available_t_reg-2)+",@t"+(int)(available_t_reg-1)+",Lable"+(int)(local_lable1)+".\nLable"+(int)(local_lable1+1)+" : ";
                   }else
                   {
                     my_assembly = my_assembly + instruction +" @t"+(int)(available_t_reg-2)+",@t"+(int)(available_t_reg-1)+",Lable"+(int)(local_lable1)+".\nLable"+(int)(local_lable1+1)+" : ";
                   }

                   /*** recursion ***/
                   String s = to_assempply(my_sentence_index+2);/*ignor the if and { sentence, and send index after them*/
                   StringTokenizer sk0 = new StringTokenizer(s,";");/*to separate the index from assemply*/
                   my_sentence_index = Integer.parseInt(sk0.nextToken());/*contains the index*/
                   s = sk0.nextToken(); /*contains the assemply*/
                   if(s.startsWith("error"))/*if any error occurs in recurtion*/
                      return s;
                   tail_of_assemply = tail_of_assemply + "Lable"+(int)(local_lable1)+" : "+s+"Jump Lable"+(int)(local_lable1+1)+" .\n";
                   }else
                   {
                     /**/
                     my_assembly = my_assembly + instruction +" @t"+(int)(available_t_reg-2)+",@t"+(int)(available_t_reg-1)+",Lable"+(int)(local_lable1)+".\n";
                     /*** recursion ***/
                     String s = to_assempply(my_sentence_index+2);/*ignor the if and { sentence, and send index after them*/
                     StringTokenizer sk0 = new StringTokenizer(s,";");/*to separate the index from assemply*/
                     my_sentence_index = Integer.parseInt(sk0.nextToken());/*contains the index*/
                     s = sk0.nextToken(); /*contains the assemply*/
                     if(s.startsWith("error"))/*if any error occurs in recurtion*/
                       return s;
                     
                      my_assembly = my_assembly + s + "Lable" + (int)(local_lable1) + " : " ;
                     
                     available_s_reg = 2;
                   }
                   
                }
                break;
            case 8: 
                /**************************************************
                 *******************while**************************
                 **************************************************/
                /*It's very simmilar to if but the we check the condition again so we will put the returned lable before the codition*/
                str1 = sentence.get(my_sentence_index);/* contains (vaiable or number);condition;(variable or number) */
                sk1 = new StringTokenizer(str1,";");
                if(sk1.countTokens()!=3)
                    return "error in if condition";
                str1 = sk1.nextToken(); /*vaiable or number*/
                str2 = sk1.nextToken(); /*condition*/
                str3 = sk1.nextToken(); /*vaiable or number*/
                
                int local_lable = lable_number;
                lable_number = lable_number + 2;
                my_assembly = my_assembly + "Lable"+(int)(local_lable+1)+" : " ;
                
                if(var_reg_num(str1)==2 && var_reg_num(str3)==2 )/*str1 is number and str2 is number*/
                {
                    int n1,n2;
                    n1 = Integer.parseInt(str1);
                    n2 = Integer.parseInt(str3);
                    boolean bool; /*contains the result of if codition*/
              
                      if(str2.equals("=="))/*if the condition is == */
                      {
                          
                          if(n1==n2)
                            bool=true;/*if codition is true*/
                          else
                            bool=false;/*if condtion is false*/
                          
                      }else if(str2.equals(">"))/*if the condition is > */
                      {
                          if(n1>n2)
                            bool=true;
                          else
                            bool =false;
                      }else if(str2.equals("<"))/*if the condition is < */
                      {
                          if(n1<n2)
                            bool=true;
                          else
                            bool=false;
                      }else if(str2.equals(">="))/*if the condition is >= */
                      {
                          if(n1>=n2)
                            bool=true;
                          else
                            bool=false;
                      }else if(str2.equals("<=")) /*if the condition is <= */
                      {
                          if(n1<=n2)
                            bool=true;
                          else
                            bool=false;
                      }else/*if the condition is != */
                      {
                          if(n1!=n2)
                            bool=true;
                          else
                            bool=false;
                      }
                        
                        if(bool)/*The condition is true*/
                        {
                            /*** Make a recursion ***/
                            String s = to_assempply(my_sentence_index+2);/*ignor the if and { sentence, and send index after them*/
                            StringTokenizer sk0 = new StringTokenizer(s,";");/*to separate the index from assemply*/
                            my_sentence_index = Integer.parseInt(sk0.nextToken());/*contains the index*/
                            s = sk0.nextToken(); /*contains the assemply*/
                            if(s.startsWith("error"))/*if any error occurs in recurtion*/
                                return s;
                            my_assembly = my_assembly +s;
                        }else/*The condition is false*/
                        { /*the if result is false so i will ignor all code in if*/
                            while(sen_keys.get(my_sentence_index)!=7)/*ignore any thing before }*/
                                my_sentence_index++;
                        }
                }else 
                {
                   String instruction="";/*contains the instruction that should be used*/
                   boolean bool;/*it's false if the condition can be inverted and we have it's instruction to optomaitze the assembly*/
                   int n2;

                   if(var_reg_num(str1)==0 && var_reg_num(str3)==2)/*str1 is a variable and str3 is a number*/
                   {

                    n2 = Integer.parseInt(str3); /*contains my number*/
                    String mor = variable_in_memory_or_register(str1+";"+function_in_name);
                    if(mor.startsWith("@s"))/*so the variable in stack memory*/
                    {
                     /*to arrenge which will put first in register file the variable or number*/
                     int my_index01 =  ( -memory_variables.size()+memory_variables.indexOf(str1));
                     if(memory_variables.indexOf(str1)==-1)
                        return "error, not found the variable "+str1;
                     my_assembly = my_assembly + "Load @s"+available_s_reg+",@sp,"+my_index01+".\n";/*put my variable in a register*/
                     available_s_reg++;
                    }else /*so my variable is in a register*/
                    {
                       my_assembly = my_assembly + "LoadI @s"+available_s_reg+","+mor+",0.\n";
                       available_s_reg++;
                    }
                    my_assembly = my_assembly + "LoadI @s"+available_s_reg+",@zero,"+n2+".\n";/*load the number in a register*/
                    available_s_reg++;                       
                     
                   }
                   else if(var_reg_num(str1)==2 && var_reg_num(str3)==0)/*str1 is a number and str3 is a variable*/
                   {

                    n2 = Integer.parseInt(str1);
                    String mor = variable_in_memory_or_register(str3+";"+function_in_name);
                    if(mor.startsWith("@s"))/*so the variable in stack memory*/
                    {
                     /*to arrange which will put first in register file the variable or number*/
                     int my_index01 =  ( -memory_variables.size()+memory_variables.indexOf(str3) ) ;
                     if(memory_variables.indexOf(str3)==-1)
                            return "error, not found the variable "+str3;
                     my_assembly = my_assembly + "Load @s"+available_s_reg+",@sp,"+my_index01+".\n";/*put my variable in a register*/
                     available_s_reg++;
                    }else /*so it's in a register*/
                    {
                        my_assembly = my_assembly + "LoadI @s"+available_s_reg+","+mor+",0.\n";
                        available_s_reg++; 
                    }
                     my_assembly = my_assembly + "LoadI @s"+available_s_reg+",@zero,"+n2+".\n";/*laod the number in a register*/
                     available_s_reg++;
                   }else /*Both str1 and str3 are variables*/
                   {
                       
                    /*First variable*/
                    String mor = variable_in_memory_or_register(str1+";"+function_in_name);
                    if(mor.startsWith("@s"))/*so the variable in stack memory*/
                    {
                     /*to arrange which will put first in register file the variable or number*/
                     int my_index01 =  ( -memory_variables.size()+memory_variables.indexOf(str1) )+sp_inti ;
                     if(memory_variables.indexOf(str1)==-1)
                            return "error, not found the variable "+str1;
                     my_assembly = my_assembly + "Load @s"+available_s_reg+",@sp,"+my_index01+".\n";/*put my variable in a register*/
                     available_s_reg++;
                    }else /*so my variable is in a register*/
                    {
                        my_assembly = my_assembly + "LoadI @s"+available_s_reg+","+mor+",0.\n";
                        available_s_reg++; 
                    }
                    
                    /*second variable*/
                    mor = variable_in_memory_or_register(str3+";"+function_in_name);
                    if(mor.startsWith("@s"))/*so the variable in stack memory*/
                    {
                     /*to arrange which will put first in register file the variable or number*/
                     int my_index01 =  ( -memory_variables.size()+memory_variables.indexOf(str3) )+sp_inti ;
                     if(memory_variables.indexOf(str3)==-1)
                         return "error not fount the variable " + str3;
                     my_assembly = my_assembly + "Load @s"+available_s_reg+",@sp,"+my_index01+".\n";/*put my variable in a register*/
                     available_s_reg++;
                    }else /*so my variable is in a register*/
                    {
                        my_assembly = my_assembly + "LoadI @s"+available_s_reg+","+mor+",0.\n";
                        available_s_reg++; 
                    }  
                     
                   }
                   
                   if(str2.equals("=="))/*if the condition is == */
                   { 
                       instruction = "beq";
                       bool=true;
                   }
                   else if(str2.equals(">"))/*if the condition is > */
                   {
                       instruction = "blt";
                       bool = true;
                   }
                   else if(str2.equals("<"))/*if the condition is < */
                   {
                       instruction = "bge";
                       bool = false;
                   }
                   else if(str2.equals(">="))/*if the condition is >= */
                   {
                       instruction = "bge";
                       bool = true;
                   }
                   else if(str2.equals("<=")) /*if the condition is <= */
                   {
                       instruction = "blt";
                       bool = true;
                   }
                   else/*if the condition is != */
                   {
                        instruction = "beq";
                        bool = false;
                   } 
                   
                   if(bool)
                   {
                   
                   if(str2.equals(">"))
                           my_assembly = my_assembly +"blt @s"+(int)(available_s_reg-1)+",@s"+(int)(available_s_reg-2)+",Lable"+(int)(local_lable)+".\n";
                      
                   else if(str2.equals("<="))
                   {
                       
                           my_assembly = my_assembly + "blt @s"+(int)(available_s_reg-2)+",@s"+(int)(available_s_reg-1)+",Lable"+(int)(local_lable)+".\n";
                           my_assembly = my_assembly + "beq @s"+(int)(available_s_reg-2)+",@s"+(int)(available_s_reg-1)+",Lable"+(int)(local_lable)+".\n";
                       
                   }else
                   {
                      
                          my_assembly = my_assembly + instruction +" @s"+(int)(available_s_reg-2)+",@s"+(int)(available_s_reg-1)+",Lable"+(int)(local_lable)+".\n";
                      
                   }
                   /*** recursion ***/
                   String s = to_assempply(my_sentence_index+2);/*ignor the if and { sentence, and send index after them*/
                   StringTokenizer sk0 = new StringTokenizer(s,";");/*to separate the index from assemply*/
                    try{
                    s = sk0.nextToken();     
                    my_sentence_index = Integer.parseInt(s);/*contains the index*/
                    }catch(Exception e){return s;}/*if any error occur in exception*/
                   s = sk0.nextToken(); /*contains the assemply*/
                   if(s.startsWith("error"))/*if any error occurs in recursion*/
                      return s;
                   tail_of_assemply = tail_of_assemply + "Lable"+(int)(local_lable)+" : "+s+"Jump Lable"+(int)(local_lable+1)+" .\n";
                   
                   }else
                   {
                     my_assembly = my_assembly + instruction +" @s"+(int)(available_s_reg-2)+",@s"+(int)(available_s_reg-1)+",Lable"+(int)(local_lable)+".\n";
                      
                     /*** recursion ***/
                     String s = to_assempply(my_sentence_index+2);/*ignor the if and { sentence, and send index after them*/
                     StringTokenizer sk0 = new StringTokenizer(s,";");/*to separate the index from assemply*/
                     try{
                     s = sk0.nextToken();
                     my_sentence_index = Integer.parseInt(s);/*contains the index*/
                     }catch(Exception e){return s;}/*if any error in recursion*/
                     s = sk0.nextToken(); /*contains the assemply*/
                     if(s.startsWith("error"))/*if any error occurs in recurtion*/
                       return s;
                     
                        my_assembly = my_assembly + s ;
                        my_assembly = my_assembly + "Jump Lable"+(int)(local_lable+1)+".\n"+ "Lable" + (int)(local_lable) + " : " ;
                    
                     available_s_reg =  2; /*becuse we use @s registers from @s2, becuse @s0 and @s1 for variables*/
                   }
                   
                }

                break;
            case 9:
                /********************************************
                 ******************function******************
                 ********************************************/
                 str1 = sentence.get(my_sentence_index); /*contains the function sentence*/
                 sk1  = new StringTokenizer(str1,";");
                 str1 = sk1.nextToken(); /*conatains the function name*/
                 function_in_name = str1.trim();
                 int count=0;
                 count_push_for_return=0; /*make it zero*/
                 if(sk1.countTokens()>8)/*I have 4 argment registers only*/
                   return "error to many arrguments, at most 4 arguments only";
                 
                 while(sk1.hasMoreTokens())/*loop to take all arguments*/
                 {
                     str2 = sk1.nextToken();/*contains the argument type*/
                     str3 = sk1.nextToken();/*contains the argument name*/
                     reg_variables.add("@a"+count+";"+str3+";"+str1);/*@aX;arrgument name ; function name*/
                     count++;
                 }
                 function_names.add(str1);
                 arg_num.add(count);
                 
                 my_assembly = my_assembly + str1 + " : ";/*put a lable of function name at the beganning of a function */
                 /*** recursion ***/
                 String s = to_assempply(my_sentence_index+2);/*ignor the function and { sentence, and send index after them*/
                 StringTokenizer sk0 = new StringTokenizer(s,";");/*to separate the index from assemply*/
                 try{
                 my_sentence_index = Integer.parseInt(sk0.nextToken());/*contains the index*/
                 }catch(Exception e){}
                 if(sk0.hasMoreTokens())
                   s = sk0.nextToken(); /*contains the assemply*/
                 else
                   s="";/*The end of the code and no assemply present*/
                 if(s.startsWith("error")||s.startsWith("ok"))/*if any error occurs in recurtion*/
                    return s;
                 
                 if(function_in_name.equals("main"))
                   my_assembly = my_assembly +s+"end-labe : Jump end-labe.\n";  
                 else    
                   my_assembly = my_assembly +s+"Jr @ra.\n\n";/*Put the value of @ra register in PC */
                 
                 lable_number = lable_number + 2;
                 memory_variables.clear(); /*Clear the arrayList at the end of the function*/
                 memory_variables_type.clear();
                 break;
            case 11:/*calling with a return*/  
            case 10:/*calling without return*/
                /***********************************************
                 ****************function calling***************
                 ***********************************************/
                String my_return_variable="";
                ArrayList<Integer>temp=new ArrayList<>();
                str1 = sentence.get(my_sentence_index); /*contains the calling function sentence*/
                sk1  = new StringTokenizer(str1,";");
                if(sentence_key==11)/*The first tokein is the variable*/
                    my_return_variable = sk1.nextToken();/*contains the variable*/
                str1 = sk1.nextToken(); /*conatains the calling function name*/
                if(!function_names.contains(str1))/*check if function name is exist or not*/
                   return "error not found the function name";
                if( arg_num.get( function_names.indexOf(str1) )!=sk1.countTokens())/*The arguments should has the size is stringTokonizer size*/
                   return "error the arguments number is not correct";
                
                count=0;
                 /*save the current address of @ra in memory*/
                 my_assembly = my_assembly + "Store @ra,@sp,0.\n";
                 my_assembly = my_assembly + "Push .\n";
                 memory_variables.push("temp");  /*to handle the index of the variable*/
                 memory_variables_type.push("temp");
                 
                 
                while(sk1.hasMoreTokens())
                {
                    /*First, save the @a registers in memory be avoid overwritting on it*/
                    my_assembly = my_assembly + "Store @a"+count+",@sp,0.\n";
                    my_assembly = my_assembly + "Push .\n";
                    memory_variables.push("temp");  /*to handle the index of the variable*/
                    memory_variables_type.push("temp");
    
                    
                    str2 = sk1.nextToken();/*contains the argument*/
                    if(var_reg_num(str2)==2)/*if the arrgument is a number*/
                      my_assembly = my_assembly + "LoadI @a"+count+",@zero,"+str2+".\n";
                    else/*so it's a variable*/
                    {
                       String sth = variable_in_memory_or_register(str2+";"+function_in_name);
                       if(sth.startsWith("@s"))/*so it in memory*/
                       {
                       int index_var =  ( -memory_variables.size()+memory_variables.indexOf(str2) )+sp_inti;/*The index of my variable*/
                       if(memory_variables.indexOf(str2)==-1)
                            return "error, not found the variable "+str2;
                       my_assembly = my_assembly + "Load @a"+count+",@sp,"+index_var+".\n";
                      }else/*it's in a register*/
                      {
                         my_assembly = my_assembly + "LoadI @a"+count+","+sth+",0.\n";
                      }
                    
                    }
                    
                    
                    count++;
                }
                
                
                
                my_assembly = my_assembly + "Jal "+str1+".\n";/*will save the next address in @ra and then jump to the lable*/
                
                int iu=0;
                int xx = memory_variables.size();
                for(int ii=count-1;ii>=0;ii--)
                { /*return the @a register from memory*/
                    my_assembly = my_assembly + "Pop .\n"; 
                    my_assembly = my_assembly + "Load @a"+ii+",@sp,0.\n";
                    memory_variables.pop(); /*remove form array*/
                    memory_variables_type.pop();
                    iu++;
                }
                /*return the @ra registers from memory*/
                my_assembly = my_assembly + "Pop .\n"; 
                my_assembly = my_assembly + "Load @ra,@sp,0.\n";
                memory_variables.pop();
                memory_variables_type.pop();

                
                if(sentence_key==11)/*so I should return in a variable*/
                {
                    str1 = variable_in_memory_or_register(my_return_variable+";"+function_in_name);
                    if(str1.startsWith("@s"))/*so it's in memeory*/
                    {
                       int my_var_index =  ( -memory_variables.size()+memory_variables.indexOf(my_return_variable) )+sp_inti;
                       if(memory_variables.indexOf(my_return_variable)==-1)
                          return "error, not found the variable "+my_return_variable;
                       my_assembly = my_assembly +"Store @v0,@sp,"+my_var_index+".\n";
                    }else/*so it's in a register*/
                    {
                        my_assembly = my_assembly + "LoadI "+str1+",@v0,0.\n";
                    }
                }
                    

                break;
                
          
        }
           my_sentence_index++;
     }catch(Exception e){return "error in "+sentence.get(my_sentence_index)+e;}
        }/*while bracet*/
        
   
//        System.out.println(my_assembly+"\n"+tail_of_assemply); 
        return my_assembly+"\n"+tail_of_assemply;
    }
    
    String variable_in_memory_or_register(String s)
    {
       
        if(reg_variables.contains("@a0;"+s))/*check if the @aX;name;fun_name is in reg_variable arraylist or not*/   
        {
            return "@a0";
        }else if( reg_variables.contains("@a1;"+s) )
        {
           return "@a1"; 
        }else if (reg_variables.contains("@a2;"+s) )
        {
           return "@a2";
        }else if(reg_variables.contains("@a3;"+s))
        {
            return "@a3";  
        }
        else/*so the variable in stack memory*/
        {
          return "@s0";
        }
    }
    
    /* This function is to check if the string is integer or not */
    int var_reg_num(String s) /*returns 0 if variable and 1 if reg and 2 if number*/
    {
        try {
            Integer.parseInt(s);
            return 2;
        } catch (Exception e) { /*will gives exeption if it is not integer*/
            /*reg or variable*/
            if(s.startsWith("@")) /*all registers starts with @, but variable not*/
                return 1;
            else
                return 0;
        }
  
    }

    /* This function is to check if the string is float/int or not */
    Boolean is_float(String s)
    {
       
        try {
            Float.parseFloat(s);
            return true;
        } catch (Exception e) { /*will gives exeption if it is not float*/
            return false;
        } 
    }
    
    /* This function returns Add,Sub,Mul or Div according to parameter +,-,*,/ */
    String operator_to_op_code(String s )
    {
        if(s.equals("+"))
          return "Add";
        else if(s.equals("-"))
           return "Sub";
        else if(s.equals("*"))
            return "Mul";
        else if(s.equals("/"))
            return "Div";
        else
            return "error";
    }
    
    /*********************************************************************
     *********************************************************************
     *********************************************************************
     *********************************************************************
     **************************assembler part*****************************
     *********************************************************************
     *********************************************************************
     *********************************************************************
     *********************************************************************/
    /*VARIABLES*/
    boolean is_code_wrigh; /*True if the code is true, false if not*/
    char end_line;    /*Tells that the line of code is ended*/
    String machine_code;/*The machine code which is translated */
    char reg_symbol;  /*The symbol which puts before the register*/
    int ROM_size;     /*The size of ROM in CPU*/
    String R_format_op;/*The op_code of R format instruction*/ 
    String F_format_op;/*The op_code of R format instruction*/ 
    String add_fun;   /*The op_code of add instruction*/ 
    String sub_fun;   /*The op_code of sub instruction*/ 
    String mul_fun;   /*The op_code of mul instruction*/ 
    String div_fun;   /*The op_code of div instruction*/
    String and_fun;   /*The op_code of and instruction*/
    String or_fun;   /*The op_code of or instruction*/
    String xor_fun;   /*The op_code of xor instruction*/
    String slt_fun;   /*The op_code of slt instruction*/
    String jal_op;    /*The op code of Jal instruction*/
    String jr_op;     /*The op_code of Jr instruction*/

    String shmt_op;   /*The op_code of div instruction*/
    String beq_op;    /*The op_code of beq instruction*/
    String blt_op;    /*The op_code of blt instruction*/
    String bge_op;    /*The op_code of bge instruction*/
    String jump_op;   /*The op_code of jump instruction*/
    String load_op;  /*The op_code of load from memory instruction*/
    String loadI_op;  /*The op_code of load from memory instruction*/
    String subi_op;
    String store_op;  /*The op_code of load from memory instruction*/
    String loadm_op;  /*The op_code of load from memory instruction*/

    String addf_fun;   /*The op_code of add_float instruction*/ 
    String subf_fun;   /*The op_code of sub_float instruction*/ 
    String mulf_fun;   /*The op_code of mul_float instruction*/ 
    String divf_fun;   /*The op_code of div_float instruction*/

    String do_nothing_op; /*The op_code of do nothing*/



    /*Registers names*/
    String R0="zero";  
    String R1="at";  
    String R2="v0";  
    String R3="v1";  
    String R4="a0";  
    String R5="a1";  
    String R6="a2";  
    String R7="a3";  
    String R8="t0";  
    String R9="t1";  
    String R10="t2";  
    String R11="t3";  
    String R12="t4";  
    String R13="t5";  
    String R14="t6";  
    String R15="t7";  
    String R16="s0";  
    String R17="s1";  
    String R18="s2";  
    String R19="s3";  
    String R20="s4";  
    String R21="s5";  
    String R22="s6";  
    String R23="s7";  
    String R24="t8";  
    String R25="t9";  
    String R26="k0";  
    String R27="k1";  
    String R28="gp";  
    String R29="sp";  
    String R30="fp";  
    String R31="ra";  
    ArrayList<String> code_lines;
    
    public void intit_assembly() {
        end_line='.';
        ROM_size=256;
        R_format_op="000000";
        add_fun="100000";
        sub_fun="100010";
        mul_fun="101000";
        div_fun="000110";
        and_fun="000100";
        or_fun ="001100";
        xor_fun="001110";
        slt_fun="001010";
        shmt_op="00000";
        
        beq_op  ="000100";
        blt_op  ="010100";
        bge_op  ="100100";
        jump_op ="000010";
        load_op ="100011";
        loadI_op="110011";
        subi_op ="110100";
        loadm_op="001000";
        store_op="101011";
        
        F_format_op="000000";
        addf_fun =  "100000";
        subf_fun =  "100010";
        mulf_fun =  "101000";
        divf_fun =  "000110";
        
        jal_op = "100010";
        jr_op  = "110010";
        
        do_nothing_op="000000";
        reg_symbol='@';
    }
    
    /****************************************************************************
      This fill a binary string to a suitable number of bit for Ex bianry 10, I want to
      fill it to 5 bits, so it will be 00010
    *****************************************************************************/
    private String fill_binary_to_n_digets(int n , String s)
    {
        if(s.length()<16)
        {
        while(s.length()!=n)
        {
            s="0"+s;
        }
        }else if(s.length()>16)
        {
           s = s.substring(s.length()-16);
        }
        
        return s;
    }
    
    /*This function change the register name to it's binary represenaion*/
    private String change_Reg_to_01(String s)
    {
        if(s.equals(R0))
            return "00000";
        else if(s.equals(R1))
            return "00001";
        else if(s.equals(R2))
            return "00010";
        else if(s.equals(R3))
            return "00011";
        else if(s.equals(R4))
            return "00100";
        else if(s.equals(R5))
            return "00101";
        else if(s.equals(R6))
            return "00110";
        else if(s.equals(R7))
            return "00111";
        else if(s.equals(R8))
            return "01000";
        else if(s.equals(R9))
            return "01001";
        else if(s.equals(R10))
            return "01010";
        else if(s.equals(R11))
            return "01011";
        else if(s.equals(R12))
            return "01100";
        else if(s.equals(R13))
            return "01101";
        else if(s.equals(R14))
            return "01110";
        else if(s.equals(R15))
            return "01111";
        else if(s.equals(R16))
            return "10000";
        else if(s.equals(R17))
            return "10001";
        else if(s.equals(R18))
            return "10010";
        else if(s.equals(R19))
            return "10011";
        else if(s.equals(R20))
            return "10100";
        else if(s.equals(R21))
            return "10101";
        else if(s.equals(R22))
            return "10110";
        else if(s.equals(R23))
            return "10111";
        else if(s.equals(R24))
            return "11000";
        else if(s.equals(R25))
            return "11001";
        else if(s.equals(R26))
            return "11010";
        else if(s.equals(R27))
            return "11011";
        else if(s.equals(R28))
            return "11100";
        else if(s.equals(R29))
            return "11101";
        else if(s.equals(R30))
            return "11110";
        else if(s.equals(R31))
            return "11111";
        
        return "error";   
    }
    
    String check_R_format(String s)
    {
        StringTokenizer s1 = new StringTokenizer(s,",");
        
        if(s1.countTokens()!=3)
        {
            /*The code is not correct, thier is a missed or added register*/
            return "error_reg_mis";
        }
        
        String temp;
        String Reg1,Reg2,Reg3;
        
        /*First Register*/
        temp = s1.nextToken().trim();
        if(!temp.startsWith(reg_symbol+""))
        {
            /*error in symbol*/
            return "error_symbol";
        }
        
        Reg1 = change_Reg_to_01(temp.substring(1));
        if(Reg1.equals("error"))
        {
            /*error in register name*/
            return "error_reg_name";

        }
        
        /*Second Register*/
        temp = s1.nextToken().trim();
        if(!temp.startsWith(reg_symbol+""))
        {
            /*error in symbol*/
            return "error_symbol";
        }
        
        Reg2 = change_Reg_to_01(temp.substring(1));
        if(Reg2.equals("error"))
        {
            /*error in register name*/
            return "error_reg_name";            
        }
        
        /*third Register*/
        temp = s1.nextToken().trim();
        if(!temp.startsWith(reg_symbol+""))
        {
            /*error in symbol*/
            return "error_symbol";
        }
        
        Reg3 = change_Reg_to_01(temp.substring(1));
        if(Reg3.equals("error"))
        {
            /*error in register name*/
            return "error_reg_name";
        }
        
        return Reg2+Reg3+Reg1;
   
      }
    
    
     String check_I_format(String s)
    {
       StringTokenizer s1 = new StringTokenizer(s,",");
        
        if(s1.countTokens()!=3)
        {
            /*The code is not correct, thier is a missed or added register*/
            return "error_reg_mis";
        }
        
        String Reg1,Reg2,imm,temp;
        
        /*First Register*/
        temp = s1.nextToken().trim();
        if(!temp.startsWith(reg_symbol+""))
        {
            /*error in symbol*/
            return "error_symbol";
        }
        
        Reg1 = change_Reg_to_01(temp.substring(1));
        if(Reg1.equals("error"))
        {
            /*error in register name*/
            return "error_reg_name";
        } 
        
        /*second Reg*/
        temp = s1.nextToken().trim();
        if(!temp.startsWith(reg_symbol+""))
        {
            /*error in symbol*/
            return "error_symbol";
        }
        
        Reg2 = change_Reg_to_01(temp.substring(1));
        if(Reg2.equals("error"))
        {
            /*error in register name*/
            return "error_reg_name";
        } 
        
        /*Handel the imm data*/
        int x;
        imm=s1.nextToken().trim();
        x= Integer.parseInt(imm);
        imm=Integer.toBinaryString(x);
        System.out.println("____)("+imm);
        imm=fill_binary_to_n_digets(16,imm);
        
        System.out.println("----"+imm);
        return Reg2+Reg1+imm;
    }
    
    String check_RI_format(String s)
    {
       StringTokenizer s1 = new StringTokenizer(s,",");
        
        if(s1.countTokens()!=2)
        {
            /*The code is not correct, thier is a missed or added register*/
            return "error_reg_mis";
        }
        
        String Reg1,imm,temp;
        
        /*First Register*/
        temp = s1.nextToken().trim();
        if(!temp.startsWith(reg_symbol+""))
        {
            /*error in symbol*/
            return "error_symbol";
        }
        
        Reg1 = change_Reg_to_01(temp.substring(1));
        if(Reg1.equals("error"))
        {
            /*error in register name*/
            return "error_reg_name";
        } 
        
        /*Handel the imm data*/
        int x;
        imm=s1.nextToken().trim();
        x= Integer.parseInt(imm);
        
        imm=Integer.toBinaryString(x);
        imm=fill_binary_to_n_digets(21,imm);
        
        return Reg1+imm;
    }
    
    String check_branch(String s)
    {
        StringTokenizer s1 = new StringTokenizer(s,",");
        
        if(s1.countTokens()!=3)
        {
            /*The code is not correct, thier is a missed or added register*/
            return "error_reg_mis";
        }
        
        String temp;
        String Reg1,Reg2,lable;
        
        /*First Register*/
        temp = s1.nextToken().trim();
        if(!temp.startsWith(reg_symbol+""))
        {
            /*error in symbol*/
            return "error_symbol";
        }
        
        Reg1 = change_Reg_to_01(temp.substring(1));
        if(Reg1.equals("error"))
        {
            /*error in register name*/
            return "error_reg_name";

        }
        
        /*Second Register*/
        temp = s1.nextToken().trim();
        if(!temp.startsWith(reg_symbol+""))
        {
            /*error in symbol*/
            return "error_symbol";
        }
        
        Reg2 = change_Reg_to_01(temp.substring(1));
        if(Reg2.equals("error"))
        {
            /*error in register name*/
            return "error_reg_name";            
        }
        
        /*Handel the lable*/
        
        temp = s1.nextToken().trim();

        for(int i=0;i<code_lines.size();i++)
        {
            if(code_lines.get(i).startsWith(temp+" :") || code_lines.get(i).startsWith(temp+"  :") 
            || code_lines.get(i).startsWith(temp+"   :") || code_lines.get(i).startsWith(temp+":") )
            {
                lable = Integer.toBinaryString(i);
                lable = fill_binary_to_n_digets(16,lable); /*The addreess is 16 bit in instruction*/
                
                return Reg1+Reg2+lable;
            }else if(code_lines.get(i).contains(s) && code_lines.get(i).contains(":") )/*it contains my lable and : */
            {
               String s123 = code_lines.get(i);
               s123 = s123.substring(s123.indexOf(":")+1).trim();
               if(s123.startsWith(s))
               {
                lable = Integer.toBinaryString(i);
                lable = fill_binary_to_n_digets(16,lable); /*The addreess is 16 bit in instruction*/
                
                return Reg1+Reg2+lable;
               }
            }
        }
        
        return "error lable "+temp+" is not found";
       
    }
    
    String check_jump(String s)
    {
         /*Handel the lable*/
        String lable;

        for(int i=0;i<code_lines.size();i++)
        {
           
            if(code_lines.get(i).startsWith(s))
            {
                lable = Integer.toBinaryString(i);
                lable = fill_binary_to_n_digets(26,lable);/*The address is 16 bit in jump instruction*/
                
                return lable;
            }else if(code_lines.get(i).contains(s) && code_lines.get(i).contains(":") )/*it contains my lable and : */
            {
               String s123 = code_lines.get(i);
               s123 = s123.substring(s123.indexOf(":")+1).trim();
               if(s123.startsWith(s))
               {
               lable = Integer.toBinaryString(i);
               lable = fill_binary_to_n_digets(26,lable);/*The address is 16 bit in jump instruction*/
                
                return lable; 
               }
            }
        }
        
        return "error_no_lable";
    }
    
    

    
    
    /**/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_code = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jButton_run = new javax.swing.JButton();
        jButton_compile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1070, 600));
        setSize(new java.awt.Dimension(1070, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0,80));

        jTextArea_code.setColumns(20);
        jTextArea_code.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextArea_code.setRows(5);
        jScrollPane1.setViewportView(jTextArea_code);

        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\mohammedhamdy32\\Desktop\\ComplierDesign\\src\\Imag\\128x128.png")); // NOI18N

        jButton_run.setBackground(new java.awt.Color(0, 114, 162));
        jButton_run.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton_run.setForeground(new java.awt.Color(0, 51, 51));
        jButton_run.setText("Run");
        jButton_run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_runActionPerformed(evt);
            }
        });

        jButton_compile.setBackground(new java.awt.Color(0, 114, 162));
        jButton_compile.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton_compile.setForeground(new java.awt.Color(0, 51, 51));
        jButton_compile.setText("Compile");
        jButton_compile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_compileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jButton_compile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(jButton_run, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_run, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_compile, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 63, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 1000, 410));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\mohammedhamdy32\\Desktop\\ComplierDesign\\src\\Imag\\Universal.jpeg")); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    String assembly="";
    boolean is_compile_true=false;
    private void jButton_runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_runActionPerformed
        intit_assembly();
        
        if(is_compile_true)
        {
           
        String my_code;
        int instructoins_count;
        is_code_wrigh = true;
        my_code = assembly.trim();
        code_lines = new ArrayList<String>();
        StringTokenizer s1 = new StringTokenizer(my_code,end_line+"");
        instructoins_count=s1.countTokens();
        
        if(instructoins_count>ROM_size)
        {
            JOptionPane.showMessageDialog(this,"The ROM is "+ROM_size+"instuctions only, you write "+instructoins_count);
            is_code_wrigh=false;
        }

        String s,st1,st2,st3;
        StringTokenizer sk1;
        while(s1.hasMoreTokens())
        {
            s = s1.nextToken().trim();
            System.out.println(s);
            if(s.startsWith("MulI"))/*MulI @x0,@x1,9*/
            {
                s = s.substring(4).trim() ;
                sk1 = new StringTokenizer(s,",");
                System.out.println(sk1.countTokens());
                st1 = sk1.nextToken();/*first register*/
                st2 = sk1.nextToken();/*second register*/
                st3 = sk1.nextToken();/*The number*/
                code_lines.add("LoadI @at,@zero,"+st3);/*put number 3 in @at register*/
                code_lines.add("Mul "+st1+",@at,"+st2);
            }else if(s.startsWith("DivI"))
            {
                s = s.substring(4).trim() ;
                sk1 = new StringTokenizer(s,",");
                st1 = sk1.nextToken();/*first register*/
                st2 = sk1.nextToken();/*second register*/
                st3 = sk1.nextToken();/*The number*/
                code_lines.add("LoadI @at,@zero,"+st3);/*put number 3 in @at register*/
                code_lines.add("Div "+st1+","+st2+",@at"); 
            }else
            code_lines.add(s.trim());
        }

        String line;
        machine_code="";

        int i;
        for(i=0;i<code_lines.size();i++)
        {
            line = code_lines.get(i);
            machine_code = machine_code + "\""; /*add " to the start of machine code*/           

            /*To remove the lable if exist*/
            while(line.contains(":"))
            {
                try{
                    line=line.substring( line.indexOf(":")+1 ).trim();
                }catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this,"error in code in : , in assembler");
                    is_code_wrigh=false;
                }
            }
            System.out.println("!!!!!!!!!!!!!"+line);
            /*To check the operation*/
            /****************ADD*******************/
            if(line.startsWith("Add "))
            {
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + R_format_op + temp + shmt_op + add_fun ;
                //System.out.println(temp );  /*To print the registers*/
                //System.out.print(shmt_op);
                //System.out.print(add_fun+" ");
                //System.out.println( "   --"+line);

            }
            /****************SUB********************/
            else if(line.startsWith("Sub "))
            {
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + R_format_op + temp + shmt_op + sub_fun  ;

                // System.out.println(temp);
                // System.out.print(shmt_op);
                //System.out.print(sub_fun+ " ");
                // System.out.println( "   --"+line); /*To print the registers*/

            }
            /******************MUL*********************/
            else if(line.startsWith("Mul "))
            {
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + R_format_op + temp + shmt_op + mul_fun ;
                //System.out.println(temp);
                //System.out.print(shmt_op);
                //System.out.print(mul_fun+" ");
                //System.out.println( "   --"+line); /*To print the registers*/

            }
            /******************div**********************/
            else if(line.startsWith("Div "))
            {
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + R_format_op + temp + shmt_op + div_fun ;
                //System.out.print(temp);
                //System.out.print(shmt_op);
                //System.out.print(div_fun+" ");
                //System.out.println("   --"+line);

            }
            /****************And******************/
            else if(line.startsWith("And "))
            {
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + R_format_op + temp + shmt_op + and_fun ;
                //System.out.print(temp);
                //System.out.print(shmt_op);
                //System.out.print(div_fun+" ");
                //System.out.println("   --"+line);
            }
            /******************Or*********************/
            else if(line.startsWith("Or "))
            {
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(3).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + R_format_op + temp + shmt_op + or_fun ;
                //System.out.print(temp);
                //System.out.print(shmt_op);
                //System.out.print(div_fun+" ");
                //System.out.println("   --"+line);
            }
            /******************Xor**********************/
            else if(line.startsWith("Xor "))
            {
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + R_format_op + temp + shmt_op + xor_fun ;
                //System.out.print(temp);
                //System.out.print(shmt_op);
                //System.out.print(div_fun+" ");
                //System.out.println("   --"+line);
            }
            /********************Slt***********************/
            else if(line.startsWith("Slt ")){
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + R_format_op + temp + shmt_op + slt_fun ;
                //System.out.print(temp);
                //System.out.print(shmt_op);
                //System.out.print(div_fun+" ");
                //System.out.println("   --"+line);
            }
            /*******************beq*******************/
            else if(line.startsWith("beq "))
            {
                //System.out.print(beq_op+" ");
                String temp = check_branch(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,temp+", in beq in assembler");
                    is_code_wrigh = false;
                    break;
                }
                /*temp contains the registers and the adderss*/
                machine_code = machine_code + beq_op + temp ;
                //System.out.println(temp+ "   --"+line);

            }
            /*******************blt*******************/
            else if(line.startsWith("blt "))
            {
                String temp = check_branch(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,temp+",in blt in assembler");
                    is_code_wrigh = false;
                    break;
                }
                /*temp contains the registers and the adderss*/
                machine_code = machine_code + blt_op + temp ;
                //System.out.println(temp+ "   --"+line);

            }
            /*******************bge*******************/
            else if(line.startsWith("bge "))
            {
                //System.out.print(beq_op+" ");
                String temp = check_branch(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,temp+",in bge in assembler");
                    is_code_wrigh = false;
                    break;
                }
                /*temp contains the registers and the adderss*/
                machine_code = machine_code + bge_op + temp ;
                //System.out.println(temp+ "   --"+line);

            }
            /*****************Jump****************/
            else if(line.startsWith("Jump "))
            {
                //System.out.print(jump_op+" ");
                String temp = check_jump(line.substring(5).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,temp+",in jump in assembler");
                    is_code_wrigh = false;
                    break;
                }
                machine_code = machine_code + jump_op + temp ;
                //System.out.println(temp+ "   --"+line);
            }
            /****************Load*********************/
            else if(line.startsWith("Load "))
            {
                //System.out.print(load_op+" ");
                String temp;
                try{
                    temp = check_I_format(line.substring(5).trim());
                }catch(Exception e)
                {
                    /*if the substring gives exception*/
                    JOptionPane.showMessageDialog(this,"error in load, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                machine_code = machine_code + load_op + temp ;
                //System.out.println(temp+ "   --"+line);
            }
            /****************LoadI*********************/
            else if(line.startsWith("LoadI ") )
            {
                //System.out.print(loadi_op+" ");
                String temp;
                try{
                    temp = check_I_format(line.substring(6).trim());
                }catch(Exception e)
                {
                    /*if the substring gives exception*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                machine_code = machine_code + loadI_op + temp ;
                //System.out.println(temp+ "   --"+line);
            }
            /****************AddI*******************/
            else if(line.startsWith("AddI ") )/*it's LoadI*/
            {
                //System.out.print(loadi_op+" ");
                String temp;
                try{
                    temp = check_I_format(line.substring(5).trim());
                }catch(Exception e)
                {
                    /*if the substring gives exception*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                machine_code = machine_code + loadI_op + temp ; /*AddI is similler to LoadI*/
                //System.out.println(temp+ "   --"+line);
            }
            /****************SubI*********************/
            else if(line.startsWith("SubI "))
            {
                //System.out.print(loadi_op+" ");
                String temp;
                try{
                    temp = check_I_format(line.substring(5).trim());
                }catch(Exception e)
                {
                    /*if the substring gives exception*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                machine_code = machine_code + subi_op + temp ;
                //System.out.println(temp+ "   --"+line);
            }else if(line.startsWith("MulI "))
            {

            }
            /***************Push ******************/
            else if(line.startsWith("Push") )
            { /*I will increment the @sp register only*/
                String temp;

                temp = check_I_format("@sp,@sp,1"); /*Increment the @sp reigster*/

                machine_code = machine_code + loadI_op + temp ; /*AddI is similler to LoadI*/
            }
            /*******************Pop*******************/
            else if(line.startsWith("Pop") )
            { /*I will increment the @sp register only*/
                String temp;

                temp = check_I_format("@sp,@sp,1"); /*decrement the @sp reigster*/

                machine_code = machine_code + subi_op + temp ; /*AddI is similler to LoadI*/
            }
            /********************Store*********************/
            else if(line.startsWith("Store "))
            {
                //System.out.print(store_op+" ");
                String temp;
                try{
                    temp = check_I_format(line.substring(6).trim());
                }catch(Exception e)
                {
                    /*if the substring gives exception*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }
                machine_code = machine_code + store_op + temp ;
                //System.out.println(temp+ "   --"+line);
                /***********************Add float****************************/
            }
            /*************************J********************************/
            else if(line.startsWith("Jal "))
            {
                //System.out.print(jump_op+" ");
                String temp = check_jump(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,temp+", in assembler");
                    is_code_wrigh = false;
                    break;
                }
                machine_code = machine_code + jal_op + temp ;
            }
            /**********************************************************/
            else if(line.startsWith("Jr "))
            {
                //                String reg = line.substring(2).trim();
                //                reg = change_Reg_to_01(reg.substring(1));
                //                reg = fill_binary_to_n_digets(26,reg);
                machine_code = machine_code + jr_op +"11111000000000000000000000";
            }
            else if(line.startsWith("AddF "))
            {
                //System.out.print(F_format_op+" ");
                String temp = check_R_format(line.substring(5).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + F_format_op + temp + shmt_op + addf_fun ;
                /*************************SubF*********************************/
            }else if(line.startsWith("SubF "))
            {
                //System.out.print(F_format_op+" ");
                String temp = check_R_format(line.substring(5).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + F_format_op + temp + shmt_op + subf_fun ;
            }
            /****************************MulF*****************************/
            else if(line.startsWith("MulF "))
            {
                //System.out.print(F_format_op+" ");
                String temp = check_R_format(line.substring(5).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + F_format_op + temp + shmt_op + mulf_fun ;
            }
            /************************DivF*******************************/
            else if(line.startsWith("DivF "))
            {
                //System.out.print(F_format_op+" ");
                String temp = check_R_format(line.substring(5).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code, in assembler");
                    is_code_wrigh = false;
                    break;
                }

                machine_code = machine_code + F_format_op + temp + shmt_op + divf_fun ;
            }
            /***********************Noth************************/
            else if(line.equals("Noth"))
            {
                //System.out.println("00000000000000000000000000000000"+ "   --"+line);
                machine_code = machine_code + "00000000000000000000000000000000" ;
            }else{
                /*Not correct code*/
                JOptionPane.showMessageDialog(this,"error in code, in assembler");
                is_code_wrigh = false;
                break;
            }

            /*The end of machine code*/
            if(i==ROM_size-1)
            machine_code = machine_code + "\" " ; /*add " at the end of machine code, it's the last instruction*/
            else
            machine_code = machine_code + "\"," ; /*add ", at the end of machine code*/

            machine_code = machine_code + "  --"+line ;
            machine_code = machine_code + "\n";
        }

        if(i<ROM_size)
        {
            while(i!=ROM_size-1)
            {
                i++;
                machine_code = machine_code + "\"00000000000000000000000000000000\",   --Noth\n";
            }
            machine_code = machine_code + "\"00000000000000000000000000000000\"    --Noth";
        }
        System.out.println(machine_code);
        
        if(is_code_wrigh)
        {    
        PrintWriter outputStream = null;
        try{
            outputStream = new PrintWriter(new FileWriter("vhdl code\\Instruction_Memory.vhdl"));
            outputStream.println("------------------------------------------------------------------------\n" +
"-- File:           Instruction_Memory.vhdl\n" +
"-- author: 	    implementing microprocessor in FBGA team\n" +
"-- Description:    This is an implementation of a Instruction_Memory \n" +
"--                 behavioral architecture.\n" +
"------------------------------------------------------------------------\n" +
"\n" +
"--Library'\n" +
"library IEEE;\n" +
"use IEEE.STD_LOGIC_1164.ALL;\n" +
"use IEEE.std_logic_unsigned.all;\n" +
"use IEEE.NUMERIC_STD.ALL;\n" +
"\n" +
"--Entity\n" +
"\n" +
"entity Instruction_Memory is\n" +
"port (\n" +
" ReadAddress: in std_logic_vector(31 downto 0);\n" +
" instruction: out  std_logic_vector(31 downto 0)\n" +
");\n" +
"end Instruction_Memory;\n" +
"\n" +
"\n" +
"\n" +
"--Architecture\n" +
"architecture Behavioral of Instruction_Memory is\n" +
"\n" +
"\n" +
"signal rom_addr: std_logic_vector(7 downto 0);\n" +
" type ROM_type is array (0 to 255 ) of std_logic_vector(31 downto 0);\n" +
" constant rom_data: ROM_type:=(\n");
            
        outputStream.println(machine_code);
        
        outputStream.println(");\n" +
"begin\n" +
"  \n" +
"  rom_addr<=ReadAddress(9 downto 2);\n" +
"  instruction <= rom_data(to_integer(unsigned(rom_addr))) when (to_integer(unsigned(ReadAddress))<1024) else x\"00000000\";\n" +
"\n" +
"end Behavioral;");
            
        outputStream.close();
        
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,e);
        }
        
        }else{
            /*Not correct code*/
            JOptionPane.showMessageDialog(this,"error in code or no code");
        }
     }/*is_compile_true bacet*/
     else
     {
        JOptionPane.showMessageDialog(this,"No assembly file, you should compile first");
     }
    }//GEN-LAST:event_jButton_runActionPerformed

    private void jButton_compileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_compileActionPerformed
       
            String TA_code = file_data + jTextArea_code.getText();
            TA_code = remove_comments(TA_code);  /*removing comments*/
           
            TA_code = pre_processor(TA_code);/*preprosessor*/
            if(TA_code.startsWith("error"))
                JOptionPane.showMessageDialog(this,"error in preprocessor, in #");
            
            PrintWriter outputStream00 = null;
            try{
            outputStream00 = new PrintWriter(new FileWriter("compilation files\\pure_code.pre"));
            outputStream00.println(TA_code);
            outputStream00.close();
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(this,"can't find the pass of .pre file");
            }
            
            Lexical_Analysis(TA_code);
            String parssing_out = parsing(0);
            if(parssing_out.startsWith("error"))/*if there is an error*/
            {
                JOptionPane.showMessageDialog(this,parssing_out+",The error is in parrsing");
            }
            
//          System.out.println("\n\n\n\n"+ sentence.get(0) + sen_keys.get(0));
//          System.out.println(sentence.get(1) + sen_keys.get(1));
//          System.out.println(sentence.get(2) + sen_keys.get(2));
//          System.out.println(sentence.get(3) + sen_keys.get(3));
//          System.out.println(sentence.get(4) + sen_keys.get(4));
//          System.out.println(sentence.get(5) + sen_keys.get(5));
//          System.out.println(sentence.get(6) + sen_keys.get(6));
//          System.out.println(sentence.get(7) + sen_keys.get(7));
//          System.out.println(sentence.get(8) + sen_keys.get(8));
//          System.out.println(sentence.get(9) + sen_keys.get(9));
//          System.out.println(sentence.get(10) + sen_keys.get(10));
//          System.out.println(sentence.get(11) + sen_keys.get(11));
//          System.out.println(sentence.get(12) + sen_keys.get(12));
//          System.out.println(sentence.get(13) + sen_keys.get(13));
//          System.out.println(sentence.get(14) + sen_keys.get(14));
//          System.out.println(sentence.get(15) + sen_keys.get(15));
//          System.out.println(sentence.get(16) + sen_keys.get(16));
//          System.out.println(sentence.get(17) + sen_keys.get(17));
           
            /***getting the startup code***/
            String startupcode="";
             try{
                    File my_lib = new File("compilation files\\startup.asm");
                    Scanner reader = new Scanner(my_lib);
                    while(reader.hasNext())
                    {
                        startupcode = startupcode + reader.nextLine() + "\n";
                    }
               } catch(Exception e)
                 {
                     JOptionPane.showMessageDialog(this,"Not found the startupcode");
                 }

            /*my aseembly*/
            assembly = startupcode + to_assempply(0) ;
            if(assembly.startsWith("error"))
            {
                JOptionPane.showMessageDialog(this,assembly+", The error is when changing to assembly");
                is_compile_true = false;
            }
           else
            {
                System.out.println("************my asembly****************\n"+assembly+"****************************");
                is_compile_true= true;
            }
            
            sentence.clear();
            sen_keys.clear();
            tokens.clear();
            key.clear();
            function_names.clear();
            arg_num.clear();
            memory_variables.clear();
            memory_variables_type.clear();
            reg_variables.clear();
            tail_of_assemply="";
            function_in_name="";
            lable_number=0;
            available_s_reg=2;
            
           
            
           PrintWriter outputStream = null;
            try{
            outputStream = new PrintWriter(new FileWriter("compilation files\\The_assembly_file.asm"));
            outputStream.println(assembly);
            outputStream.close();
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(this,"can't find the pass of .asm file");
            }
    }//GEN-LAST:event_jButton_compileActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_compile;
    private javax.swing.JButton jButton_run;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_code;
    // End of variables declaration//GEN-END:variables
}
