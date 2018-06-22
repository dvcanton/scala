class ExampleJob{
  private var sc:   SparkContext;

  public ExampleJob(SparkContext sc) {
    this.sc = sc;
  }

  def run(t: String, u: String) : RDD[(String, String)] = {
    val transactions = sc.textFile(t)

    // Transforming the transactions text file into a Key/Value RDD
    val newTransactionsPair = transactions.map{t =>
      val p = t.split("\t")
      (p(2).toInt, p(1).toInt)

    }

    // Transforming the users text file into a Key/Value RDD

    val users = sc.textFile(u)
    val newUsersPair = users.map{t =>
      val p = t.split("\t")
      (p(0).toInt, p(3))
    }

    val result = processData(newTransactionsPair, newUsersPair)
    return sc.parallelize(result.toSeq).map(t => (t._1.toString, t._2.toString))
  }

  def processData (t: RDD[(Int, Int)], u: RDD[(Int, String)]) : Map[Int,Long] = {
    var jn = t.leftOuterJoin(u).values.distinct
    return jn.countByKey
  }
}

object ExampleJob {
  def main(args: Array[String]) {
        val transactionsIn = args(1)
        val usersIn = args(0)

        // Defining the Spark Context
        val conf = new SparkConf().setAppName("SparkJoins")
                                  .setMaster("local")
        val sc = new SparkContext(conf)

        val job = new ExampleJob(sc)
        val results = job.run(transactionsIn, usersIn)
        val output = args(2)
        results.saveAsTextFile(output)
        context.stop()
  }
}


/*

users
1 matthew@test.com  EN  US
2 matthew@test2.com EN  GB
3 matthew@test3.com FR  FR
/*
