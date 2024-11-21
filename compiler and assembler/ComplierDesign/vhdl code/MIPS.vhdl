------------------------------------------------------------------------
-- File:           MIPS.vhdl
-- Engineer: 	   Ahmad Qasim
-- Description:    This is an implementation of a MIPS
--                 behavioral architecture.
------------------------------------------------------------------------




--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.std_logic_unsigned.all;
use IEEE.NUMERIC_STD.ALL;




------------------***Entity_MIPS***-------------------------
entity MIPS is 
port
(
clk                : in std_logic;
reset              : in std_logic; 
Instr              : out std_logic_vector(31 downto 0);
Internal_clk       : out std_logic; 
ALU_output         : out std_logic_vector(31 downto 0)     
);
end MIPS;
-----------------------***THE_END***------------------------




------------------***Architecture***---------------------------
architecture behavioral of MIPS is



------------------***Internal_Clock***---------------------
component Internal_Clock
port ( 
clk,reset  : in  std_logic;
Output_clk : out std_logic
);
end component;
-----------------------***THE_END***---------------------------





------------------***Instruction_Memory***---------------------
component Instruction_Memory
port (
 ReadAddress: in std_logic_vector(31 downto 0);
 instruction: out  std_logic_vector(31 downto 0)
);
end component;
-----------------------***THE_END***---------------------------


------------------***Register_File***--------------------------
component RegisterFile
generic(
	D:INTEGER:=32;
	A:INTEGER:=5
);

port
(
clk,reset     : in std_logic;
ReadRegister1 : in std_logic_vector(A-1 downto 0);
ReadRegister2 : in std_logic_vector(A-1 downto 0);
WriteRegister : in std_logic_vector(A-1 downto 0);

RegWrite      : in std_logic;

WriteData     : in std_logic_vector (D-1 downto 0);

ReadData1     : out std_logic_vector(D-1 downto 0);
ReadData2     : out std_logic_vector(D-1 downto 0)

);
end component;
-----------------------***THE_END***---------------------------


-----------------------***Main_control***----------------------
component Main_control
port
(
Opcode               : in  std_logic_vector(5 downto 0);
Address_ra           : out std_logic_vector(4 downto 0);    
RegDst               : out std_logic;
Jump                 : out std_logic;
JumpAndLink          : out std_logic;
JumpRegister         : out std_logic;
Branch_zero          : out std_logic;
Branch_lessThan      : out std_logic;
Branch_greaterThan   : out std_logic;
MemRead              : out std_logic;
MemToReg             : out std_logic;
ALUOp                : out std_logic_vector(1 downto 0);
MemWrite             : out std_logic;
ALUSrc               : out std_logic;
LoadImmadiate        : out std_logic;
SubImmadiate         : out std_logic;
RegWrite             : out std_logic
);
end component;
-----------------------***THE_END***---------------------------


-----------------------***MUX_5bit_OutPut***-------------------
component MUX_5bit_OutPut
generic(
	N:INTEGER:=5
);
port
(
MUX_IN0 : in  std_logic_vector(N-1 downto 0);
MUX_IN1 : in  std_logic_vector(N-1 downto 0);
MUX_sel : in  std_logic;
MUX_OUT : out std_logic_vector(N-1 downto 0)
);
end component;
-----------------------***THE_END***---------------------------


-----------------------***SignExtend***------------------------
component SignExtend
port
(
se_in  : in std_logic_vector(15 downto 0);
se_out : out std_logic_vector(31 downto 0)
);
end component;
-----------------------***THE_END***---------------------------


-----------------------***ALU_control***-----------------------

component ALU_control
port
(
funct     : in std_logic_vector (5 downto 0);
ALUOp     : in std_logic_vector (1 downto 0);
operation : out std_logic_vector(3 downto 0)
);
end component;
-----------------------***THE_END***---------------------------


-------------------------***MUX***-----------------------------
component MUX
generic(
	N:INTEGER:=32
);
port
(
MUX_IN0 : in  std_logic_vector(N-1 downto 0);
MUX_IN1 : in  std_logic_vector(N-1 downto 0);
MUX_sel : in  std_logic;
MUX_OUT : out std_logic_vector(N-1 downto 0)
);
end component;
-----------------------***THE_END***---------------------------


-------------------------***ALU***-----------------------------
component ALU
port
(
a              : in  std_logic_vector(31 downto 0);
b              : in  std_logic_vector(31 downto 0);
ALU_control    : in  std_logic_vector(3 downto 0) ;
AlU_result     : out std_logic_vector(31 downto 0);
Carry_out      : out std_logic;
sign_flag      : out std_logic;
zero           : out std_logic         
);
end component;
-----------------------***THE_END***---------------------------


-----------------------***Data_Memory***-----------------------
component Data_Memory
port (
 clk      : in std_logic;  
 Address  : in std_logic_Vector(31 downto 0);
 writeData: in std_logic_Vector(31 downto 0);
 memWrite : in std_logic;
 memRead  : in std_logic;
 ReadData : out std_logic_Vector(31 downto 0)
);
end component;
-----------------------***THE_END***---------------------------


-----------------------***Test_Shift_left***-------------------
component Test_Shift_left
port
(
instr        : in std_logic_vector (25 downto 0);
pc_current   : in std_logic_vector (3 downto 0) ;
Jump_Address : out std_logic_vector(31 downto 0)
);
end component;
-----------------------***THE_END***---------------------------


-----------------------***Branch_Address_generate***------------
component Branch_Address_generate
port
(
instr          : in std_logic_vector (15 downto 0);
sign_extend    : in std_logic_vector (13 downto 0) ;
Branch_Address : out std_logic_vector(31 downto 0)
);
end component;

-----------------------***THE_END***---------------------------





-----------------------***Defined_Signal***--------------------


signal output_Instruction     : std_logic_vector(31 downto 0);
signal current_signal         : std_logic_vector(31 downto 0);
signal PC_Out                 : std_logic_vector(31 downto 0);
signal PC_Increment           : std_logic_vector(31 downto 0);
signal WriteData              : std_logic_Vector(31 downto 0);
signal ReadData1              : std_logic_vector(31 downto 0);
signal ReadData2              : std_logic_vector(31 downto 0);
signal MUX_OUT_2              : std_logic_vector(31 downto 0);
signal AlU_result             : std_logic_vector(31 downto 0);
signal se_out                 : std_logic_vector(31 downto 0);
signal ReadData               : std_logic_vector(31 downto 0);
signal Jump_Address           : std_logic_vector(31 downto 0);
signal Data_out               : std_logic_vector(31 downto 0);
signal Branch_Address         : std_logic_vector(31 downto 0);                 
signal MUX_OUT_3              : std_logic_vector(31 downto 0);
signal MUX_OUT_4              : std_logic_vector(31 downto 0);
signal MUX_OUT_5              : std_logic_vector(31 downto 0);
signal MUX_OUT_6              : std_logic_vector(31 downto 0);
signal MUX_OUT_7              : std_logic_vector(31 downto 0);
signal MUX_OUT_8              : std_logic_vector(31 downto 0);
signal MUX_OUT_Load           : std_logic_Vector(31 downto 0);
signal MUX_OUT_Sub            : std_logic_Vector(31 downto 0);
signal MUX_OUT_Link           : std_logic_Vector(31 downto 0);


signal Branch_sel_onLessThan    : std_logic; 
signal Branch_sel_onGreaterThan : std_logic;
signal MUX_OUT_1                : std_logic_Vector(4 downto 0);
signal Address_ra               : std_logic_Vector(4 downto 0);
signal MUX_OUTPC_2              : std_logic_Vector(4 downto 0);
signal operation                : std_logic_vector(3 downto 0);
signal zero                     : std_logic;
signal Branch_sel_onZero        : std_logic;                         
signal Carry_out                : std_logic;
signal sign_flag                : std_logic;

signal IntOutput_clk   :  std_logic;



signal RegDst             :  std_logic;
signal Jump               :  std_logic;
signal JumpAndLink        :  std_logic;
signal JumpRegister       :  std_logic;
signal Branch_zero        :  std_logic;
signal Branch_lessThan    :  std_logic;
signal Branch_greaterThan :  std_logic;
signal MemRead            :  std_logic;
signal MemToReg           :  std_logic;
signal ALUOp              :  std_logic_vector(1 downto 0);
signal MemWrite           :  std_logic;
signal ALUSrc             :  std_logic;
signal RegWrite           :  std_logic;
signal LoadImmadiate      :  std_logic;
signal SubImmadiate       :  std_logic;
-----------------------***THE_END***---------------------------




-----------------------***Design***----------------------------
begin

u1 : Internal_Clock port map
(
clk         => clk,
reset       => reset,
Output_clk  => IntOutput_clk
);

Internal_clk<=IntOutput_clk;

process(clk,reset)
begin
     if(reset ='1') then 
		current_signal<=x"00000000";
	 elsif(rising_edge(clk)) then 
	     current_signal <=PC_Out;
	end if;
end process;

PC_Increment <= std_logic_vector(unsigned(current_signal)+to_unsigned(4,32));






u2 : Instruction_Memory port map
(
ReadAddress => current_signal,
instruction => output_Instruction
);



U3 : MUX_5bit_OutPut port map
(
MUX_IN0   => output_Instruction(20 downto 16),  
MUX_IN1   => output_Instruction(15 downto 11),
MUX_sel   => RegDst,
MUX_OUT   => MUX_OUT_1 
);

U4 : MUX_5bit_OutPut port map
(
MUX_IN0   => MUX_OUT_1,  
MUX_IN1   => Address_ra,
MUX_sel   => JumpAndLink,
MUX_OUT   => MUX_OUTPC_2 
);



U5 : RegisterFile port map
(
clk           => clk,
reset         => reset,
ReadRegister1 => output_Instruction(25 downto 21),
ReadRegister2 => output_Instruction(20 downto 16),  
WriteRegister => MUX_OUTPC_2,
RegWrite      => RegWrite ,
WriteData     => MUX_OUT_Link,
ReadData1     => ReadData1,
ReadData2     => ReadData2 
);


U6 : Main_control port map
(
Opcode             => output_Instruction(31 downto 26),
Address_ra         => Address_ra,
RegDst             => RegDst   ,
Jump               => Jump     ,
JumpAndLink        => JumpAndLink ,
JumpRegister       => JumpRegister,
Branch_zero        => Branch_zero   ,
Branch_lessThan    => Branch_lessThan,
Branch_greaterThan => Branch_greaterThan,
MemRead            => MemRead  ,
MemToReg           => MemToReg ,
ALUOp              => ALUOp    ,
MemWrite           => MemWrite ,
ALUSrc             => ALUSrc   ,
LoadImmadiate      => LoadImmadiate,
SubImmadiate       => SubImmadiate,
RegWrite           => RegWrite
);


U7 : SignExtend port map
(
se_in  => output_Instruction(15 downto 0),
se_out => se_out
);

U8 : MUX port map
(
MUX_IN0 => ReadData2,
MUX_IN1 => se_out,
MUX_sel => ALUSrc,
MUX_OUT => MUX_OUT_2
);


U9 : ALU_control port map
(
funct      => output_Instruction(5 downto 0),
ALUOp      => ALUOp,
operation  => operation
);


U10 : ALU port map
(
a           =>ReadData1 ,
b           =>MUX_OUT_2 ,
ALU_control =>operation ,
AlU_result  =>AlU_result,
Carry_out   =>Carry_out ,
sign_flag   =>sign_flag ,
zero        =>zero
);


U11 : Data_Memory port map
(
clk        => clk,
Address    => AlU_result,
writeData  => ReadData2,
memWrite   => memWrite, 
memRead    => memRead,
ReadData   => ReadData
);

U12 : MUX port map
(
MUX_IN0 => AlU_result,
MUX_IN1 => ReadData,
MUX_sel => MemToReg,
MUX_OUT => writeData
);

U13 : MUX port map
(
MUX_IN0 => writeData,
MUX_IN1 => AlU_result,
MUX_sel => LoadImmadiate,
MUX_OUT => MUX_OUT_Load
);



U14 : MUX port map
(
MUX_IN0 => MUX_OUT_Load,
MUX_IN1 => AlU_result,
MUX_sel => SubImmadiate,
MUX_OUT => MUX_OUT_Sub
);



U15 : MUX port map
(
MUX_IN0 => MUX_OUT_Sub,
MUX_IN1 => PC_Increment,
MUX_sel => JumpAndLink,
MUX_OUT => MUX_OUT_Link
);


U16 : Test_Shift_left port map
(
instr        => output_Instruction(25 downto 0),
pc_current   => output_Instruction(31 downto 28),
Jump_Address => Jump_Address
);

U17 : Branch_Address_generate port map
(
instr          => output_Instruction(15 downto 0),
sign_extend    => se_out(31 downto 18),
Branch_Address => Branch_Address
);


Branch_sel_onZero        <= zero and Branch_zero;

Branch_sel_onLessThan    <=sign_flag and Branch_lessThan;
Branch_sel_onGreaterThan <=not sign_flag and Branch_greaterThan;

U18 : MUX port map
(
MUX_IN0 => PC_Increment,
MUX_IN1 => Branch_Address,
MUX_sel => Branch_sel_onZero,
MUX_OUT => MUX_OUT_3
);


U19 : MUX port map
(
MUX_IN0 => MUX_OUT_3,
MUX_IN1 => Jump_Address,
MUX_sel => Jump,
MUX_OUT => MUX_OUT_4
);


U20 : MUX port map
(
MUX_IN0 => MUX_OUT_4,
MUX_IN1 => Branch_Address,
MUX_sel => Branch_sel_onLessThan,
MUX_OUT => MUX_OUT_5
);

U21 : MUX port map
(
MUX_IN0 => MUX_OUT_5,
MUX_IN1 => Branch_Address,
MUX_sel => Branch_sel_onGreaterThan,
MUX_OUT => MUX_OUT_6
);

U22 : MUX port map
(
MUX_IN0 => MUX_OUT_6,
MUX_IN1 => Jump_Address,
MUX_sel => JumpAndLink,
MUX_OUT => MUX_OUT_7
);

U23 : MUX port map
(
MUX_IN0 => MUX_OUT_7,
MUX_IN1 => ReadData1,
MUX_sel => JumpRegister,
MUX_OUT => MUX_OUT_8
);

PC_Out <= MUX_OUT_8;

Instr              <= output_Instruction;
ALU_output         <= AlU_result;

-----------------------***THE_END***---------------------------
end behavioral;
-----------------------***THE_END***---------------------------