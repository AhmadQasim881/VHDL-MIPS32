------------------------------------------------------------------------
-- File:           Test_Shift_left.vhdl
-- Engineer: 	   implementing microprocessor in FBGA team
-- Description:    This is an implementation of a ALU control
--                 behavioral architecture.
------------------------------------------------------------------------

--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;



entity Test_Shift_left is 
port
(
instr        : in std_logic_vector (25 downto 0);
pc_current   : in std_logic_vector (3 downto 0) ;
Jump_Address : out std_logic_vector(31 downto 0)
);
end Test_Shift_left;

    

architecture behavioral of Test_Shift_left is

signal Extend_to_28Bit     : std_logic_vector(27 downto 0) := (others => '0');
signal Instr_shift_Left_2  : std_logic_vector(27 downto 0) := (others => '0');


begin

Extend_to_28Bit    <= "00" & instr;  
Instr_shift_Left_2 <= std_logic_vector(unsigned(Extend_to_28Bit) sll 2);
Jump_Address       <= pc_current& Instr_shift_Left_2;   

end behavioral;