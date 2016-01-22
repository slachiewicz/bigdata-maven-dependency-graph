package nl.ordina.jtech.mavendependencygraph.indexer
  
import java.io.ObjectOutputStream
import java.net.Socket
import java.net.ServerSocket
import java.io.PrintWriter

class Server(port: Int) {
  val listener: ServerSocket = new ServerSocket(port)

  def serve (write: (PrintWriter) => Int) {
    var cnt = 0
    try {
      val socket: Socket = listener.accept();
      try {
        val out: PrintWriter = new PrintWriter(socket.getOutputStream(), true);
        cnt = write(out)
      } finally {
        socket.close();
      }
    } finally {
      listener.close();
    }
    cnt
  }

}