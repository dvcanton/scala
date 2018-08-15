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

object naiveBayes {
  def main(args: Array[String]){
    val sc = new SparkContext( "local", "SGD Linear Regression", "/usr/local/spark")
    /* local = master URL; SGD Linear Regression = application name; */
    /* /usr/local/spark = Spark Home;  */

    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    val file = "data/sample_libsvm_data.txt"
    val sqlContext = new SQLContext(sc)
    var data = sqlContext.read.format("libsvm").load(file)

    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3), seed = 1234L)
    val model = new naiveBayes().fit(trainingData)

    val preditions = model.transform(testData)
    preditions.show()

    val evaluator = new MulticlassClassificationEvaluator()
                            .setLabelCol("label")
                            .setPredictionCol("prediction")
                            .setMetricName("accuracy")

    val accuracy = evaluator.evaluate(predictions)
    println("Test set accuracy = " + accuracy)
  }


}
