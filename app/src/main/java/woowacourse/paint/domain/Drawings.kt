package woowacourse.paint.domain

data class Drawings(val value: List<Drawing> = emptyList()) {
    fun add(drawing: Drawing): Drawings = Drawings(value + drawing)
}
