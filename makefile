install : 
	mvn verify
	sudo cp target/removetags-1.0-SNAPSHOT.jar /usr/bin
	sudo cp removetags /usr/bin
	sudo chmod +x /usr/bin/removetags
	
