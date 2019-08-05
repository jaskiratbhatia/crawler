setupDocker:
	docker build --no-cache -t jaskiratcrawlerapp:1.0 -f ./Dockerfile .
test_docker: setupDocker
	docker run -it --rm jaskiratcrawlerapp:1.0 mvn package exec:java -Dexec.mainClass=com.crawler.Crawler -Dexec.args="https://www.google.com/ 1"

test_local:
	mvn package exec:java -Dexec.mainClass=com.crawler.Crawler -Dexec.args="https://www.google.com/ 2"
