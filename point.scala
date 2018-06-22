class Point(xc: Int, yc: Int){
  var x: Int = xc
  var y: Int = yc

  def move(dx: Int, dy: Int){
    x = x + dx
    y = y + dy
    println("Point x new location: " + x);
    println("Point y new location: " + y);
  }
}


object PointDemo {
  def main(args: Array[String]){
    val pt = new Point(10, 20);

    pt.move(10,10);
  }
}
