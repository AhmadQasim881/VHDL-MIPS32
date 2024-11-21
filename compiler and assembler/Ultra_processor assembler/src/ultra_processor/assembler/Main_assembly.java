/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ultra_processor.assembler;
import java.io.FileWriter;
import java.io.PrintWriter;
import static java.lang.Integer.toBinaryString;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
/**
 *
 * @author implementing microprocessor in FBGA team
 */
public class Main_assembly extends javax.swing.JFrame {
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

    /**
     * Creates new form Main_assembly
     */
    public Main_assembly() {
        initComponents();
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
    
    /*a function to change to float*/
    String change_to_float(String s)
    {
        float a =Float.parseFloat(s); 
        int bits = Float.floatToIntBits(a);
        return String.format("%32s", Integer.toBinaryString(bits)).replace(" ", "0");
        
        
//        StringTokenizer s1 = new StringTokenizer(s,".");
//        String left,right; /*101.101001*/
//        int sign;
//
//        float r;  /*for right in integer form*/
//        
//        if(s.startsWith("-"))
//        {
//            sign = 1;
//            s = s.substring(1); /*remove the sign*/
//        }
//        else
//            sign = 0;
//        
//        right = "0";
//        left = s1.nextToken();
//        if(s1.hasMoreTokens())
//            right = s1.nextToken();
//        
//        left = toBinaryString(Integer.parseInt(left));
//        
//        r =  Float.parseFloat("0."+right);
//        right = "";
//        float k = r;
//        for(int i=0;i<23;i++)
//        {
//            r = r*2;
//            if(r>1)
//            {
//                right = right + "1";
//                r--;
//            }else if(r<1)
//            {
//               right = right + "0";
//            }else  /*equal 1*/
//            {
//                right = right + "1";
//                break;
//            }
//        }
//        
//        while(right.length()<23) /*if it exit from the break statmatment*/
//          right = right + "0";
//        
//        String matissa;
//        int exponant;
//        
//        int i=0;
//        while( (right+left).substring(i) == "0" )
//        {
//            i++;
//        }
//         matissa  = right.substring(i) + left;
//
//        if(right.contains("1"))
//        {
//           exponant = right.length()-i-1;
//        }else
//        {
//           exponant = -(i-right.length()+1) ;
//        }
//        
//        exponant = exponant +127;
//            
//        
//        return exponant +"r="+k;

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
            }
        }
        
        return "error_no_lable";
    }

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
        jButton_run = new javax.swing.JButton();
        jButton_save_in_file = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0,80));

        jTextArea_code.setColumns(20);
        jTextArea_code.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextArea_code.setRows(5);
        jScrollPane1.setViewportView(jTextArea_code);

        jButton_run.setText("Run");
        jButton_run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_runActionPerformed(evt);
            }
        });

        jButton_save_in_file.setText("Save");
        jButton_save_in_file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_save_in_fileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addComponent(jButton_run)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_save_in_file)
                .addGap(153, 153, 153))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_save_in_file)
                    .addComponent(jButton_run))
                .addGap(37, 37, 37))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 690, 310));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        setSize(new java.awt.Dimension(766, 400));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_runActionPerformed
        String my_code;
        int instructoins_count;
        is_code_wrigh = true;
        my_code = jTextArea_code.getText();
        my_code = my_code.trim();
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
                System.out.println("!!!!!!!"+st1+" ");
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
            if(line.contains(":"))
            {
                try{
                line=line.substring( line.indexOf(":")+1 ).trim();
                }catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this,"error in code in : ");
                    is_code_wrigh=false;
                }
            }
            
            /*To check the operation*/
            /****************ADD*******************/
            if(line.startsWith("Add "))
            {
                //System.out.print(R_format_op+" ");
                String temp = check_R_format(line.substring(4).trim());
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,temp);
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
                    JOptionPane.showMessageDialog(this,temp);
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
                    JOptionPane.showMessageDialog(this,temp);
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
                    JOptionPane.showMessageDialog(this,temp);
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
                    JOptionPane.showMessageDialog(this,"error in load");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code");
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
                System.out.println("7777777777777 "+line.substring(5).trim());
                try{
                temp = check_I_format(line.substring(5).trim());
                }catch(Exception e)
                {
                    /*if the substring gives exception*/
                    JOptionPane.showMessageDialog(this,"error in code");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
                    is_code_wrigh = false;
                    break;
                }
                if(temp.startsWith("error"))
                {
                    /*Not correct code*/
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,temp);
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                    JOptionPane.showMessageDialog(this,"error in code");
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
                JOptionPane.showMessageDialog(this,"error in code");
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
             
    }//GEN-LAST:event_jButton_runActionPerformed

    private void jButton_save_in_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_save_in_fileActionPerformed
        
        
        if(is_code_wrigh)
        {    
        PrintWriter outputStream = null;
        try{
            outputStream = new PrintWriter(new FileWriter("D:\\A_DB_Matarials\\projects\\32-bit CPU VHDL\\Instruction_Memory.vhdl"));
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
    }//GEN-LAST:event_jButton_save_in_fileActionPerformed

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
            java.util.logging.Logger.getLogger(Main_assembly.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_assembly.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_assembly.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_assembly.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_assembly().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_run;
    private javax.swing.JButton jButton_save_in_file;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_code;
    // End of variables declaration//GEN-END:variables
}
