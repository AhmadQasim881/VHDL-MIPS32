------------------------------------------------------------------------
-- File:           ALU_control.vhdl
-- Engineer: 	   Ahmad Qasim
-- Description:    This is an implementation of a ALU control
--                 behavioral architecture.
------------------------------------------------------------------------

--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;



entity ALU_control is 
port
(
funct     : in std_logic_vector (5 downto 0);
ALUOp     : in std_logic_vector (1 downto 0);
operation : out std_logic_vector(3 downto 0)
);
end ALU_control;




architecture behavioral of ALU_control is

signal result : std_logic_vector(3 downto 0) :=(others => '0');

begin

result(3) <= '0';
result(2) <= ALUOp(0) or (ALUOp(1) and funct(1));
result(1) <= not ALUOp(1) or not funct(2);
result(0) <= ALUOp(1) and (funct(3) or funct(0));

operation <= result;
end behavioral;