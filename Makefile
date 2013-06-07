all:
	mvn -U package
	cp target/minecartmaniacore-*.jar MinecartMania.jar

clean:
	mvn -U clean package
	cp target/minecartmaniacore-*.jar MinecartMania.jar