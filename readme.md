in this project we have added jenkins file and docker file
written shell scripts
the final layout of the project should be
currency-conversion-devops/
│
├── src/
│   ├── main/
│   └── test/
│
├── scripts/
│   ├── server_check.sh
│   ├── log_check.sh
│   └── archive_logs.sh
│
├── logs/
│
├── Dockerfile
├── Jenkinsfile
├── pom.xml
├── README.md
└── .gitignore


configured jenkins with jenkinssetup.sh
then installed required plugins.
build jar file using mvn clean install
now created 2 ec2 instances 1.sonarqube 2.Jfrog 
1. sonarqube 9000 port 
sudo yum install docker -y && sudo systemctl start docker && sudo systemctl enable docker 
sudo docker run -p 9000:9000 sonarqube
then configure and quality gates as well update pipeline with sonarqube connection and code quality check before that webhook jenkins in sonarqube so that jenkins server can get response from sonarqube server . if the code quality is good then jenkins will perform code quality checks
once .jar file is generated 
2. config jfrog install docker in 3rd ec2 instance 
sudo yum install docker -y && sudo systemctl start docker && sudo systemctl enable docker 
sudo docker run -itd -p 8081:8081 -p 8082:8082 vikasmanda/jfrog-oss:latest
create artifactory repository
install artifactory plugin in jenkins then add jfrog server in jenkins global tool configuration
then we can able to upload artifacts to jfrog server using jenkins