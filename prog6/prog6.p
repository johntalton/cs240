(* MyProg                                                *)
(* coded by YNOP 1.99                                    *)


SYSTEM prog;

TYPE Letters = SET OF ['A'..'Z']; 

CONFIGURATION grid [1..15],[1..15];
CONNECTION 
        north: grid[i,j] -> grid[i-1,j].south;
        south: grid[i,j] -> grid[i+1,j].north;
        east:  grid[i,j] -> grid[i,j+1].west;
        west:  grid[i,j] -> grid[i,j-1].east;


SCALAR
 ch : CHAR;
 x : INTEGER;
 y : INTEGER;
 w : INTEGER;
 h : INTEGER;
 n : INTEGER;
 x2 : INTEGER;
 y2 : INTEGER;
 count : INTEGER;
 name : ARRAY [1..20] OF CHAR;

VECTOR alive : BOOLEAN;

BEGIN
   PARALLEL
      alive := FALSE;
   ENDPARALLEL;

   WriteString("Enter the file name: ");
   ReadString(name);
   OpenInput(name);
   Readint(count);
   
   FOR n := 1 TO count DO 
      Readint(x);
      Readint(y);
      Readint(w);
      Readint(h);
      x2 := x + w;
      y2 := y + h;
      PARALLEL   
         IF (DIM2 >= x) THEN
            IF (DIM2 < x2 ) THEN
               IF (DIM1 >= y) THEN
                  IF (DIM1 < y2) THEN
                     alive := TRUE;
                  END
               END
            END;
         END; 
      ENDPARALLEL;  
    END; (* end for *)  
 
   PARALLEL
      x := 1;
   ENDPARALLEL;
   
   CloseInput;
  WriteString("Please enter a sequence of commands"); WriteLn;
  Read(ch);
  LOOP
    ch := CAP(ch); 
    LOOP 
      IF Done AND  
       (ch IN Letters{'L','R','U','D','Q'} ) THEN 
         EXIT;
      END;
      Read(ch);
      ch := CAP(ch);
    END; 
    IF (ch = 'Q') THEN 
       WriteString("GoodBye");WriteLn;
       EXIT 
    END;   
    IF (ch = 'L') THEN 
       WriteString("Left Move.");WriteLn;
       PARALLEL
          PROPAGATE.west(alive);
          IF (DIM2 = 15) THEN alive := FALSE; END;
       ENDPARALLEL;
    END;
    IF (ch = 'R') THEN
       WriteString("Right Move.");WriteLn;
       PARALLEL
          PROPAGATE.east(alive);
          IF (DIM2 = 1) THEN alive := FALSE; END;
       ENDPARALLEL;
    END;
    IF (ch = 'U') THEN
       WriteString("Up Move.");WriteLn;
       PARALLEL
          PROPAGATE.north(alive);
          IF (DIM1 = 15) THEN alive := FALSE; END;
       ENDPARALLEL;
    END;
    IF (ch = 'D') THEN
      WriteString("Down Move.");WriteLn;
      PARALLEL
          PROPAGATE.south(alive);
          IF (DIM1 = 1) THEN alive := FALSE; END;
       ENDPARALLEL;
    END;


   PARALLEL
       x := 1;
   ENDPARALLEL;


    Read(ch); (* get next commands; a good place for a breakpoint *)
  END;  (* main loop to process commands *)
END prog.






