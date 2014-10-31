/**
 * Created by abomm on 8/5/14.
 */

import org.apache.spark._
import org.apache.spark.mllib.classification.{LogisticRegressionWithSGD, SVMWithSGD}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.util.MLUtils

import scala.io.Source


object MLTest {
  def main(args: Array[String]) = {

    val conf = new SparkConf().setAppName("Multi Predict")
    val sc = new SparkContext(conf)

    val input = Source.fromFile("/Users/abomm/Documents/code/scala/prediction/multi-prediction/assets/train.csv").getLines.toList.map(_.split(',').toList)
    val grouped = input.groupBy(_.last)



    // Load training data in LIBSVM format.
    val data = MLUtils.loadLibSVMFile(sc, "/Users/abomm/Documents/spark-1.0.0/mllib/data/sample_libsvm_data.txt")

    // Split data into training (60%) and test (40%).
    val splits = data.randomSplit(Array(0.6, 0.4), seed = 11L)
    val training = splits(0).cache()
    val test = splits(1)

    // Run training algorithm to build the model
    val numIterations = 10
//    val model = SVMWithSGD.train(training, numIterations)
    val model = LogisticRegressionWithSGD.train(training, numIterations)

    // Clear the default threshold.
    model.clearThreshold()

    // Compute raw scores on the test set.
    val scoreAndLabels = test.map { point =>
      val score = model.predict(point.features)
      (score, point.label)
    }

   // scoreAndLabels foreach println

    val hits = (scoreAndLabels.map {tup => if (tup._1 == tup._2) 1 else 0} reduce (_ + _))
    val len = scoreAndLabels.count
    val accuracy = hits.toDouble / len.toDouble
    println("hits" + hits + " len "  + len)

    println("DEBUG: Accuracy " + accuracy)

    // Get evaluation metrics.
    val metrics = new BinaryClassificationMetrics(scoreAndLabels)
    val auROC = metrics.areaUnderROC()

    println("Area under ROC = " + auROC)


  }
}
