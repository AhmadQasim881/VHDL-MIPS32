------------------------------------------------------------------------
-- File:           RegisterFile.vhdl
-- Engineer: 	   Ahmad Qasim
-- Description:    This is an implementation of a RegisterFile
--                 behavioral architecture.
------------------------------------------------------------------------

--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
USE IEEE.numeric_std.all;  



entity RegisterFile is 

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
end RegisterFile;




architecture behavioral of RegisterFile is

type reg_file_type is array (0 to 31) of std_logic_vector(31 downto 0);

signal array_reg: reg_file_type;

begin

process(clk,reset)
begin
if(reset='1') then
	array_reg(0)  <= x"00000000"; --$zero 0
	array_reg(1)  <= x"00000001"; --$at   1
	array_reg(2)  <= x"00000002"; --$v0   2
	array_reg(3)  <= x"00000003"; --$v1   3
	array_reg(4)  <= x"00000004"; --$a0   4
	array_reg(5)  <= x"00000005"; --$a1   5
	array_reg(6)  <= x"00000006"; --$a2   6
	array_reg(7)  <= x"00000007"; --$a3   7
	array_reg(8)  <= x"00000008"; --$t0   8
	array_reg(9)  <= x"00000009"; --$t1   9 
	array_reg(10) <= x"0000000A"; --$t2   10 
	array_reg(11) <= x"0000000B"; --$t3   11
	array_reg(12) <= x"0000000C"; --$t4   12
	array_reg(13) <= x"0000000D"; --$t5   13 
	array_reg(14) <= x"0000000E"; --$t6   14
	array_reg(15) <= x"0000000F"; --$t7   15
	array_reg(16) <= x"00000000"; --$s0   16 
	array_reg(17) <= x"00000001"; --$s1   17
	array_reg(18) <= x"00000002"; --$s2   18
	array_reg(19) <= x"00000003"; --$s3   19
	array_reg(20) <= x"00000004"; --$s4   20
	array_reg(21) <= x"00000005"; --$s5   21 
	array_reg(22) <= x"00000006"; --$s6   22
	array_reg(23) <= x"00000007"; --$s7   23
	array_reg(24) <= x"00000008"; --$t8   24
	array_reg(25) <= x"00000009"; --$t9   25
	array_reg(26) <= x"0000000A"; --$k0   26
	array_reg(27) <= x"0000000B"; --$k1   27
	array_reg(28) <= x"0000000C"; --$gp   28
	array_reg(29) <= x"00000000"; --$sp   29
	array_reg(30) <= x"0000000E"; --$fp   30
	array_reg(31) <= x"0000000F"; --$ra   31
	
elsif (rising_edge(clk)) then
	if(RegWrite='1')then
	     array_reg(to_integer(unsigned(WriteRegister)))<=WriteData;
	end if;
end if;
end process;

ReadData1 <= array_reg(to_integer(unsigned(ReadRegister1)));
ReadData2 <= array_reg(to_integer(unsigned(ReadRegister2)));


end behavioral;