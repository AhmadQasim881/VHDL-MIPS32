------------------------------------------------------------------------
-- File:           Main_control.vhdl
-- Engineer: 	   Ahmad Qasim
-- Description:    This is an implementation of a Main control 
--                 behavioral architecture.
------------------------------------------------------------------------

--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;



entity Main_control is 
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
end Main_control;




architecture behavioral of Main_control is


begin
	process(Opcode)
	begin
		 RegWrite <= '0';
		 
		 case Opcode is
			when "000000" => -- add , sub , and , or , Slt , MUL , DIV
				Address_ra         <= "00000";
				RegDst             <= '1' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= '0' ;
				ALUOp              <= "10";
				MemWrite           <= '0' ; 
				ALUSrc             <= '0' ;
				RegWrite           <= '1' ;
				LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;
			when "100011" => --load
				Address_ra         <= "00000";
				RegDst             <= '0' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '1' ;
				MemToReg           <= '1' ;
				ALUOp              <= "00";
				MemWrite           <= '0' ; 
				ALUSrc             <= '1' ;
				RegWrite           <= '1' ;
				LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;			
			when "110011" => --loadi
				Address_ra         <= "00000";
				RegDst             <= '0' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= '0' ;
				ALUOp              <= "00";
				MemWrite           <= '0' ; 
				ALUSrc             <= '1' ;
				RegWrite           <= '1' ;
				LoadImmadiate      <= '1' ;
			    SubImmadiate       <= '0' ;
			when "110100" => --SUBi
				Address_ra         <= "00000";
				RegDst             <= '0' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= '0' ;
				ALUOp              <= "01";
				MemWrite           <= '0' ; 
				ALUSrc             <= '1' ;
				RegWrite           <= '1' ;
				LoadImmadiate      <= '0' ;
			    SubImmadiate       <= '1' ;
			when "101011" => --store
				Address_ra         <= "00000";
				RegDst             <= 'X' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= '0' ;
				ALUOp              <= "00";
				MemWrite           <= '1' ; 
				ALUSrc             <= '1' ;
				RegWrite           <= '0' ;
				LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;
			when "000100" => --beq
				Address_ra         <= "00000";
				RegDst             <= 'X' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '1' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= 'X' ;
				ALUOp              <= "01";
				MemWrite           <= '0' ; 
				ALUSrc             <= '0' ;
				RegWrite           <= '0' ;
                LoadImmadiate      <= '0' ;	
				SubImmadiate       <= '0' ;				
			when "010100" => --blt
				Address_ra         <= "00000";
				RegDst             <= 'X' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '1' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= 'X' ;
				ALUOp              <= "01";
				MemWrite           <= '0' ; 
				ALUSrc             <= '0' ;
				RegWrite           <= '0' ;
				LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;
			when "100100" => --bge
				Address_ra         <= "00000";
				RegDst             <= 'X' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '1' ;
				MemRead            <= '0' ;
				MemToReg           <= 'X' ;
				ALUOp              <= "01";
				MemWrite           <= '0' ; 
				ALUSrc             <= '0' ;
				RegWrite           <= '0' ;
			    LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;
			when "000010" => --Jump
				Address_ra         <= "00000";
				RegDst             <= 'X' ;
				Jump               <= '1' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= 'X' ;
				ALUOp              <= "00";
				MemWrite           <= '0' ; 
				ALUSrc             <= '0' ;
				RegWrite           <= '0' ;
				LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;
			when "100010" => --Jal
				Address_ra         <= "11111";
				RegDst             <= 'X' ;
				Jump               <= '0' ;
				JumpAndLink        <= '1' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= 'X' ;
				ALUOp              <= "00";
				MemWrite           <= '0' ; 
				ALUSrc             <= '0' ;
				RegWrite           <= '1' ;
				LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;
			when "110010" => --Jr
				Address_ra         <= "00000";
				RegDst             <= 'X' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '1' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= 'X' ;
				ALUOp              <= "00";
				MemWrite           <= '0' ; 
				ALUSrc             <= '0' ;
				RegWrite           <= '0' ;
				LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;
			when others => null;
				Address_ra         <= "00000";
				RegDst             <= '0' ;
				Jump               <= '0' ;
				JumpAndLink        <= '0' ;
				JumpRegister       <= '0' ;
				Branch_zero        <= '0' ;
				Branch_lessThan    <= '0' ;
				Branch_greaterThan <= '0' ;
				MemRead            <= '0' ;
				MemToReg           <= '0' ;
				ALUOp              <= "00";
				MemWrite           <= '0' ; 
				ALUSrc             <= '0' ;
				RegWrite           <= '0' ;
				LoadImmadiate      <= '0' ;
				SubImmadiate       <= '0' ;
		 end case;
	end process;

end behavioral;