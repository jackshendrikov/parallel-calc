-------------------------------------
-- Course Work
-- Ada
-- Ann Pidchasiuk, IV-71

-------------------------------------


with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Calendar; use Ada.Calendar;
with ada.float_text_io; use ada.float_text_io;

procedure Main is
   N: integer := 900;
   P: integer := 6;
   H: Integer := N / P;


   type vector is array(integer range <>) of integer;
   Subtype Vector_N is Vector(1..N);
   Subtype Vector_5H is Vector(1..5*H);
   Subtype Vector_4H is Vector(1..4*H);
   Subtype Vector_3H is Vector(1..3*H);
   Subtype Vector_2H is Vector(1..2*H);
   Subtype Vector_H is Vector(1..H);

   type Matrix is array(integer range <>) of Vector_N;
   Subtype Matrix_N is Matrix(1..N);
   Subtype Matrix_5H is Matrix(1..5*H);
   Subtype Matrix_4H is Matrix(1..4*H);
   Subtype Matrix_3H is Matrix(1..3*H);
   Subtype Matrix_2H is Matrix(1..2*H);
   Subtype Matrix_H is Matrix(1..H);

   Time_all: duration;
   time1,time2:  time;

--------------------Specification Task 1---------------------
   task T1 is
      pragma Storage_Size(3000000000);
      entry Send_MOME_T2(MOh: in Matrix_H; MEh: in Matrix_H);
      entry Send_R_T2(Rh: in Vector_H);
      entry Send_MZ_T6(MZ4h: in Matrix_4H);
      entry Send_m_T2(m_res: in Integer);
      entry Send_A_T2(A3h: in Vector_3H);
      entry Send_A_T6(A2h: in Vector_2H);
   end T1;

--------------------Specification Task 2---------------------
   task T2 is
      pragma Storage_Size(3000000000);
      entry Send_BZ_T1(Bh:in Vector_H; Zh: in Vector_H);
      entry Send_MOME_T3(MO2h: in Matrix_2H; ME2h: in Matrix_2H);
      entry Send_R_T3(R2h: in Vector_2H);
      entry Send_MZ_T1(MZ3h: in Matrix_3H);
      entry Send_m1_T1(m1_send: in Integer);
      entry Send_m_T3(m_res: in Integer);
      entry Send_A_T3(A2h: in Vector_2H);
   end T2;

--------------------Specification Task 3---------------------
   task T3 is
      pragma Storage_Size(3000000000);
      entry Send_BZ_T4(Bh: in Vector_H; Zh: in Vector_H);
      entry Send_R_T4(R3h: in Vector_3H);
      entry Send_MZ_T2(MZ2h: in Matrix_2H);
      entry Send_m2_T2(m2_send: in Integer);
      entry Send_m4_T4(m4_send: in Integer);
      entry Send_A_T4(Ah: in Vector_H);
   end T3;

--------------------Specification Task 4---------------------
   task T4 is
      pragma Storage_Size(3000000000);
      entry Send_BZ_T5(B2h: in Vector_2H; Z2h: in Vector_2H);
      entry Send_MOME_T3(MO3h: in Matrix_3H; ME3h: in Matrix_3H);
      entry Send_R_T5(R4h: in Vector_4H);
      entry Send_MZ_T3(MZh: in Matrix_H);
      entry Send_m5_T5(m5_send: in Integer);
      entry Send_m_T3(m_res: in Integer);
   end T4;

--------------------Specification Task 5---------------------
   task T5 is
      pragma Storage_Size(3000000000);
      entry Send_BZ_T6(B3h: in Vector_3H; Z3h: in Vector_3H);
      entry Send_MOME_T4(MO2h: in Matrix_2H; ME2h: in Matrix_2H);
      entry Send_MZ_T6(MZh: in Matrix_H);
      entry Send_m6_T6(m6_send: in Integer);
      entry Send_m_T4(m_res: in Integer);
   end T5;

--------------------Specification Task 6---------------------
   task T6 is
      pragma Storage_Size(3000000000);
      entry Send_BZ_T1(B4h: in Vector_4H; Z4h: in Vector_4H);
      entry Send_MOME_T5(MOh: in Matrix_H; MEh: in Matrix_H);
      entry Send_R_T5(Rh: in Vector_H);
      entry Send_m_T5(m_res: in Integer);
      entry Send_A_T5(Ah: in Vector_H);
   end T6;

------------------------- Task 1--------------------------
   task body T1 is
      Sum1, Sum2: Integer := 0;
      MO, ME: Matrix_H;
      Z, B: Vector_N;
      MZ: Matrix_4H;
      m, m1: Integer;
      R: Vector_H;
      A: Vector_N;
      MTx: Matrix_H;
   begin
      put_Line("T1 started!");

      for i in 1 .. N loop
         B(i):=1;
         Z(i):=1;
      end loop;

      T2.Send_BZ_T1(B(H+1 .. 2*H), Z(H+1 .. 2*H));
      T6.Send_BZ_T1(B(2*H+1 .. N), Z(2*H+1 .. N));

      accept Send_MOME_T2 (MOh : in Matrix_H; MEh : in Matrix_H) do
         MO := MOh;
         ME := MEh;
      end Send_MOME_T2;

      accept Send_R_T2 (Rh : in Vector_H) do
         R := Rh;
      end Send_R_T2;

      accept Send_MZ_T6 (MZ4h : in Matrix_4H) do
         MZ := MZ4h;
      end Send_MZ_T6;

      T2.Send_MZ_T1(MZ(H+1 .. 4*H));

      m1 :=100000;
      for I in 1 .. H loop
         if m1 < Z(I) then
            m1 := Z(I);
         end if;
      end loop;
      T2.Send_m1_T1(m1);

      accept Send_m_T2 (m_res : in Integer) do
         m := m_res;
      end Send_m_T2;

      for i in 1.. H loop
         for j in 1 .. N loop
            Sum1 := 0;
            for z in 1 .. N loop
               Sum1 := Sum1 + MO(J)(Z) * MZ(Z)(I);
            end loop;
            MTx(J)(I) := Sum1;
         end loop;
      end loop;
      for i in 1.. H loop
         Sum1 := 0;
         Sum2 := 0;
         for j in 1 .. N loop
            Sum1 := Sum1 + B(i) * MTx(I)(J);
            Sum2 := Sum2 + m * R(i) * ME(I)(J);
         end loop;
         A(I) := Sum1 + Sum2;
      end loop;

      accept Send_A_T2 (A3h : in Vector_3H) do
         A(H+1 .. 4*H) := A3h;
      end Send_A_T2;

      accept Send_A_T6 (A2h : in Vector_2H) do
         A(4*H+1 .. N) := A2h;
      end Send_A_T6;

      if N < 36 then
         for i in 1 .. N loop
            Put(A(i), 4);
         end loop;
      end if;
      time2:=clock;
      time_all:=time2-time1;
      put_line("TIME");
      put (Float(Time_all), 4, 3, 0);
      put_Line("T1 end!");
   end T1;

------------------------- Task 2--------------------------
   task body T2 is
      Sum1, Sum2: Integer := 0;
      B, Z: Vector_H;
      R: Vector_2H;
      A: Vector_3H;
      MO, ME: Matrix_2H;
      MZ: Matrix_3H;
      m, m1, m2: Integer;
      MTx: Matrix_H;
   begin
      put_Line("T2 started!");

      accept Send_BZ_T1 (Bh : in Vector_H; Zh : in Vector_H) do
         B := Bh;
         Z := Zh;
      end Send_BZ_T1;

      accept Send_MOME_T3 (MO2h : in Matrix_2H; ME2h : in Matrix_2H) do
         MO := MO2h;
         ME := ME2h;
      end Send_MOME_T3;
      T1.Send_MOME_T2(MO(H+1 .. 2*H), ME(H+1 .. 2*H));
      accept Send_R_T3 (R2h : in Vector_2H) do
         R := R2h;
      end Send_R_T3;
      T1.Send_R_T2(R(H+1 .. 2*H));

      accept Send_MZ_T1 (MZ3h : in Matrix_3H) do
         MZ := MZ3h;
      end Send_MZ_T1;
      T3.Send_MZ_T2(MZ(H+1 .. 3*H));

      m2 :=100000;
      for I in 1 .. H loop
         if m2 < Z(I) then
            m2 := Z(I);
         end if;
      end loop;

      accept Send_m1_T1 (m1_send : in Integer) do
         m1 := m1_send;
      end Send_m1_T1;

      if m2 < m1 then
         m2 := m1;
      end if;
      T3.Send_m2_T2(m2);

      accept Send_m_T3 (m_res : in Integer) do
         m := m_res;
      end Send_m_T3;

      T1.Send_m_T2(m);

      for i in 1.. H loop
         for j in 1 .. N loop
            Sum1 := 0;
            for z in 1 .. N loop
               Sum1 := Sum1 + MO(J)(Z) * MZ(Z)(I);
            end loop;
            MTx(J)(I) := Sum1;
         end loop;
      end loop;
      for i in 1.. H loop
         Sum1 := 0;
         Sum2 := 0;
         for j in 1 .. N loop
            Sum1 := Sum1 + B(i) * MTx(I)(J);
            Sum2 := Sum2 + m * R(i) * ME(I)(J);
         end loop;
         A(I) := Sum1 + Sum2;
      end loop;

      accept Send_A_T3 (A2h : in Vector_2H) do
         A(H+1 .. 3*H) := A2h;
      end Send_A_T3;
      T1.Send_A_T2(A);
      Put_Line("T2 ended");
   end T2;

------------------------- Task 3--------------------------
   task body T3 is
      Sum1, Sum2: Integer := 0;
      MO, ME: Matrix_N;
      B, Z: Vector_H;
      R: Vector_3H;
      MZ: Matrix_2H;
      m, m2, m3, m4: Integer;
      A: Vector_2H;
      MTx: Matrix_H;
   begin
      put_Line("T3 started!");

       for i in 1 .. N loop
         for j in 1 .. N loop
            MO(i)(j) := 1;
            ME(i)(j) := 1;
         end loop;
      end loop;

      accept Send_BZ_T4 (Bh : in Vector_H; Zh : in Vector_H) do
         B := Bh;
         Z := Zh;
      end Send_BZ_T4;

      T2.Send_MOME_T3(MO(H+1 .. 3*H), ME(H+1 .. 3*H));
      T4.Send_MOME_T3(MO(3*H+1 .. N), ME(3*H+1 .. N));

      accept Send_R_T4 (R3h : in Vector_3H) do
         R := R3h;
      end Send_R_T4;
      T2.Send_R_T3(R(H+1 .. 3*H));

      accept Send_MZ_T2 (MZ2h : in Matrix_2H) do
         MZ := MZ2h;
      end Send_MZ_T2;
      T4.Send_MZ_T3(MZ(H+1 .. 2*H));

      m3 :=100000;
      for I in 1 .. H loop
         if m3 < Z(I) then
            m3 := Z(I);
         end if;
      end loop;

      accept Send_m2_T2 (m2_send : in Integer) do
         m2 := m2_send;
      end Send_m2_T2;

      if m3 < m2 then
         m3 := m2;
      end if;

      accept Send_m4_T4 (m4_send : in Integer) do
         m4 := m4_send;
      end Send_m4_T4;

      if m3 < m4 then
         m3 := m4;
      end if;

      m := m3;
      T2.Send_m_T3(m);
      T4.Send_m_T3(m);

      for i in 1.. H loop
         for j in 1 .. N loop
            Sum1 := 0;
            for z in 1 .. N loop
               Sum1 := Sum1 + MO(J)(Z) * MZ(Z)(I);
            end loop;
            MTx(J)(I) := Sum1;
         end loop;
      end loop;
      for i in 1.. H loop
         Sum1 := 0;
         Sum2 := 0;
         for j in 1 .. N loop
            Sum1 := Sum1 + B(i) * MTx(I)(J);
            Sum2 := Sum2 + m * R(i) * ME(I)(J);
         end loop;
         A(I) := Sum1 + Sum2;
      end loop;

      accept Send_A_T4 (Ah : in Vector_H) do
         A(H+1 .. 2*H) := Ah;
      end Send_A_T4;
      T2.Send_A_T3(A);
      Put_Line("T3 ended");
   end T3;

------------------------- Task 4--------------------------
   task body T4 is
      Sum1, Sum2: Integer := 0;
      B, Z: Vector_2H;
      A: Vector_H;
      R: Vector_4H;
      MO, ME: Matrix_3H;
      MZ: Matrix_H;
      m, m5, m4: Integer;
      MTx: Matrix_H;
   begin
      put_Line("T4 started!");

      accept Send_BZ_T5 (B2h : in Vector_2H; Z2h : in Vector_2H) do
         B := B2h;
         Z := Z2h;
      end Send_BZ_T5;
      T3.Send_BZ_T4(B(H+1 .. 2*H), Z(H+1 .. 2*H));

      accept Send_MOME_T3 (MO3h : in Matrix_3H; ME3h : in Matrix_3H) do
         MO := MO3h;
         ME := ME3h;
      end Send_MOME_T3;
      T5.Send_MOME_T4(MO(H+1 .. 3*H), ME(H+1 .. 3*H));

      accept Send_R_T5 (R4h : in Vector_4H) do
         R := R4h;
      end Send_R_T5;
      T3.Send_R_T4(R(H+1 .. 4*H));

      accept Send_MZ_T3 (MZh : in Matrix_H) do
         MZ := MZh;
      end Send_MZ_T3;
      -- dont go
      m4 :=100000;
      for I in 1 .. H loop
         if m4 < Z(I) then
            m4 := Z(I);
         end if;
      end loop;

      accept Send_m5_T5 (m5_send : in Integer) do
         m5 := m5_send;
      end Send_m5_T5;

      if m4 < m5 then
         m4 := m5;
      end if;

      T3.Send_m4_T4(m4);

      accept Send_m_T3 (m_res : in Integer) do
         m := m_res;
      end Send_m_T3;

      T5.Send_m_T4(m);

      for i in 1.. H loop
         for j in 1 .. N loop
            Sum1 := 0;
            for z in 1 .. N loop
               Sum1 := Sum1 + MO(J)(Z) * MZ(Z)(I);
            end loop;
            MTx(J)(I) := Sum1;
         end loop;
      end loop;
      for i in 1.. H loop
         Sum1 := 0;
         Sum2 := 0;
         for j in 1 .. N loop
            Sum1 := Sum1 + B(i) * MTx(I)(J);
            Sum2 := Sum2 + m * R(i) * ME(I)(J);
         end loop;
         A(I) := Sum1 + Sum2;
      end loop;
      T3.Send_A_T4(A);
      Put_Line("T4 ended");
   end T4;

------------------------- Task 5--------------------------
   task body T5 is
      Sum1, Sum2: Integer := 0;
      R: Vector_N;
      B, Z: Vector_3H;
      MO, ME: Matrix_2H;
      MZ: Matrix_H;
      m, m5, m6: Integer;
      A: Vector_H;
      MTx: Matrix_H;
   begin
      put_Line("T5 started!");

      for i in 1 .. N loop
         R(i):=1;
      end loop;

      accept Send_BZ_T6 (B3h : in Vector_3H; Z3h : in Vector_3H) do
         B := B3h;
         Z := Z3h;
      end Send_BZ_T6;
      T4.Send_BZ_T5(B(H+1 .. 3*H), Z(H+1 .. 3*H));

      accept Send_MOME_T4 (MO2h : in Matrix_2H; ME2h : in Matrix_2H) do
         MO := MO2h;
         ME := ME2h;
      end Send_MOME_T4;
      T6.Send_MOME_T5(MO(H+1 .. 2*H), ME(H+1 .. 2*H));

      T6.Send_R_T5(R(H+1 .. 2*H));
      T4.Send_R_T5(R(2*H+1 .. N));

      accept Send_MZ_T6 (MZh : in Matrix_H) do
         MZ := MZh;
      end Send_MZ_T6;

      m5 :=100000;
      for I in 1 .. H loop
         if m5 < Z(I) then
            m5 := Z(I);
         end if;
      end loop;

      accept Send_m6_T6 (m6_send : in Integer) do
         m6 := m6_send;
      end Send_m6_T6;

      if m5 < m6 then
         m5 := m6;
      end if;

      T4.Send_m5_T5(m5);

      accept Send_m_T4 (m_res : in Integer) do
         m := m_res;
      end Send_m_T4;

      T6.Send_m_T5(m);

      for i in 1.. H loop
         for j in 1 .. N loop
            Sum1 := 0;
            for z in 1 .. N loop
               Sum1 := Sum1 + MO(J)(Z) * MZ(Z)(I);
            end loop;
            MTx(J)(I) := Sum1;
         end loop;
      end loop;
      for i in 1.. H loop
         Sum1 := 0;
         Sum2 := 0;
         for j in 1 .. N loop
            Sum1 := Sum1 + B(i) * MTx(I)(J);
            Sum2 := Sum2 + m * R(i) * ME(I)(J);
         end loop;
         A(I) := Sum1 + Sum2;
      end loop;

      T6.Send_A_T5(A);
      Put_Line("T5 ended");
   end T5;

------------------------- Task 6--------------------------
   task body T6 is
      Sum1, Sum2: Integer := 0;
      MZ: Matrix_N;
      A: Vector_2H;
      B, Z: Vector_4H;
      MO, ME: Matrix_H;
      R: Vector_H;
      m6, m: Integer;

      MTx: Matrix_H;
   begin
      put_Line("T6 started!");

      for i in 1 .. N loop
         for j in 1..N loop
            MZ(i)(j):=1;
         end loop;
      end loop;

      accept Send_BZ_T1 (B4h : in Vector_4H; Z4h : in Vector_4H) do
         B := B4h;
         Z := Z4h;
      end Send_BZ_T1;
      T5.Send_BZ_T6(B(H+1 .. 4*H), Z(H+1 .. 4*H));

      accept Send_MOME_T5 (MOh : in Matrix_H; MEh : in Matrix_H) do
         MO := MOh;
         ME := MEh;
      end Send_MOME_T5;

      accept Send_R_T5 (Rh : in Vector_H) do
         R := Rh;
      end Send_R_T5;

      T5.Send_MZ_T6(MZ(H+1 .. 2*H));
      T1.Send_MZ_T6(MZ(2*H+1 .. N));

      --dont go
      m6 :=100000;
      for I in 1 .. H loop
         if m6 < Z(I) then
            m6 := Z(I);
         end if;
      end loop;
      T5.Send_m6_T6(m6);

      accept Send_m_T5 (m_res : in Integer) do
         m := m_res;
      end Send_m_T5;

      for i in 1.. H loop
         for j in 1 .. N loop
            Sum1 := 0;
            for z in 1 .. N loop
               Sum1 := Sum1 + MO(J)(Z) * MZ(Z)(I);
            end loop;
            MTx(J)(I) := Sum1;
         end loop;
      end loop;
      for i in 1.. H loop
         Sum1 := 0;
         Sum2 := 0;
         for j in 1 .. N loop
            Sum1 := Sum1 + B(i) * MTx(I)(J);
            Sum2 := Sum2 + m * R(i) * ME(I)(J);
         end loop;
         A(I) := Sum1 + Sum2;
      end loop;

      accept Send_A_T5 (Ah : in Vector_H) do
         A(H+1 .. 2*H) := Ah;
      end Send_A_T5;
      T1.Send_A_T6(A);
      Put_Line("T6 ended");
   end T6;

begin
   time1:=clock;
   null;
end main;
