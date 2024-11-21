------------------------------------------------------------------------
-- File:           MIPS_tb.vhdl
-- Engineer: 	   Ahmad Qasim
-- Description:    This is an implementation of a testbench MIPS 
--                 behavioral architecture.
------------------------------------------------------------------------

--Library'
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;



------------------***Entity_MIPS_tb***----------------------
entity MIPS_tb is 
end MIPS_tb;
-----------------------***THE_END***------------------------




-----------------------***Architecture***-------------------
architecture behavioral of MIPS_tb is




-----------------------***MIPS***---------------------------
component MIPS
port
(
clk                : in std_logic;
reset              : in std_logic;  
Instr              : out std_logic_vector(31 downto 0);
Internal_clk       : out std_logic;
ALU_output         : out std_logic_vector(31 downto 0)     
);
end component;
-----------------------***THE_END***------------------------



-----------------------***Defined_Signal***-----------------
signal clk                     : std_logic :='0';
signal reset                   : std_logic :='0';
signal Instr                   : std_logic_vector(31  downto 0);
signal ALU_output              : std_logic_vector(31  downto 0);
signal Internal_clk            : std_logic;
signal Zero_flag_Output        : std_logic;

constant clk_period : time := 10 ns;
-----------------------***THE_END***------------------------



-----------------------***THE_END***------------------------
begin 

uut : MIPS port map
(
clk                  => clk,
reset                => reset,
Instr                => Instr,
Internal_clk         =>Internal_clk,
ALU_output           => ALU_output
);


clk_process :process
begin
clk <= '0';
wait for clk_period/2;
clk <= '1';
wait for clk_period/2;
 end process;



stim_proc: process
begin  
    reset <= '1';
    wait for clk_period*10;
reset <= '0'; 
    wait;
end process;

-----------------------***THE_END***------------------------

end behavioral;
-----------------------***THE_END***------------------------