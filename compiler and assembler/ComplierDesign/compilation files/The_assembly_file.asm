LoadI @sp,@zero,0.
Jump main.
reverseDigits : Push .
LoadI @t0,@zero,0.
Store @t0,@sp,-1.
Lable1 : LoadI @s2,@a0,0.
LoadI @s3,@zero,0.
blt @s3,@s2,Lable0.
Load @v0,@sp,-1.
Pop .
Jr @ra.

Pop .
Jr @ra.

add_from_x_to_y : Store @ra,@sp,0.
Push .
Store @a0,@sp,0.
Push .
LoadI @a0,@a0,0.
Jal reverseDigits.
Pop .
Load @a0,@sp,0.
Pop .
Load @ra,@sp,0.
LoadI @a0,@v0,0.
Store @ra,@sp,0.
Push .
Store @a0,@sp,0.
Push .
LoadI @a0,@a1,0.
Jal reverseDigits.
Pop .
Load @a0,@sp,0.
Pop .
Load @ra,@sp,0.
LoadI @a1,@v0,0.
Push .
Push .
LoadI @t0,@zero,0.
Store @t0,@sp,-1.
LoadI @t0,@a0,0.
Store @t0,@sp,-2.
Lable5 : Load @s4,@sp,-2.
LoadI @s5,@a1,0.
blt @s4,@s5,Lable4.
beq @s4,@s5,Lable4.
Load @v0,@sp,-1.
Pop .
Pop .
Jr @ra.

Pop .
Pop .
Jr @ra.

main : Push .
Store @ra,@sp,0.
Push .
Store @a0,@sp,0.
Push .
LoadI @a0,@zero,0.
Store @a1,@sp,0.
Push .
LoadI @a1,@zero,19.
Jal add_from_x_to_y.
Pop .
Load @a1,@sp,0.
Pop .
Load @a0,@sp,0.
Pop .
Load @ra,@sp,0.
Store @v0,@sp,-1.
Load @s0,@sp,-1.
AddI @t0,@s0,0.
Store @t0,@sp,-1.
Pop .
end-labe : Jump end-labe.

Lable0 : Load @s0,@sp,-1.
MulI @t0,@s0,10.
DivI @t1,@a0,10.
MulI @t1,@t1,10.
Sub @t1,@a0,@t1.
Add @t0,@t0,@t1.
Store @t0,@sp,-1.
DivI @t0,@a0,10.
LoadI @a0,@t0,0.
Jump Lable1 .
Lable4 : Load @s0,@sp,-1.
Load @s1,@sp,-2.
Add @t0,@s0,@s1.
Store @t0,@sp,-1.
Load @s0,@sp,-2.
AddI @t0,@s0,1.
Store @t0,@sp,-2.
Jump Lable5 .

