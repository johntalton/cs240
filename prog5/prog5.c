/****************************************
*    prog5.c -- Parallel intagral prog
*
*   John Talton
*   cs240
*
***************************************/
#include <stdio.h>
#include <time.h>
#define MatrixSize 50
#define PrecentSparse 25

/* We'll be using MPI routines, definitions, etc. */
#include "mpi.h"


void CreateMatrix(int c[50], char V[150], int P[150]){
   int row;
   int col;
   char val;
   int pointer = 0;
   int count;

   time_t MyTime;
   time(&MyTime);
   srand((int)MyTime);

   for(row = 0; row < MatrixSize;row++){
      count = 0;
      for(col = 0; col < MatrixSize; col++){
         if(rand()%PrecentSparse == 0){
            count++;
            val = rand()%26+'a';
      /*      printf("%c",val);*/
            V[pointer]  = val;
            P[pointer++] = col;
         } else { /*printf(" ");*/ }
         c[row] = count;
      }
      /*printf("\n");*/
   }
   /*printf("\n============\n");*/
}


void PrintMatrix(int c[50], char V[100], int P[100]){
  int i,j,t=0,t2=0;
  for(i = 0; i < MatrixSize;i++){
      printf("\n");
      t2 = 0;
      for(j = 0;j < MatrixSize;j++){
         if((c[i] > t2) && (P[t] == j)){
            t++;
            t2++;
            printf("%c", V[t - 1]);
         }  else {
            printf(" ");
         }
      }
   }
   printf("\n--------------\n");
}


main(int argc, char** argv) {
   int         my_rank;   /* My process rank           */
   int   	p;
   int         tag = 0;
   MPI_Status  status;

   int RowCount[50];
   char Value[150];
   int Position[150];

   int Col,count,j,k,send;
   char tempVsend[50];
   int tempPsend[50];
   char buffer[400];
   int position = 0;


   /* Let the system do what it needs to start up MPI */
   MPI_Init(&argc, &argv);

   /* Get my process rank */
   MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

   /* Find out how many processes are being used */
   MPI_Comm_size(MPI_COMM_WORLD, &p);


   /* Add up the integrals calculated by each process */
   if (my_rank == 0) {
      CreateMatrix(RowCount,Value,Position);
      PrintMatrix(RowCount,Value,Position);

      tag = 0;
      for(Col = 0;Col<MatrixSize;Col++){
         count = 0;
         send = 0;
         for(j = 0;j<MatrixSize;j++){
            for(k = 0;k<RowCount[j];k++){
               count++;
               if(Position[count]==Col){
                  tempVsend[send]  =  Value[count];
                  tempPsend[send++] = j;
                  /*printf("%c",Value[count]);*/
               }
            }
         }
         /* Pack And send it to process 1 */

         position = 0;
         MPI_Pack(&send,         1, MPI_INT, buffer, 400, &position, MPI_COMM_WORLD);
         MPI_Pack(&tempVsend, send, MPI_CHAR,buffer, 400, &position, MPI_COMM_WORLD);
         MPI_Pack(&tempPsend, send, MPI_INT, buffer, 400, &position, MPI_COMM_WORLD);
         MPI_Send(buffer, 400, MPI_CHAR, 1, tag++, MPI_COMM_WORLD);
      }






   } else if(my_rank == 1) {
      /*  get and Unpack data from process 0 */
      tag=0;
      count =0;printf("TRANS starts here");
      for(Col = 0; Col < MatrixSize; Col++){
         position=0;
         MPI_Recv(buffer, 400, MPI_CHAR, 0, tag++, MPI_COMM_WORLD, &status);
         MPI_Unpack(buffer, 400, &position, &RowCount[Col], 1,MPI_INT, MPI_COMM_WORLD);
         MPI_Unpack(buffer, 400, &position, &Value[count], RowCount[Col],MPI_CHAR, MPI_COMM_WORLD);
         MPI_Unpack(buffer, 400, &position, &Position[count], RowCount[Col],MPI_INT, MPI_COMM_WORLD);
         count += RowCount[Col];
      }



      PrintMatrix(RowCount,Value,Position);
   }


   /* Shut down MPI */
   MPI_Finalize();
} /*  main  */


