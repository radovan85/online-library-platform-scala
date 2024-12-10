package com.radovan.spring.utils

import java.nio.charset.StandardCharsets
import java.util.Random
import org.springframework.stereotype.Component

@Component
class RandomStringUtil {

  def getAlphaNumericString(n: Int): String = {
    val array = new Array[Byte](256)
    new Random().nextBytes(array)
    val randomString = new String(array, StandardCharsets.UTF_8)
    val r = new StringBuffer
    var count = n
    for (k <- 0 until randomString.length if count > 0) {
      val ch = randomString.charAt(k)
      if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
        r.append(ch)
        count -= 1
      }
    }
    r.toString
  }
}