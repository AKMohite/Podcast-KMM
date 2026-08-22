package com.mak.pocketnotes.android.feature.casestudy

// Longest common substring: Write a function to find the longest common substring in an array of strings.If no public substring exists, an empty string “” is returned.
// Example 1:
// Input：strs = ["abcde","bcd"]
// Output：”bcd"
// Example 2:
// Input：strs = ["abc","def"]
// Output：””
// Prompt:
// 1 <= strs.length <= 200
// 0 <= strs[i].length <= 200
// If strs[i] it is not empty, it consists of only lowercase English letters

fun main() {
  val input = listOf("acbde", "cbd")
  val oputput = longestSubstring(input)
  println(oputput)
}

fun longestSubstring(input: List<String>): String {
  // [a: 1, c: 2, b: 2, d: 2, e: 1]
  val characterMap = mutableMapOf<Char, Int>()
  for (str in input) {
    for (c in str) {
      characterMap[c] = characterMap.getOrDefault(c, 0) + 1
    }
  }
  val a = characterMap.toList().sortedByDescending { pair -> pair.second }
  val highestOccurence = a.firstOrNull()?.second ?: 0 // 2
  if (highestOccurence <= 1) return "" // no occurence
  return a.filter { (key, value) -> value == highestOccurence }.map { (key, value) -> key }.joinToString()
//     //    characterMap.sortDescending{ it }
//        characterMap.sortByDescending { map -> map.value } // [c: 2, b: 2, d: 2, a: 1, e: 1]
//        val highestOccurence = characterMap.firstOrNull()?.value ?: 0 // 2
//        if(highestOccurence <= 1) return "" // no occurence
//        characterMap.filter{ map -> map.value == highestOccurence } // [c: 2, b: 2, d: 2]

//        return characterMap.map{ map-> map.key }.joinToString() // [c, b, d] -> "cbd"
//    return "$characterMap"
}
