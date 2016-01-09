package nl.ordina.jtech.mavendependencygraph.indexer

import java.io.ObjectOutputStream
import java.net.Socket

class Sender(host: String, port: Int) {
  val socket = new Socket(host, port)
  val out = new ObjectOutputStream(socket.getOutputStream())
  
  def send(msg: Object) {
    out.writeObject(msg)
  }
  
  def flush() {
    out.flush
  }

  def close() {
    socket.close
  }
}