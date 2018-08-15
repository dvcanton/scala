/*
How to run this program
1. Compile and package jar with the application
  > sbt package
2. Run the applicaiton using spark-submit
  > /usr/local/spark/bin/spark-submit --class "logReg" --master local[4] target/scala-2.11/ml-for-spark_2.11-1.0.jar

*/

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrameReader
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SQLContext

object logReg {
  def main(args: Array[String]){
    val sc = new SparkContext( "local", "SGD Linear Regression", "/usr/local/spark")
    /* local = master URL; SGD Linear Regression = application name; */
    /* /usr/local/spark = Spark Home;  */

    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    val file = "data/sample_libsvm_data.txt"
    val sqlContext = new SQLContext(sc)
    var data = sqlContext.read.format("libsvm").load(file);

    val numIterations = 100
    val stepSize = 0.00000001

    val model = new LogisticRegression().setMaxIter(numIterations)
                                        .setRegParam(0.3)
                                        .setElasticNetParam(0.8)

    val result = model.fit(data)

    println(s"Coefficients: ${result.coefficientMatrix}")
    println(s"Intercepts: ${result.interceptVector}")

  }


}
