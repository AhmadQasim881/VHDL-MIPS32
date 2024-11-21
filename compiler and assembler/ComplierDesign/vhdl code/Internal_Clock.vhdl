------------------------------------------------------------------------
-- File:           Internal_Clock.vhdl
-- Engineer: 	   Ahmad Qasim
-- Description:    This is an implementation of a Internal_Clock 
--                 behavioral architecture.
------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.numeric_std.ALL;

entity Internal_Clock is

port ( 
clk,reset  : in  std_logic;
Output_clk : out std_logic
);

end Internal_Clock;



architecture behavioral of Internal_Clock is

signal count    : integer   :=1;
signal tmp      : std_logic :='0';

constant Number : integer   := 50000000;

begin

process(clk,reset)
begin
		if(reset='1') then
			count<= 1;
			tmp  <= '0';
			elsif(rising_edge(clk)) then
				count<=count+1;

				if (count = Number) then
					tmp   <= NOT tmp;
					count <= 1;
			    end if;
		end if;
	Output_clk <= tmp;
end process;


end behavioral;