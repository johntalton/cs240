# Generated automatically from Makefile.in by configure.
ALL: default
##### User configurable options #####

SHELL       = /bin/sh
ARCH        = sun4
COMM        = ch_p4
MPIR_HOME   = /usr/local/languages/mpi/
CC          = /usr/local/languages/mpi/lib/sun4/ch_p4/mpicc
CLINKER     = $(CC)
F77         = /usr/local/languages/mpi/lib/sun4/ch_p4/mpif77
FLINKER     = $(F77)
CCC         = /usr/local/languages/mpi/lib/sun4/ch_p4/mpiCC
CCLINKER    = $(CCC)
AR          = ar crl
RANLIB      = ranlib
PMPILIB     = -lpmpich
OPTFLAGS    = 
MPE_LIBS    = -L/usr/local/X11/lib -lmpe -lX11 -lm
MPE_DIR     = /usr/local/languages/mpi/mpe
MPE_GRAPH   = -DMPE_GRAPHICS
#

### End User configurable options ###

CFLAGS   = $(OPTFLAGS) 
CFLAGSMPE = $(CFLAGS) -I$(MPE_DIR) $(MPE_GRAPH)
CCFLAGS   = $(CFLAGS)
#FFLAGS   = '-qdpc=e' 
FFLAGS   = $(OPTFLAGS)
EXECS   = prog5

default: $(EXECS)

all: default

prog5: prog5.o
	$(CLINKER) $(OPTFLAGS) -o prog5 prog5.o -lm
