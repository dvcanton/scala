/*
How to run this program
1. Compile and package jar with the application
  > sbt package
2. Run the applicaiton using spark-submit
  > /usr/local/spark/bin/spark-submit --class "logReg" --master local[4] target/scala-2.11/ml-for-spark_2.11-1.0.jar

*/

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint

import org.apache.spark.mllib.feature.Normalizer
import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.mllib.regression.LinearRegressionWithSGD


object sgdLG {
/*
model class class org.apache.spark.mllib.regression.LinearRegressionModel
testData class class org.apache.spark.rdd.MapPartitionsRDD
*/
  def testModel(model : LinearRegressionModel, testData : RDD[LabeledPoint]){

    val valuesAndPreds = testData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    valuesAndPreds.foreach((result) => println(s"predicted label: ${result._1}, actual label: ${result._2}"))
    val MSE = valuesAndPreds.map{ case(v, p) => math.pow((v - p), 2) }.mean()
    println("training Mean Squared Error = " + MSE)

  }

  def main(args: Array[String]){
    val sc = new SparkContext( "local", "SGD Linear Regression", "/usr/local/spark")
    /* local = master URL; SGD Linear Regression = application name; */
    /* /usr/local/spark = Spark Home;  */

    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)

    val file = "data/lpsa.dat"
    val data = sc.textFile(file)
    /* A few words about this data
        * The file is composed of multidimentional data points in the form:
          (y, x1, x2, x3, ..)

        * We are only concened with the last feature (xn)
          in order to find out the relationship:
           y = mx + b
    */

    //val numAs = data.filter(line => line.contains("a")).count()
    //val numBs = data.filter(line => line.contains("b")).count()
    //println(s"Lines with a: $numAs, Lines with b: $numBs")

    val parsedData = data.map { line =>
      val x : Array[String] = line.replace(",", " ").split(" ")
      val y = x.map{ (a => a.toDouble)}
      val d = y.size -1
      val c = Vectors.dense(y(0), y(d))
      LabeledPoint(y(0), c)
    }.cache()

    val splitedData = parsedData.randomSplit(Array(0.8, 0.2), 42)
    val (trainingData, testData) = (splitedData(0), splitedData(1))

    val numIterations = 100
    val stepSize = 0.00000001
    val model = LinearRegressionWithSGD.train(trainingData, numIterations, stepSize)
    testModel(model, testData)

    val numIterations2 = 1000
    val stepSize2 = stepSize/1000
    val model2 = LinearRegressionWithSGD.train(trainingData, numIterations2, stepSize2)
    testModel(model2, testData)


  }


}
