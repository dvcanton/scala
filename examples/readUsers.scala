var usersFile = sc.textFile("hdfs://localhost:9000/users.txt")
var df = usersFile.toDF("line")
df.show()
