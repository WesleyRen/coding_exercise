# 1.
mvn clean package

# 2.
java -cp target/EmailUtil-1.0-SNAPSHOT.jar EmailUtil "from@here.com" "My Name" "you@none-existence.nono.com, you@other.company.com" "Test Subject" "Test body" "smtp.server.com" 25

