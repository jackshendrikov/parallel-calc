------------------------------------------------------
--|                    Labwork #5                    |
--|                 Ada. Rendezvous                  |
------------------------------------------------------
--|   Author   |    Jack (Yevhenii) Shendrikov       |
--|   Group    |                IO-82                |
--|   Variant  |                 #24                 |
--|    Date    |             06.04.2020              |
------------------------------------------------------
--| Function   |    Z = (B*C)*D + E*(MA*MB)*x        |
------------------------------------------------------

with Ada.Text_IO, Ada.Integer_Text_IO;
use Ada.Text_IO, Ada.Integer_Text_IO;

procedure Main is
   N: Integer := 4;
   P: Integer := 4;
   H: Integer := N / P;
   FILL_NUM: Integer := 1;

   type Vector_All is array(Integer range <>) of Integer;
   subtype Vector is Vector_All(1..N);
   subtype VectorH is Vector_All(1..1*H);
   subtype Vector2H is Vector_All(1..2*H);
   subtype Vector3H is Vector_All(1..3*H);


   type Matrix_All is array(Integer range <>) of Vector;
   subtype Matrix is Matrix_All(1..N);
   subtype MatrixH is Matrix_All(1..1*H);
   subtype Matrix2H is Matrix_All(1..2*H);
   subtype Matrix3H is Matrix_All(1..3*H);


   -- Input Procedures for Number, Vector, Matrix --
   procedure NumberInput(x: out Integer) is
   begin
      x := FILL_NUM;
   end NumberInput;

   procedure VectorInput(V: out Vector) is
   begin
      for i in 1..N loop
         V(i) := FILL_NUM;
      end loop;
   end VectorInput;

   procedure MatrixInput(M: out Matrix) is
   begin
      for i in 1..N loop
         for j in 1..N loop
            M(i)(j) := FILL_NUM;
         end loop;
      end loop;
   end MatrixInput;


   -- Output Procedures for Vector --
   procedure VectorOutput(V: in Vector; str: in String) is
   begin
      Put("Vector " & str & ":");
      for i in 1..N loop
         put(V(i));
         put(" ");
      end loop;
      Put_Line("");
   end VectorOutput;


   -- Calculation Procedure [Zh = b * Dh + E * (MA * MBh) * x] --
   procedure Calculate(b : Integer; DH : VectorH; E : Vector; MA : Matrix; MBH : MatrixH; x : Integer; ZH : out VectorH) is
      sum1, sum2 : Integer;
   begin
      for i in 1..H loop
         sum1 := 0;
         for j in 1..N loop
            sum2 := 0;
            for k in 1..N loop
               -- Calculate MA * MBh --
               sum2 := sum2 + MA(k)(j) * MBH(i)(k);
            end loop;
            -- Calculate E * (MA * MBh) * x --
            sum1 := sum1 + E(j) * sum2 * x;
         end loop;
         -- Calculate Zh --
         ZH(i) := b * DH(i) + sum1;
      end loop;
      end Calculate;



   procedure tasksProcedure is
      task T1 is
         pragma Storage_Size(300_000_000);
         entry DataFromT4(VCH : in VectorH; VDH : in VectorH; V : in Vector; M : in MatrixH);
         entry ResBT2ToT1(b : Integer);
         entry ResZ3HToT1(ResZ3H : Vector3H);
      end T1;

      task T2 is
         pragma Storage_Size(300_000_000);
         entry DataFromT1(a : in Integer; V3H : in Vector3H; M : in Matrix);
         entry DataFromT4(VC2H : in Vector2H; VD2H : in Vector2H; V : in Vector; M : in Matrix2H);
         entry B1FromT1(b1 : Integer);
         entry B3FromT3(b3 : Integer);
         entry ResZ2HToT2(ResZ2H : Vector2H);
      end T2;

      task T3 is
         pragma Storage_Size(300_000_000);
         entry DataFromT4(VC3H : in Vector3H; VD3H : in Vector3H; V : in Vector; M : in Matrix3H);
         entry DataFromT1(a : in Integer; V2H : in Vector2H; M : in Matrix);
         entry B4FromT4(b4 : Integer);
         entry ResBT2ToT3(b : Integer);
         entry ResZHToT3(ResZH : VectorH);
      end T3;

      task T4 is
         pragma Storage_Size(300_000_000);
         entry DataFromT1(a : in Integer; VH : in VectorH; M : in Matrix);
         entry ResBT3ToT4(b : in Integer);
      end T4;

      task body T1 is
         x, b1, res : Integer;
         B, E, Z: Vector;
         MA: Matrix;

         CH, DH, ZH : VectorH;
         MBH : MatrixH;

      begin
         Put_Line("T1 started.");

         -- 1) Enter x, B, MA
         NumberInput(x);
         VectorInput(B);
         MatrixInput(MA);

         -- 2) Pass x, B3H, MA to T2
         T2.DataFromT1(x, B(H+1..N), MA);

         -- 3) Get CH, DH, E, MBH from T2
         accept DataFromT4(VCH : in VectorH; VDH : in VectorH; V : in Vector; M : in MatrixH) do
            CH := VCH;
            DH := VDH;
            E := V;
            MBH := M;
         end DataFromT4;

         -- 4) Calculate b1 = BH * CH
         b1 := 0;
         for i in 1..H loop
            b1 := b1 + B(i) * CH(i);
         end loop;

         -- 5) Pass b1 to T2
         T2.B1FromT1(b1);

         -- 6) Get b from T2
         accept ResBT2ToT1 (b : in Integer) do
            res := b;
         end ResBT2ToT1;

         -- 7) Calculate ZH = b * DH + E * (MA * MBH) * x
         Calculate(res, DH, E, MA, MBH, x, ZH);

         -- 8) Get Z3H from T2
         accept ResZ3HToT1 (ResZ3H : in Vector3H) do
            Z(1..H) := ZH;
            Z(H+1..N) := ResZ3H;
         end ResZ3HToT1;

         -- 9) Print Z
         if (N < 10) then
            delay(0.5);
            VectorOutput(Z, "Z");
         end if;

         Put_Line("T1 finished.");

      end T1;

      task body T2 is
         x, b2, b : Integer;
         E : Vector;
         MA : Matrix;

         ZH : VectorH;
         C2H, D2H : Vector2H;
         B3H, Z3H : Vector3H;
         MB2H : Matrix2H;

      begin
         Put_Line("T2 started.");

         -- 1) Get x, B3H, MA from T1
         accept DataFromT1(a : in Integer; V3H : in Vector3H; M : in Matrix) do
            x := a;
            B3H := V3H;
            MA := M;
         end DataFromT1;

         -- 2) Pass x, B2H, MA to T3
         T3.DataFromT1(x, B3H(H+1..3*H), MA);

         -- 3) Get C2H, D2H, E, MB2H from T3
         accept DataFromT4(VC2H : in Vector2H; VD2H : in Vector2H; V : in Vector; M : in Matrix2H) do
            C2H := VC2H;
            D2H := VD2H;
            E := V;
            MB2H := M;
         end DataFromT4;

         -- 4) Pass CH, DH, E, MBH to T1
         T1.DataFromT4(C2H(H+1..2*H), D2H(H+1..2*H), E, MB2H(H+1..2*H));

         -- 5) Calculate b2 = BH * CH
         b2 := 0;
         for i in 1..H loop
            b2 := b2 + B3H(i) * C2H(i);
         end loop;

         -- 6) Get b1 from T1
         -- 7) Calculate b2 = b2 + b1
         accept B1FromT1 (b1 : in Integer) do
            b2 := b2 + b1;
         end B1FromT1;

         -- 8) Get b3 from the problem T3
         accept B3FromT3 (b3 : in Integer) do
            b2 := b2 + b3;
         end B3FromT3;

         -- 9) Calculate b = b2 + b3
         b:= b2;

         -- 10) Pass b to T1, T3
         T1.ResBT2ToT1(b);
         T3.ResBT2ToT3(b);

         -- 11) Calculate ZH = b * DH + E * (MA * MBH) * x
         Calculate(b2, D2H(1..H), E, MA, MB2H(1..H), x, ZH);

         -- 12) Get Z2H from T3
         accept ResZ2HToT2 (ResZ2H : in Vector2H) do
            Z3H(1..H) := ZH;
            Z3H(H+1..3*H) := ResZ2H;
         end ResZ2HToT2;

         -- 13) Pass Z3H to T1
         T1.ResZ3HToT1(Z3H);

         Put_Line("T2 finished.");
      end T2;

      task body T3 is
         x, b3, res : Integer;
         E : Vector;
         MA : Matrix;

         ZH : VectorH;
         B2H, Z2H : Vector2H;
         C3H, D3H : Vector3H;
         MB3H : Matrix3H;

      begin
         Put_Line("T3 started.");

         -- 1) Get C3H, D3H, E, MB3H from T4
         accept DataFromT4(VC3H : in Vector3H; VD3H : in Vector3H; V : in Vector; M : in Matrix3H) do
            C3H := VC3H;
            D3H := VD3H;
            E := V;
            MB3H := M;
         end DataFromT4;

         -- 2) Get x, B2H, MA from T2
         accept DataFromT1(a : in Integer; V2H : in Vector2H; M : in Matrix) do
            x := a;
            B2H := V2H;
            MA := M;
         end DataFromT1;

         -- 3) Pass C2H, D2H, E, MB2H to T2
         T2.DataFromT4(C3H(H+1..3*H), D3H(H+1..3*H), E, MB3H(H+1..3*H));

         -- 4) Pass x, BH, MA to T4
         T4.DataFromT1(x, B2H(H+1..2*H), MA);

         -- 5) Calculate b3 = BH * CH
         b3 := 0;
         for i in 1..H loop
            b3 := b3 + B2H(i) * C3H(i);
         end loop;

         -- 6) Get b4 from T4
         -- 7) Calculate b3 = b3 + b4
         accept B4FromT4 (b4 : in Integer) do
            b3 := b3 + b4;
         end B4FromT4;

         -- 8) Pass b3 to T2
         T2.B3FromT3(b3);

         -- 9) Get b from T2
         accept ResBT2ToT3 (b : in Integer) do
            res := b;
         end ResBT2ToT3;

         -- 10) Pass b to T4
         T4.ResBT3ToT4(res);

         -- 11) Calculate ZH = b * DH + E * (MA * MBH) * x
         Calculate(res, D3H(1..H), E, MA, MB3H(1..H), x, ZH);

         -- 12) Get ZH from T4
         accept ResZHToT3 (ResZH : in VectorH) do
            Z2H(1..H) := ZH;
            Z2H(H+1..2*H) := ResZH;
         end ResZHToT3;

         -- 13) Pass Z2H to T2
         T2.ResZ2HToT2(Z2H);

         Put_Line("T3 finished.");
      end T3;

      task body T4 is
         x, b4, res : Integer;
         C, D, E : Vector;
         MB, MA : Matrix;

         BH, ZH : VectorH;
      begin
         Put_Line("T4 started.");

         -- 1) Enter C, D, E, MB
         VectorInput(C);
         VectorInput(D);
         VectorInput(E);
         MatrixInput(MB);

         -- 2) Pass C3H, D3H, E, MB3H to T3
         T3.DataFromT4(C(H+1..N), D(H+1..N), E, MB(H+1..N));

         -- 3) Get x, BH, MA from T3
         accept DataFromT1(a : in Integer; VH : in VectorH; M : in Matrix) do
            x := a;
            BH := VH;
            MA := M;
         end DataFromT1;

         -- 4) Calculate b4 = BH * CH
         b4 := 0;
         for i in 1..H loop
            b4 := b4 + BH(i) * C(i);
         end loop;

         -- 5) Pass b4 to T3
         T3.B4FromT4(b4);

         -- 6) Get b from T3
         accept ResBT3ToT4 (b : in Integer) do
            res := b;
         end ResBT3ToT4;

         -- 7) Calculate ZH = b * DH + E * (MA * MBH) * x
         Calculate(res, D(1..H), E, MA, MB(1..H), x, ZH);

         -- 8) Pass ZH to T3
         T3.ResZHToT3(ZH);

         Put_Line("T4 finished.");
      end T4;
   begin
      Put_Line("");
   end tasksProcedure;

begin
   Put_Line("Lab5 started!"); New_Line;
   tasksProcedure;
   New_Line; Put_Line("Lab5 finished!"); New_Line;
end Main;
