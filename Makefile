all:
	mvn -U package
	cp target/minecartmaniacore-2.1.3\ beta\ 1.jar MinecartMania.jar

clean:
	mvn -U clean package
	cp target/minecartmaniacore-2.1.3\ beta\ 1.jar MinecartMania.jar