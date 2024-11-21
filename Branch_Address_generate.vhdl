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



entity Branch_Address_generate is 
port
(
instr          : in std_logic_vector (15 downto 0);
sign_extend    : in std_logic_vector (13 downto 0) ;
Branch_Address : out std_logic_vector(31 downto 0)
);
end Branch_Address_generate;

    

architecture behavioral of Branch_Address_generate is

signal Extend_to_18Bit     : std_logic_vector(17 downto 0) := (others => '0');
signal Instr_shift_Left_2  : std_logic_vector(17 downto 0) := (others => '0');


begin

Extend_to_18Bit    <= "00" & instr;  
Instr_shift_Left_2 <= std_logic_vector(unsigned(Extend_to_18Bit) sll 2);
Branch_Address     <= sign_extend& Instr_shift_Left_2;   

end behavioral;