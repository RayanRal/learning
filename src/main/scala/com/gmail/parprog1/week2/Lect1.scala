package com.gmail.parprog1.week2

import com.gmail.parprog1.week1.Lect7.parallel

/**
  * Created by rayanral on 9/22/16.
  */
object Lect1 extends App {

  def parMergeSort(xs: Array[Int], maxDepth: Int): Unit = {
    val ys = new Array[Int](xs.length)

    def sort(from: Int, until: Int, depth: Int): Unit = {
      if(depth == maxDepth)
        quickSort(xs, from, until - from)
      else {
        val mid = (from + until) / 2
        parallel(sort(from, mid, depth + 1), sort(mid, until, depth + 1))
        val flip = (maxDepth - depth) % 2 == 0
        val src = if(flip) ys else xs
        val dest = if(flip) xs else ys
        merge(src, dest, from, mid, until)
      }
    }

    def copy(src: Array[Int], dest: Array[Int], from: Int, until: Int, depth: Int): Unit = {
      if(depth == maxDepth) {
        Array.copy(src, from, dest, from, until - from)
      } else {
        val mid = (from + until) / 2
        val right = parallel(
          copy(src, dest, from, mid, depth + 1),
          copy(src, dest, mid, until, depth + 1)
        )
      }
    }

    def merge(src: Array[Int], dest: Array[Int], from: Int, mid: Int, until: Int): Unit = ???
  }

  def quickSort(xs: Array[Int], from: Int, until: Int) = ???



}
