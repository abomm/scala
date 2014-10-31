import java.io.File

import jsat.ARFFLoader
import jsat.classifiers.boosting.AdaBoostM1
import jsat.classifiers.{ClassificationModelEvaluation, ClassificationDataSet}
import jsat.classifiers.bayesian.NaiveBayes
import jsat.classifiers.trees.RandomForest

import scala.collection.JavaConversions._


/**
 * Created by abomm on 10/24/14.
 */
object ArisML {
  def main(args: Array[String]) {
    val inFile = new File(args(0))
    val dataSet = ARFFLoader.loadArffFile(inFile)

    val classDataSet = new ClassificationDataSet(dataSet, 0)
    val classifier = new RandomForest(100)
//    val classifier = new jsat.classifiers.bayesian.MultinomialNaiveBayes()
//    val classifier = new jsat.classifiers.trees.ERTrees(30)
//    val classifier = new (new NaiveBayes, 20)

    print("Starting training...")
    classifier.trainC(classDataSet)
    println("DONE!")

    /*
    val modelEval = new ClassificationModelEvaluation(classifier, classDataSet)
    println("Starting cross validation")
    modelEval.evaluateCrossValidation(3)

    println(s"cross validation error rate is ${modelEval.getErrorRate}")
    */


    println("Starting to calculate matches...")
    val matches = classDataSet.getDataPointIterator.zipWithIndex.map { case (dp, i) =>
      val truth = classDataSet.getDataPointCategory(i)
      val predicted = classifier.classify(dp).mostLikely
//      println(s"Predicted is $predicted and truth is $truth")
      if (predicted == truth) 1 else 0
    }.toVector
    println(s"Simple accuracy of ${matches.sum.toFloat / matches.length}")

  }

}
