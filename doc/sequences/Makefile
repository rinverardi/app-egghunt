all: postHint1.png postHint2.png

clean:
	rm -f *.png

%.png: %.uml
	plantuml -tpng $<
