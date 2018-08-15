package excel
import org.apache.poi.ss.usermodel.{ DataFormatter, WorkbookFactory, Row, Cell }
import java.io.File
import collection.JavaConversions._ // lets you iterate over a java iterable

object Main extends App {
  println("Hello, world")
  val f = new File("src/main/scala/data/sales.xls")
  val workbook = WorkbookFactory.create(f)

  // Sheet name
  println(workbook.getSheetName(0))
  val sheet = workbook.getSheetAt(0) // Assuming they're in the first sheet her

  val rowIterator = sheet.iterator()

  var count = 0;
  while(rowIterator.hasNext && count < 5){
    val row = rowIterator.next()

    val cellIterator = row.cellIterator()
    while(cellIterator.hasNext) {
      val cell = cellIterator.next()
      cell.getCellType match {
        case Cell.CELL_TYPE_STRING => {
          print(cell.getStringCellValue + "\t")
        }
        case Cell.CELL_TYPE_NUMERIC => {
          print(cell.getNumericCellValue + "\t")
        }

        case Cell.CELL_TYPE_BLANK => {
          print("null" + "\t")
        }
      }
    }
    println("")
    count = count + 1;
  }
}
