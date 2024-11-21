------------------------------------------------------------------------
-- File:           SignExtend.vhdl
-- Engineer: 	   implementing microprocessor in FBGA team
-- Description:    This is an implementation of a SignExtend
--                 behavioral architecture.
------------------------------------------------------------------------

--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity SignExtend is 

port
(
se_in  : in std_logic_vector(15 downto 0);
se_out : out std_logic_vector(31 downto 0)
);
end SignExtend;




architecture behavioral of SignExtend is

begin

se_out <= x"0000" & se_in when se_in(15)='0' else x"ffff"& se_in; 

end behavioral;