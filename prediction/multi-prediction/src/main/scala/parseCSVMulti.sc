import scala.io.Source

val input = Source.fromFile("/Users/abomm/Documents/code/scala/prediction/multi-prediction/assets/train.csv").getLines.toList.map(_.split(',').toList)

val grouped = input.groupBy(_.last)

grouped.head._2.head


def fact2tail(i: BigInt): BigInt = {
  def f(i: BigInt, accum: BigInt): BigInt =  if (i == 0) accum else f(i - 1, i * accum)
  f(i,1)
}

fact2tail(34)

def fact5(n: Int): Int = n match {
  case 0 => 1
  case n => n * fact5(n - 1)
}
/*

def sumIter[T](a: Iterable[T]): T = {
  def s[T](a: Iterable[T], accum: T): T = if (a.size == 0) accum else s(a.tail, accum + a.head )
  s(a,0)
}

sumIter(List(1,2,3,4,5))
*/

object StringCons {
  def apply(c: Char, s: String) = c + s
  def unapply(s: String) : Option[(Char,String)] = s match {
    case "" => None
    case _ => Some((s.charAt(0), s.substring(1)))
  }
}

"xfaggotry" match {
  case StringCons('x', rest) => println("not me")
  case StringCons('f', rest) => println("the rest is " + rest + ".")
}
