all: deployment.png

clean:
	rm -f *.png

%.png: %.uml
	plantuml -tpng $<
