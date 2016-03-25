/*
 * Copyright (c) 2014 Azavea.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geotrellis.raster.histogram

import geotrellis.raster.summary.Statistics


/**
  * Data object representing a histogram of values.
  */
abstract trait Histogram[@specialized (Int, Double) T <: AnyVal] extends Serializable {

  /**
   * Return the number of occurrences for 'item'.
   */
  def itemCount(item: T): Int

  /**
   * Return the total number of occurrences for all items.
   */
  def totalCount(): Int

  /**
   * Return the smallest item seen.
   */
  def minValue(): Option[T]

  /**
   * Return the largest item seen.
   */
  def maxValue(): Option[T]

  /**
   * Return the smallest and largest items seen as a tuple.
   */
  def minMaxValues(): Option[(T, T)] = {
    val min = minValue
    val max = maxValue
    if (min.nonEmpty && max.nonEmpty)
      Some(min.get, max.get)
    else
      None
  }

  /**
   * Return a mutable copy of this histogram.
   */
  def mutable(): MutableHistogram[T]

  /**
    * Return a sorted array of values seen by this histogram.
    */
  def values(): Array[T]

  /**
    * Return an array containing the values seen by this histogram.
    */
  def rawValues(): Array[T]

  /**
    * Execute the given function on the value and count of each bucket
    * in the histogram.
    */
  def foreach(f: (T, Int) => Unit): Unit

  /**
    * Execute the given function on the value of each bucket in the
    * histogram.
    *
    * @param  f  A unit function of one parameter
    */
  def foreachValue(f: T => Unit): Unit

  /**
    * Compute the quantile breaks of the histogram, where the latter
    * are evenly spaced in 'num' increments starting at zero percent.
    */
  def quantileBreaks(num: Int): Array[T]

  /**
    * Compute the mode of the distribution represented by the
    * histogram.
    */
  def mode(): Option[T]

  /**
    * Compute the median of the distribution represented by the
    * histogram.
    */
  def median(): Option[T]

  /**
    * Compute the mean of the distribution represented by the
    * histogram.
    */
  def mean(): Option[Double]

  /**
    * Return a statistics object for the distribution represented by
    * the histogram.  Contains among other things: mean, mode, median,
    * and so-forth.
    */
  def statistics(): Option[Statistics[T]]

  /**
    * The number of buckets utilized by this [[Histogram]].
    */
  def bucketCount(): Int

  /**
    * Return the sum of this histogram and the given one (the sum is
    * the histogram that would result from seeing all of the values
    * seen by the two antecedent histograms).
    */
  def merge(histogram: Histogram[T]): Histogram[T]
}
