package Hospit.train

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Gzhtest {
  val conf = new SparkConf().setAppName("TransformationAction").setMaster("local[4]")
  val sc = new SparkContext(conf)
  def main(args: Array[String]): Unit = {
    val data = sc.textFile("D:/Code/Spark/textfile/sampleDataSet")
//    val data = sc.textFile("data.log")
    //    println(timeInterval("12"))
    ipGroupByPrc(data)
    println("--------------------------------------")
    ipGroupByTime(data)
    println("--------------------------------------")
    ipGroupByCity(data)
    println("--------------------------------------")
//    prctSttcIp(data)
    data.foreach(println)
  }
  def systemCount(data:RDD[String]): Unit ={
    val res = data.map(_.split(',')).map(x=>(x(7),1)).reduceByKey(_+_)
    res.foreach(println)
  }
  def ipGroupByPrc(data:RDD[String]): Unit ={
    val res = data.map(_.split(','))
      .map(x=>(x(10),x(9))).distinct().groupByKey().map(x=>(x._1,x._2.size))
    res.foreach(println)
  }
  def ipGroupByCity(data:RDD[String]): Unit ={
    val res = data.map(_.split(','))
      .map(x=>(x(11),x(9))).distinct().groupByKey().map(x=>(x._1,x._2.size))
    res.foreach(println)
  }
  def ipGroupByTime(data:RDD[String]): Unit ={
    val tmpres = data.map(_.split(',')).map(x=>(x(2),x(10),x(11),x(9))).cache()
    val res = tmpres.map(x=>((timeInterval(x._1.split(' ')(1).split(':')(0)),x._2),x._4))
      .distinct()
      .groupByKey().map(x=>(x._1,x._2.size))
      .map(x=>(x._1._1,(x._1._2,x._2))).groupByKey()

    res.foreach(println)
  }

  def  timeInterval(time:String): String ={
    val hour:Int = time.toInt
    if(hour<=9 && 0 <= hour ){
      "[0-9]"
    }
    else if( (hour<=12 && 9 < hour )){
      "[9-12]"
    }
    else if( (hour<=18 && 12 < hour )){
      "[12-18]"
    }
    else{
      "[18-24]"
    }
  }
}
