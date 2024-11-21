------------------------------------------------------------------------
-- File:           ALU.vhdl
-- Engineer: 	   Ahmad Qasim
-- Description:    This is an implementation of a ALU
--                 behavioral architecture.
------------------------------------------------------------------------

--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.std_logic_unsigned.all;
use IEEE.NUMERIC_STD.ALL;



entity ALU is 
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
end ALU;




architecture behavioral of ALU is

signal result      : std_logic_vector(31 downto 0) ;
signal zero_flag   : std_logic;
signal tmp         : std_logic_vector(32 downto 0);


begin
 


process(a,b,ALU_control)
begin

case ALU_control is
when "0000" =>
result <= a and b;

when "0001" =>
result <= a or b;

when "0010" => 
result <= std_logic_vector(unsigned(a) + unsigned(b));

when "0011" => 
result <= std_logic_vector(to_unsigned((to_integer(unsigned(a))* to_integer(unsigned(b))),32));


when "0100" => 
result <= std_logic_vector(to_unsigned((to_integer(unsigned(a))/to_integer(unsigned(b))),32));






when "0101" => 
result <= a xor b;

when "0110" => 
result <= std_logic_vector(unsigned(a) - unsigned(b));

when "0111" => 
if(to_integer(unsigned('0'&a))<to_integer(unsigned('0'&b))) then
result <=x"00000001";
else
result <=x"00000000";
end if;

when "1100" =>
result <= a nor b;

when others => null;
result<=x"00000000";

end case;

end process;


zero_flag <= '1' when (to_integer(unsigned(result))=0) else 
             '0';
tmp <= ('0' & a) +('0'& b);

carry_out  <= tmp(32);
sign_flag  <= result(31);
zero       <= zero_flag;


AlU_result <= result;

end behavioral;