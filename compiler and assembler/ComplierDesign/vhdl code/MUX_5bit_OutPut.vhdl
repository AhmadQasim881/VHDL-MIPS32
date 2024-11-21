------------------------------------------------------------------------
-- File:           MUX_5bit_OutPut.vhdl
-- Engineer: 	   Ahmad Qasim
-- Description:    This is an implementation of a MUX
--                 behavioral architecture.
------------------------------------------------------------------------

--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;




entity MUX_5bit_OutPut is 

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
end MUX_5bit_OutPut;




architecture behavioral of MUX_5bit_OutPut is
begin
MUX_OUT <= MUX_IN0 when MUX_sel='0' else MUX_IN1;
end behavioral;