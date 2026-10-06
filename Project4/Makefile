JFLAGS=-cp .
JAR=jar
JAVA=java
JAVAC=javac
JAVACC=javacc

PARSEDIR=Parse
MJ_PARSER=MiniJavaParser

DEFAULT_JAVACCGEN = $(PARSEDIR)/SimpleCharStream.java \
	$(PARSEDIR)/ParseException.java \
	$(PARSEDIR)/Token.java \
	$(PARSEDIR)/TokenMgrError.java

MJ_JAVACCGEN = $(DEFAULT_JAVACCGEN) \
	$(PARSEDIR)/$(MJ_PARSER).java \
	$(PARSEDIR)/$(MJ_PARSER)Constants.java \
	$(PARSEDIR)/$(MJ_PARSER)TokenManager.java


# Project 4 - Parser

ASTSRC = $(wildcard Absyn/*.java)
TYPESRC = $(wildcard Types/*.java)
SYMSRC = $(wildcard Symbol/*.java)

PARSESRC = Parse/Main.java $(ASTSRC)
SEMANTSRC = Semant/Main.java Semant/ClassBuilder.java

SEMANTDIR = Semant

DEFAULT_READABSYNGEN = $(SEMANTDIR)/SimpleCharStream.java \
	$(SEMANTDIR)/ParseException.java \
	$(SEMANTDIR)/Token.java \
	$(SEMANTDIR)/TokenMgrError.java

READABSYNGEN = $(DEFAULT_READABSYNGEN) \
	$(SEMANTDIR)/ReadAbsyn.java \
	$(SEMANTDIR)/ReadAbsynConstants.java \
	$(SEMANTDIR)/ReadAbsynTokenManager.java

all: $(PARSEDIR)/$(MJ_PARSER).java \
	$(SEMANTDIR)/ReadAbsyn.java \
	$(PARSESRC:.java=.class) \
	$(TYPESRC:.java=.class) \
	$(SYMSRC:.java=.class) \
	$(SEMANTSRC:.java=.class)

$(PARSEDIR)/$(MJ_PARSER).java: $(PARSEDIR)/$(MJ_PARSER).jj
	cd $(PARSEDIR); ${JAVACC} $(MJ_PARSER).jj;
	${JAVAC} ${JFLAGS} ${MJ_JAVACCGEN}

$(SEMANTDIR)/ReadAbsyn.java: $(SEMANTDIR)/ReadAbsyn.jj
	cd $(SEMANTDIR); ${JAVACC} ReadAbsyn.jj;
	${JAVAC} ${JFLAGS} ${READABSYNGEN}

%.class : %.java
	${JAVAC} ${JFLAGS} $<

clean:
	rm -f $(PARSESRC:.java=.class) \
		$(TYPESRC:.java=.class) \
		$(SYMSRC:.java=.class) \
		$(SEMANTSRC:.java=.class) \
		$(MJ_JAVACCGEN) $(MJ_JAVACCGEN:.java=.class) \
		$(READABSYNGEN) $(READABSYNGEN:.java=.class) \
                '$(PARSEDIR)/$(MJ_PARSER)$$JJCalls.class' \
                '$(PARSEDIR)/$(MJ_PARSER)$$LookaheadSuccess.class' \
                '$(SEMANTDIR)/ReadAbsyn$$JJCalls.class' \
                '$(SEMANTDIR)/ReadAbsyn$$LookaheadSuccess.class'

realclean: clean
	rm -f */*.class *~ */*~ *.jar
