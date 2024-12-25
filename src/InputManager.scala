import java.awt.event.{KeyEvent, KeyListener}
import scala.collection.mutable

class InputManager {
  // Map pour associer les codes de touches aux fonctions
  private val bindings: mutable.Map[Int, () => Unit] = mutable.Map()

  // Fonction pour associer une action à une touche
  def bind(keyCode: Int, f: => Unit): Unit = {
    bindings += (keyCode -> (() => f))
  }

  // Méthode pour gérer les événements KeyPressed
  def handleKeyPressed(keyCode: Int): Unit = {
    bindings.get(keyCode).foreach(f => f())
  }

  def handleKeyReleased(keyCode: Int): Unit = {
    bindings.get(keyCode).foreach(f => f())
  }
}