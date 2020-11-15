run-nodejs:
	npm start --prefix ./nodejs

run-spring:
	mvn -f spring/ spring-boot:run

run-flask:
	python3 ./flask/app.py

install-dependencies:
	pip install -r ./flask/requirements.txt