package woowacourse.paint.view

import android.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import woowacourse.paint.domain.BrushColor
import woowacourse.paint.domain.BrushWidth
import woowacourse.paint.domain.Drawings
import woowacourse.paint.view.model.mapper.DrawingMapper.toDomain
import woowacourse.paint.view.model.mapper.DrawingMapper.toModel
import woowacourse.paint.view.model.mapper.DrawingsMapper.toModel
import woowacourse.paint.view.model.pen.EllipsePen
import woowacourse.paint.view.model.pen.EraserPen
import woowacourse.paint.view.model.pen.LinePen
import woowacourse.paint.view.model.pen.Pen
import woowacourse.paint.view.model.pen.RectPen
import woowacourse.paint.view.model.pen.ink.Ink
import woowacourse.paint.view.model.pen.ink.Inks

class PaintViewModel : ViewModel() {
    private var color: Int = BrushColor.paintColors[0]
    private var strokeWidth: Float = BrushWidth.range.start

    private val _drawings: MutableLiveData<Drawings> = MutableLiveData(Drawings())
    val lines: LiveData<Inks>
        get() = Transformations.map(_drawings) { it.toModel() }

    private val _pen: MutableLiveData<Pen> = MutableLiveData(
        EllipsePen(onAddInk = ::addInk).apply {
            setColor(color)
        }
    )
    val pen: LiveData<Pen>
        get() = _pen

    fun clearInk() {
    }

    fun undoInk() {
    }

    fun redoInk() {
    }

    fun updateColor(color: Int) {
        this.color = color
        _pen.value.also {
            it?.setColor(color)
        }
    }

    fun updateStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        _pen.value.also {
            it?.setStrokeWidth(strokeWidth)
        }
    }

    fun changeToLinePen() {
        _pen.value = LinePen(onAddInk = ::addInk).apply {
            setColor(color)
            setStrokeWidth(strokeWidth)
        }
    }

    fun changeToRectPen() {
        _pen.value = RectPen(onAddInk = ::addInk).apply {
            setColor(color)
        }
    }

    fun changeToEllipsePen() {
        _pen.value = EllipsePen(onAddInk = ::addInk).apply {
            setColor(color)
        }
    }

    fun changeToEraserPen() {
        _pen.value = EraserPen(
            requestPaths = ::getInks, removePathAt = ::removeInkAt
        )
    }

    private fun addInk(ink: Ink) {
        _drawings.value = _drawings.value?.add(ink.toDomain())
    }

    private fun getInks(): List<Path> {
        return _drawings.value?.value?.map {
            it.toModel().path
        } ?: emptyList()
    }

    private fun removeInkAt(index: Int) {
        _drawings.value = Drawings(
            _drawings.value?.value?.toMutableList().apply {
                this?.removeAt(index)
            } ?: emptyList()
        )
    }
}
