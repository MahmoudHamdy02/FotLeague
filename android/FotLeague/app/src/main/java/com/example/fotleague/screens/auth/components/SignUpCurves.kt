package com.example.fotleague.screens.auth.components

import android.graphics.Matrix
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import com.example.fotleague.ui.theme.Primary

fun Modifier.drawSignUpTopAndBottomCurves(topPadding: Dp): Modifier {
    return this.drawWithCache {
        onDrawBehind {
            val path =
                PathParser()
                    .parsePathString("M0 0H360V56.7304C180 0 231.151 154.497 0 56.7304V0Z")
                    .toPath()
            val pathSize = path.getBounds().size
            val matrix = Matrix()
            matrix.postScale(size.width / pathSize.width, size.width / pathSize.width)
            path
                .asAndroidPath()
                .transform(matrix)

            val bottomPath =
                PathParser()
                    .parsePathString("M240 114H0V0.425781C36.4399 20.3214 63.5 72 125 72C186.5 72 240 114 240 114Z")
                    .toPath()
            bottomPath.moveTo(0f, 0f)
            var bottomPathSize = bottomPath.getBounds().size
            val bottomMatrix = Matrix()
            bottomMatrix.postScale(
                (2f / 3f) * size.width / bottomPathSize.width,
                (2f / 3f) * size.width / bottomPathSize.width
            )
            bottomPath
                .asAndroidPath()
                .transform(bottomMatrix)
            bottomPathSize = bottomPath.getBounds().size

            path.translate(Offset(0f, -topPadding.toPx()))
            bottomPath.translate(Offset(0f, size.height - bottomPathSize.height))
            drawPath(path, Primary)
            drawPath(bottomPath, Primary)
        }
    }
}