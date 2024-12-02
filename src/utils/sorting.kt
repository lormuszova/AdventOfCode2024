package utils

fun quickSort(arr: Array<Int>): Array<Int> {
    if (arr.size <= 1) return arr
    val pivot = arr[arr.size / 2]
    val equal = arr.filter { it == pivot }.toTypedArray()
    val less = arr.filter { it < pivot }.toTypedArray()
    val greater = arr.filter { it > pivot }.toTypedArray()
    return quickSort(less) + equal + quickSort(greater)
}