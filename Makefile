LIB_ANTLR := lib/antlr.jar
ANTLR_SCRIPT := Retreet.g4

all: parser

parser:
	rm -rf build
	mkdir build
	java -cp $(LIB_ANTLR) org.antlr.v4.Tool -o build $(ANTLR_SCRIPT)
	rm -rf classes
	mkdir classes
	javac -source 1.7 -target 1.7 -cp $(LIB_ANTLR) -d classes src/*.java build/*.java
clean:
	rm -rf classes build

.PHONY: all parser clean
