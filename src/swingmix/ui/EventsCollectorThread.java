/**
 *  Copyright 2009-2020 Jan Schlößin
 *
 *  This file is part of SwingMix.
 *
 *  SwingMix is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SwingMix is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with SwingMix.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Diese Datei ist Teil von SwingMix.
 *
 *  SwingMix ist Freie Software: Sie können es unter den Bedingungen
 *  der GNU Lesser General Public License, wie von der Free Software Foundation,
 *  Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 *  veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 *  SwingMix wird in der Hoffnung, dass es nützlich sein wird, aber
 *  OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 *  Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 *  Siehe die GNU Lesser General Public License für weitere Details.
 *
 *  Sie sollten eine Kopie der GNU Lesser General Public License zusammen mit diesem
 *  Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */

package swingmix.ui;

import javax.swing.SwingUtilities;

/**
 * This class purpose is to collect events in a row and fire a task
 * at most once in 200 ms. This is a nice behaviour for keybord events
 * when you want a task to be executed when the user finished writing.
 *
 * created 18.04.2015
 * @author Jan Schlößin
 */
public class EventsCollectorThread extends Thread {
  public static EventsCollectorThread create(Runnable event) {
    EventsCollectorThread thread = new EventsCollectorThread(event);
    thread.start();
    return thread;
  }

  private long nextTimeToExecute = Long.MAX_VALUE;
  private final Runnable job;

  private EventsCollectorThread(Runnable job) {
    super("events collector thread");
    setDaemon(true);
    setPriority(MIN_PRIORITY);
    this.job = job;
  }

  public void triggerFilterExecution() {
    this.nextTimeToExecute = System.currentTimeMillis() + 200;
    interrupt();
  }

  @Override
  public void run() {
    while (true)
      try {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= nextTimeToExecute) {
          SwingUtilities.invokeLater(job);
          synchronized (this) {
            wait();
          }
        } else {
          sleep(nextTimeToExecute - currentTime);
        }
      } catch (InterruptedException ex) {
      }
  }

}
